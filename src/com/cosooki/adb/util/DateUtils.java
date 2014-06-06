package com.cosooki.adb.util;

import java.util.Date;

public class DateUtils {
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    
    public static final String getCurrentTimeString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        return sdf.format(new Date());
    }
    
    public static final String getTimeString(long time) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        return sdf.format(time);
    }
}
