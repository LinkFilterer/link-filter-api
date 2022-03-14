package com.koala.linkfilterapp.linkfilterapi.service.link;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

import static java.util.Objects.nonNull;

public class LinkUtils {
    public static Date parseDateString() {
        return null;
    }

    public static String convertDateToString(Date date) {
        String formattedDate = null;
        if (nonNull(date)) {
            String pattern = "MM/dd/yy HHmm 'abc'";
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
            formattedDate = dateTimeFormatter.format(date.toInstant());
        }
        return formattedDate;
    }

    public static Date convertStringToDate(String date, String dateFormat) throws ParseException {
        Date parseDate = null;
        if (StringUtils.hasText(date)) {
            DateFormat dateFormatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            parseDate = dateFormatter.parse(date);
//            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
//            parseDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return parseDate;
    }

    public static Date convertStringToDateTime(String date, String dateFormat) throws DateTimeParseException {
        Date parsedDate = null;
        if (StringUtils.hasText(date)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
            parsedDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return parsedDate;
    }
}
