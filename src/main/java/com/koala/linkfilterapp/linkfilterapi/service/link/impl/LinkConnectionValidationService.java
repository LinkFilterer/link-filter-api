package com.koala.linkfilterapp.linkfilterapi.service.link.impl;

import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.api.link.enums.LinkStatus;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.service.link.validator.LinkValidator.parseUrlToDomainString;
import static java.util.Objects.isNull;

@Service
public class LinkConnectionValidationService {
    Logger log = Logger.getLogger("LinkVerificationService");


    public void isValidConnectionJSoup(Link link) {
        String parsed = null;
        try {
            parsed = parseUrlToDomainString(link.getUrl());
            URL url = new URL(parsed);
            Connection.Response response = Jsoup.connect(parsed)
                    .followRedirects(false)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .method(Connection.Method.GET)
                    .timeout(5000)
                    .execute();
            int statusCode = response.statusCode();
            link.setStatusCode(statusCode);
            log.info(response.url().toExternalForm() + " : " + statusCode);
            if (statusCode >= 500) { // Redirection, Client Error, or Server Error
                link.setIsConnectable(false);
                link.setStatus(LinkStatus.INVALID);
            } else if (statusCode >= 100) {
                link.setStatus(LinkStatus.VERIFIED);
                link.setSecurityLevel(9);
                link.setIsConnectable(true);
            } else {
                link.setStatus(LinkStatus.INVALID);
                link.setIsConnectable(false);
            }
        } catch (SocketTimeoutException e) {
            log.info(String.format("Retrying %s with 'http://'", parsed));
            isValidConnectionJSoupHttp(link);
        } catch (HttpStatusException e) {
            log.log(Level.WARNING, e.toString());
            link.setStatus(LinkStatus.UNKNOWN);
            link.setStatusCode(e.getStatusCode());
            link.setIsConnectable(false);
        } catch (Exception e) {
            log.info(String.format("Retrying %s with 'http://'", parsed));
            isValidConnectionJSoupHttp(link);
        }
    }

    public void isValidConnectionJSoupHttp(Link link) {
        String parsed = "http://" + link.getUrl();
        try {
            Connection.Response response = Jsoup.connect(parsed)
                    .followRedirects(true)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .execute();
            int statusCode = response.statusCode();
            link.setStatusCode(statusCode);
            log.info(response.url().toExternalForm() + " : " + statusCode);
            if (statusCode >= 500) { // Redirection, Client Error, or Server Error
                link.setIsConnectable(false);
                link.setStatus(LinkStatus.INVALID);
            } else if (statusCode >= 100) {
                link.setStatus(LinkStatus.VALID);
                link.setIsConnectable(true);
            } else {
                link.setStatus(LinkStatus.INVALID);
                link.setIsConnectable(false);
            }
        } catch (SocketTimeoutException e) {
            log.log(Level.WARNING, e.toString());
            link.setStatus(LinkStatus.UNKNOWN);
            link.setIsConnectable(false);

        } catch (HttpStatusException e) {
            log.log(Level.WARNING, e.toString());
            link.setStatus(LinkStatus.UNKNOWN);
            link.setStatusCode(e.getStatusCode());
            link.setIsConnectable(false);
        } catch (Exception e) {
            log.log(Level.WARNING, e.toString());
            link.setStatus(LinkStatus.UNKNOWN);
            link.setIsConnectable(false);
        }
    }

    // Selenium Verification
    public void setIsConnectableSelenium(List<Link> links) {
        System.setProperty("webdriver.chrome.driver", "resources\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("--disable-blink-features=AutomationControlled");
        WebDriver driver = new ChromeDriver(options);
        String parsed = null;
        String resolvedUrl = null;
        for (Link link : links) {
            try {
                if (isNull(driver)) {
                    driver = new ChromeDriver(options);
                }
                parsed = "https://" + link.getUrl();
                driver.get(parsed);
                resolvedUrl = driver.getCurrentUrl();
                link.setStatus(LinkStatus.VERIFIED);
                link.setSecurityLevel(9);
                link.setIsConnectable(true);
            } catch (WebDriverException e) {
                log.info(resolvedUrl + " failed to connect");
                log.warning(e.toString());
                link.setIsConnectable(false);
            } catch (Exception e) {
                log.log(Level.WARNING, e.toString());
            }
        }
        driver.quit();
    }

    // Selenium verify one link
    public boolean isConnectable(String url) {
        try {
            System.setProperty("webdriver.chrome.driver", "resources\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            options.addArguments("--disable-blink-features=AutomationControlled");

            String parsedUrl = parseUrlToDomainString(url);
            WebDriver driver = new ChromeDriver(options);
            driver.get(parsedUrl);
            driver.quit();
            return true;
        } catch (Exception e) {
            log.log(Level.WARNING, e.toString());
            return false;
        }
    }

    public boolean isValidConnection(String url) {
        try {
            String parsed = parseUrlToDomainString(url);
            Connection.Response response = Jsoup.connect("https://" + parsed)
                    .followRedirects(true)
                    .execute();
            int statusCode = response.statusCode();
            if (statusCode >= 400) { //Client Error, or Server Error
                return false;
            } else if (statusCode >= 100 && !isDNSError(response)) {
                log.info(response.url().toExternalForm() + " : " + statusCode);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.log(Level.WARNING, e.toString());
            return false;
        }
    }

    private boolean isDNSError(Connection.Response response) {
        return response.headers().containsValue("close");
    }
}
