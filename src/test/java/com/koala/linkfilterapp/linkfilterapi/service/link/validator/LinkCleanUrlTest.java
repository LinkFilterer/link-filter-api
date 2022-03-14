package com.koala.linkfilterapp.linkfilterapi.service.link.validator;

import com.koala.linkfilterapp.linkfilterapi.api.common.exception.LinkException;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

public class LinkCleanUrlTest {
    String validStatus;

    @Before
    public void setup() {
        validStatus = "Error: Bad URL";
    }

    @Test
    public void shouldValidateUrl() throws Exception {
        String input1 = "https://www.google.com/extensionstuff";
        String input2 = "www.google.com";
        String input3 = "google.com";
        String input4 = "http://www.google.com/extensionstuff";
        String input5 = "http://en.m.wikipedia.org/wiki/Domain_Name_System#Domain_name_syntax";

        List<String> inputList = new ArrayList<>();
        inputList.add(input1);
        inputList.add(input2);
        inputList.add(input3);
        inputList.add(input4);
        inputList.add(input5);
        for(String input : inputList) {
            //Boolean response = isValidUrl(input);
            //System.out.println(input + " : " + response);
            // assertTrue(response);
        }

    }

    @Test
    public void shouldParseUrl() throws Exception {
        String input1 = "https://www.google.com/extensionstuff";
        String input2 = "www.bad.com/www.aax.com";
        String input3 = "http://en.m.wikipedia.org/wiki/Domain_Name_System#Domain_name_syntax";
        String input4 = "    www.google.com/extensionstuff";
        String input5 = "https://www.bbc.co.uk/";
        String input6 = "bbc.co.uk/";

        List<String> inputList = new ArrayList<>();
        inputList.add(input1);
        inputList.add(input2);
        inputList.add(input3);
        inputList.add(input4);
        inputList.add(input5);
        inputList.add(input6);

        for(String input : inputList) {
//            URL response = parseUrl(input);

//            System.out.println(input + " Host Name : " + response.getHost());
        }

    }


    @Test (expected = LinkException.class)
    public void shouldNot_ParseUrl() throws Exception {
        String input1 = "12341234125";
        String input2 = "googǀe.com";
        String input3 = "!@#$!@^!^";
        String input4 = "googƖe.com";
        String input5 = ".コアラ";
        String input6 = "htttp://en.m.wikipedia.org/wiki/Domain_Name_System#Domain_name_syntax";


        List<String> inputList = new ArrayList<>();
        inputList.add(input1);
        inputList.add(input2);
        inputList.add(input3);
        inputList.add(input4);
        inputList.add(input5);
        inputList.add(input6);
        for(String input : inputList) {
//            Boolean response = isValidUrl(input);
//            assertFalse(response);
        }


    }

}
