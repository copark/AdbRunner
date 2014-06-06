package com.cosooki.adb.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {
    private static final int BUFFER_SIZE = 1024*1024;

    public static int parseInt(String value) {
        int ret = -1;

        try {
            ret = Integer.parseInt(value);
        } catch (Exception e) {}

        return ret;
    }

    public static boolean isEmpty(String value) {
        if (value == null || value.length() == 0) {
            return true;
        }

        return false;
    }

    public static InputStream copyInputStream(InputStream in) {
        InputStream ret = null;
        ByteArrayOutputStream bos = null;

        byte[] bytes = new byte[BUFFER_SIZE];
        int read = 0;

        try {
            bos = new ByteArrayOutputStream();
            while ((read = in.read(bytes)) != -1) {
                bos.write(bytes,0,read);
            }

            byte[] ba = bos.toByteArray();
            ret = new ByteArrayInputStream(ba);        
        } catch (IOException ioe) {
        } finally {
            if (bos != null) { try { bos.close(); } catch (Exception e) {} }
            if (in != null) { try { in.close(); } catch (Exception e) {} }
        }

        return ret;
    }
    
    public static void printInputStream(InputStream in) {
        StringBuffer sb = new StringBuffer();
        BufferedReader bri = null;

        String line = null;
        try {
            bri = new BufferedReader(
                    new InputStreamReader(in));
            while ((line = bri.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
        } catch (IOException ioe) {
            if (bri != null) { try { bri.close(); } catch (Exception e) {} }
        }
        
        Logger.v(sb.toString());
    }

    private IOUtils() {}
}
