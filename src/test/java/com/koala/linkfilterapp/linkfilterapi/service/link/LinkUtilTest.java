package com.koala.linkfilterapp.linkfilterapi.service.link;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static com.koala.linkfilterapp.linkfilterapi.service.link.LinkUtils.convertStringToDate;

public class LinkUtilTest {

    @Test
    public void shouldConvertStringToDate() throws ParseException {
        String input = "1995-01-18";
        String dateFormat = "yyyy-MM-dd";
        Date date = convertStringToDate(input, dateFormat);

    }
}
