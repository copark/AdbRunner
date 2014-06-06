package com.cosooki.adb.util;

public class Logger {
    
    private static boolean enabled;
    
    public static void init(boolean enable) {
        enabled = enable;
    }

    public static void v(String message) {
        v("Logger", message, false);
    }
    
    public static void v(String tag, String message) {
        v(tag, message, false);
    }

    public static void v(String tag, int value) {
        v(tag, "" + value, false);
    }

    public static void v(String tag, long value) {
        v(tag, "" + value, false);
    }

    public static void v(String tag, boolean value) {
        v(tag, "" + value, false);
    }
    
    public static void v(String tag, String message, boolean forced) {
        if (enabled || forced) {
            System.out.println(getTime() + getTag(tag) + message);
        }
    }
    
    private static String getTag(String tag) {
        return "[" + tag + "]\t";
    }
    
    private static String getTime() {
        return "[" + DateUtils.getCurrentTimeString() + "] ";
    }
    
}
