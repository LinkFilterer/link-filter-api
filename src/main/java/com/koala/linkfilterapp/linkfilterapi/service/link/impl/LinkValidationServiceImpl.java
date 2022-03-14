package com.koala.linkfilterapp.linkfilterapi.service.link.impl;

import com.google.common.net.InternetDomainName;
import com.koala.linkfilterapp.linkfilterapi.service.link.LinkValidationService;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LinkValidationServiceImpl implements LinkValidationService {

    Logger log = Logger.getLogger("LinkValidationService");

    @Override
    public List<String> validateLinkRequest(String url, String ipAddress) {
        List<String> errors = new ArrayList<>();
        validateUrl(errors, url);

        return errors;
    }

    private void validateUrl(List<String> errors, String strUrl) {
        strUrl = strUrl.trim();


        try {
            if (isValidUrlString("www."+ strUrl) && !strUrl.startsWith("www.")) { // no https, no www prefix
                strUrl ="https://www." + strUrl;
            } else if (isValidUrlString(strUrl) && !isHttpPrefixed(strUrl)) { // has www, no http prefix
                strUrl = "https://" + strUrl;
            } else { // invalid
                errors.add("Regex does not match");
            }
            InternetDomainName.from(new URL(strUrl).getHost()).topPrivateDomain().toString();
        } catch (Exception e) {
            errors.add("Unexpected exception occurred while validating url: " + e);
        }
    }

    private boolean isValidUrlString(String url) {
        // regex will always be a valid URL i.e. starts with http:// and ends with a domain name
        Pattern p = Pattern.compile("(?i)\\b((?:[a-z][\\w-]+:(?:\\/{1,3}|[a-z0-9%])|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}\\/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))");
        Matcher m = p.matcher(url);
        return(m.matches());
    }

    private boolean isHttpPrefixed(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}
