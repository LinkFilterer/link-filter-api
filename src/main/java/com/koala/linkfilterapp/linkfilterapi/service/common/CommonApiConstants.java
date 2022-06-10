package com.koala.linkfilterapp.linkfilterapi.service.common;

public class CommonApiConstants {
    // SETTINGS
    public static long CONNECTION_THRESHOLD = 20; // Request Limit per Min

    public static long REPORT_INSPECTION_THRESHOLD = 50;


    public static String SERVICE_LOG_MESSAGE = "LinkServiceImpl ";

    // Response
    public static String REQUEST_DATE_FORMAT = "ddMMyyHHmm";

    // STATUS CONSTANTS
    public static String STATUS_UNKNOWN = "UNKNOWN";
    public static String STATUS_UNPROCESSED = "UNPROCESSED"; //Verification unknown
    public static String STATUS_VERIFIED = "VERIFIED";
    public static String STATUS_VALID = "VALID";
    public static String STATUS_INVALID = "INVALID";
    public static String STATUS_NEEDS_INSPECTION = "INSPECT";
    public static String STATUS_SUSPICIOUS = "SUSPICIOUS";
    public static String STATUS_NOT_CONNECTABLE = "NOT_CONNECTABLE";
    public static String STATUS_NOT_PARSABLE = "NOT_PARSABLE";

    // REPORT STATUS
    public static String REPORT_VALID = "VALID";
    public static String REPORT_INVALID = "INVALID";

    // REQUEST TYPES
    public static String CHECK_LINK_CD = "C";
    public static String CHECK_LINK = "CHECK_LINK";
    public static String REPORT_LINK_CD = "R";
    public static String REPORT_LINK = "REPORT_LINK";

}
