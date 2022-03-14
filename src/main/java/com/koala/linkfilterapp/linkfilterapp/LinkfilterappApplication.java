package com.koala.linkfilterapp.linkfilterapp;

import com.koala.linkfilterapp.linkfilterapi.api.common.entity.LoginHistory;
import com.koala.linkfilterapp.linkfilterapi.repository.LoginHistoryRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.logging.Logger;

@EnableSwagger2
@SpringBootApplication
@ComponentScan(basePackages = "com.*")
@EntityScan("com.koala.linkfilterapp.linkfilterapi.api.*")
@EnableJpaRepositories("com.koala.linkfilterapp.linkfilterapi.*")
public class LinkfilterappApplication {
	Logger log = Logger.getLogger("LinkfilterApplication");

	@Autowired
	LoginHistoryRepository loginHistoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(LinkfilterappApplication.class, args);
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.koala.linkfilterapp")).build();
	}

	@Component
	public class SecurityEventListener implements ApplicationListener<AbstractAuthenticationEvent>  {
		@SneakyThrows
		@Override
		public void onApplicationEvent(AbstractAuthenticationEvent event)  {
			WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
			Optional<LoginHistory> userLogin = loginHistoryRepository.findById(details.getRemoteAddress());
			if (userLogin.isPresent()) {
				userLogin.get().setName(event.getAuthentication().getName());
				userLogin.get().setAuthenticated(event.getAuthentication().isAuthenticated());
				userLogin.get().setAccessCount(userLogin.get().getAccessCount() + 1L);
				userLogin.get().setLatestAccessDate(new Timestamp(System.currentTimeMillis()));
				if(event.getAuthentication().isAuthenticated()) {
					userLogin.get().setSuccessfulLogins(userLogin.get().getSuccessfulLogins() + 1L);
				}
				loginHistoryRepository.save(userLogin.get());
			} else {
				LoginHistory history = new LoginHistory();
				history.setIp(details.getRemoteAddress());
				history.setAuthenticated(event.getAuthentication().isAuthenticated());
				history.setAccessCount(1L);
				history.setLatestAccessDate(new Timestamp(System.currentTimeMillis()));
				history.setInitialAccessDate(new Timestamp(System.currentTimeMillis()));
				if(event.getAuthentication().isAuthenticated()) {
					history.setSuccessfulLogins(1L);
				} else {
					history.setSuccessfulLogins(0L);
				}
				loginHistoryRepository.save(history);
			}
		}
	}
}
