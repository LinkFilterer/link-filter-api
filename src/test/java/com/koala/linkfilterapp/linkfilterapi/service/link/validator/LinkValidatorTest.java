package com.koala.linkfilterapp.linkfilterapi.service.link.validator;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static com.koala.linkfilterapp.linkfilterapi.service.link.validator.LinkValidator.parseUrlToDomainString;

public class LinkValidatorTest {
    String validStatus;
    String invalidStatus;
    Logger log = Logger.getLogger("LinkValidatorTest");

    @Before
    public void setup() {
        validStatus = "VALID";
        invalidStatus = "INVALID";
    }

    @Test
    public void parseLink() throws Exception {
        List<String> inputList = Arrays.asList(
                "http://www.alinx.io",
                "http://en.m.wikipedia.org/wiki/Domain_Name_System#Domain_name_syntax",
                "https://www.bbc.co.uk/",
                "https://gist.github.com/ozzi-/7087505de8114df5cee8aed26532d61f",
                "http://paypal.com-webappsuerid29348325limted.active-userid.com/webapps/89980/"
        );
        for(String url : inputList) {
            log.info(parseUrlToDomainString(url));
        }
    }
}
