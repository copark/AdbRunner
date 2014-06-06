package com.cosooki.adb.command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.cosooki.adb.item.TopItem;
import com.cosooki.adb.util.IOUtils;
import com.cosooki.adb.util.Logger;

public class TopCommand extends Command<TopItem> {
    
    private static final String TAG = TopCommand.class.getSimpleName();

    private static boolean inited = false;
    
    private static final String PID = "PID";
    private static final String CPU = "CPU%";
    private static final String VSS = "VSS";
    private static final String RSS = "RSS";
    private static final String NAME = "Name";
    
    private static HashMap<String, Integer> sIndexMap = new HashMap<String, Integer>();
    
    // PID PR CPU% S  #THR     VSS     RSS PCY UID      Name
    static {
        sIndexMap.put(PID, 0);
        sIndexMap.put(CPU, 2);
        sIndexMap.put(VSS, 5);
        sIndexMap.put(RSS, 6);
    }

    private static boolean isValidLine(String[] values) {
        boolean ret = false;

        if (values.length <= 0 || IOUtils.isEmpty(values[0])) {
            return ret;
        }
        
        try {
            Integer.parseInt(values[sIndexMap.get(PID)]);
            ret = true;
        } catch (Exception e) {}
        
        return ret;
    }
    
    private static void initIndex(String[] values) {
        if (inited || values == null || values.length > 0) {
            return;
        }
        
        for (int i=0; i<values.length; i++) {
            if (sIndexMap.containsKey(values[i])) {
                sIndexMap.put(values[i],i);
                Logger.v(TAG, "value = " + values[i] + ", index = " + i);
                inited = true;
            }
        }
    }
    
    private static String toIntString(String value) {
        return value.substring(0, value.length() -1);
    }

    public static final TopCommand parse(InputStream in) throws Exception {
        BufferedReader bri = null;
        TopCommand command = null;
        
        try {
            bri = new BufferedReader(new InputStreamReader(in));    
            String line = null;    

            command = new TopCommand();

            while ((line = bri.readLine()) != null) {
                line = line.trim();
                String[] values = line.split("\\s+");
                initIndex(values);
                
                if (!isValidLine(values)) {
                    continue;
                }
                
                TopItem item = new TopItem();
                item.setValue(TopItem.FIELD_ID, values[sIndexMap.get(PID)]);
                item.setValue(TopItem.FIELD_CPU, toIntString(values[sIndexMap.get(CPU)]));
                item.setValue(TopItem.FIELD_NAME, values[values.length-1]);
                item.setValue(TopItem.FIELD_RSS, values[sIndexMap.get(RSS)]);
                item.setValue(TopItem.FIELD_VSS, values[sIndexMap.get(VSS)]);
                
                command.mItems.add(item);
            }
        } finally {
            try { if (bri != null) bri.close(); } catch (Exception e) {}
        }

        return command;
    }
    
    TopCommand() {
        mItems = new ArrayList<TopItem>();
    }

}
