package com.koala.linkfilterapp.linkfilterapi.service.link.validator;

import com.google.common.net.InternetDomainName;
import com.koala.linkfilterapp.linkfilterapi.api.common.exception.CommonException;
import com.koala.linkfilterapp.linkfilterapi.api.link.entity.Link;
import com.koala.linkfilterapp.linkfilterapi.api.report.enums.ReportType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LinkValidator {
    public static boolean validateReportType(ReportType reportType) throws CommonException {
        if (reportType.equals(ReportType.VALID)) {
            return true;
        } else if (reportType.equals(ReportType.INVALID)) {
            return false;
        } else {
            CommonException exception = new CommonException();
            exception.setHttpStatus(HttpStatus.BAD_REQUEST);
            exception.setDescription("Invalid Report Type: " + reportType);
            throw exception;
        }
    }

    private static URL parseUrl(String strUrl) throws CommonException {
        strUrl = strUrl.trim();
        if (strUrl.startsWith("https://www.")) {
            strUrl = strUrl.replace("https://www.", "www.");
        }
        try {
            if(isValidUrlString(strUrl) && isHttpPrefixed(strUrl)) {
                URL uri = new URL(strUrl);
                return uri;
            } else if (isValidUrlString("www."+ strUrl) && !strUrl.startsWith("www.")) { // no https, no www prefix
                URL uri = new URL("https://www." + strUrl);
                return uri;
            } else if (isValidUrlString(strUrl) && !isHttpPrefixed(strUrl)) { // has www, no http prefix
                URL uri = new URL("https://" + strUrl);
                return uri;
            } else { // invalid
                CommonException exception = new CommonException();
                exception.setDescription("Regex does not match: " + strUrl);
                throw exception;
            }
        } catch (Exception e) {
            Link badLink = new Link();
            badLink.setUrl(strUrl);
            CommonException exception = new CommonException(HttpStatus.BAD_REQUEST, e.toString(), badLink,  null, null);
            throw exception;
        }
    }

    public static String parseUrlToDomainString(String url) throws CommonException {
        URL parsedUrl = parseUrl(url);
        log.info(parsedUrl.toString());
        return InternetDomainName.from(parsedUrl.getHost()).topPrivateDomain().toString();
    }

    public static boolean isValidUrlString(String url) {
        // regex will always be a valid URL i.e. starts with http:// and ends with a domain name
        Pattern p = Pattern.compile("(?i)\\b((?:[a-z][\\w-]+:(?:\\/{1,3}|[a-z0-9%])|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}\\/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))");
        Matcher m = p.matcher(url);
        return(m.matches());
    }

    public static boolean isHttpPrefixed(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}
