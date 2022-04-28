package com.koala.linkfilterapp.linkfilterapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.logging.Logger;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = "com.koala.linkfilterapp")
@ComponentScan(basePackages = "com.koala.linkfilterapp")
@EntityScan("com.koala.linkfilterapp")
@EnableJpaRepositories("com.koala.linkfilterapp")
@EnableTransactionManagement
@EnableCaching
public class LinkfilterappApplication {
	Logger log = Logger.getLogger("LinkfilterApplication");

	public static void main(String[] args) {
		SpringApplication.run(LinkfilterappApplication.class, args);
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.koala.linkfilterapp")).build();
	}
}
