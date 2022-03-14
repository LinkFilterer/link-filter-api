package com.koala.linkfilterapp.linkfilterapi.service.link.impl;

import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import org.apache.commons.net.whois.WhoisClient;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.koala.linkfilterapp.linkfilterapi.service.link.LinkUtils.convertStringToDate;

@Service
public class LinkWhoIsService {
    Logger log = Logger.getLogger("whoIsService");

    private static String WHOIS_SERVICE_URL = "%s.whois-servers.net";
    private static String WHOIS_BLOG_SERVICE_URL = "whois.nic.blog";
    private static String GG_DATE_FORMAT = "dd MMMM yyyy";
    private static String TK_DATE_FORMAT = "MM/dd/yyyy";
    private static String UK_DATE_FORMAT = "dd-MMMM-yyyy";
    private static String UK_OLD_DATE_FORMAT = "MMMM-yyyy";
    private static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public void setWhoIsDate(Link link) {
        String url = link.getUrl();
        if (Strings.isNotBlank(url)) {
            try {
                String extension = getWhoisExtension(url);
                log.info("Processed extension: " + extension);
                String result = getWhoIsData(extension, url);
                Pattern pattern;
                String dateFormat;
                Matcher matcher;
                switch (extension.trim()) {
                    case ("gg"):
                        pattern = Pattern.compile("\\bRegistered on \\b(\\d{1,2}([a-z]{2}) \\b\\w+\\b \\d{4})");
                        dateFormat = GG_DATE_FORMAT;
                        break;
                    case ("tk"):
                        pattern = Pattern.compile("\\bDomain registered: \\b(\\d{2}/\\d{2}/\\d{4}).*");
                        dateFormat = TK_DATE_FORMAT;
                        break;
                    case ("ru"):
                        pattern = Pattern.compile("\\bcreated:       \\b(\\d{4}-\\d{2}-\\d{1,2}).*");
                        dateFormat = DEFAULT_DATE_FORMAT;
                        break;
                    case ("uk"):
                        pattern = Pattern.compile("\\bRegistered on: \\b(\\d{2}-\\b\\w+\\b-\\d{4})");
                        matcher = pattern.matcher(result);
                        if(!matcher.find()) {
                            pattern = Pattern.compile("\\bRegistered on: [b|B]efore \\b(.*)");
                        }
                        dateFormat = UK_DATE_FORMAT;
                        break;
                    case ("cn"):
                        pattern = Pattern.compile("\\bRegistration Time: \\b(\\d{4}-\\d{2}-\\d{1,2}).*");
                        dateFormat = DEFAULT_DATE_FORMAT;
                        break;
                    default:
                        pattern = Pattern.compile("\\bCreation Date: \\b(\\d{4}-\\d{2}-\\d{1,2}).*");
                        dateFormat = DEFAULT_DATE_FORMAT;
                }

                matcher = pattern.matcher(result);
                if (matcher.find()) {
                    log.info("extension found: " + extension);
                    link.setWhoIsDate(getWhoIsDate(matcher, extension, dateFormat));
                } else {
                    log.info("extension not found: " + extension);
                }
            } catch (IOException | ParseException e) {
                log.warning("Error exception: " + e.getMessage());
            }
        }
    }


    private Date getWhoIsDate(Matcher matcher, String extension, String dateFormat) throws ParseException {
        String whoIsDateString;
        if ("gg".equalsIgnoreCase(extension)) {
            whoIsDateString = matcher.group(1).replace(matcher.group(2), "");
        } else {
            whoIsDateString = matcher.group(1);
        }

        Date whoIsDate;
        if ("uk".equalsIgnoreCase(extension)) {
            try {
                log.info(String.format("Attempting to convert '%s' using format '%s'", whoIsDateString, dateFormat));
                whoIsDate = convertStringToDate(whoIsDateString, dateFormat);
            } catch (ParseException e) {
                log.info(String.format("Parsing again, attempting to convert '%s' using format '%s'", whoIsDateString, UK_OLD_DATE_FORMAT));
                whoIsDate = convertStringToDate(whoIsDateString, UK_OLD_DATE_FORMAT);
            }
        } else {
            log.info(String.format("Attempting to convert '%s' using format '%s'", whoIsDateString, dateFormat));
            whoIsDate = convertStringToDate(whoIsDateString, dateFormat);
        }
        return whoIsDate;
    }

    private String getWhoisExtension(String url) {
        String[] result = url.split("\\.");
        return result[result.length - 1];
    }

    private String getWhoIsData(String extension, String url) throws IOException {
        WhoisClient whois = new WhoisClient();

        String serviceUrl;
        if (".blog".equalsIgnoreCase(extension)) {
            serviceUrl = WHOIS_BLOG_SERVICE_URL;
        } else {
            serviceUrl = String.format(WHOIS_SERVICE_URL, extension);
        }
        log.info("Attempting connection to service Url: " + serviceUrl);
        whois.connect(serviceUrl);

        String result = whois.query(url);
        whois.disconnect();
        return result;
    }
}
