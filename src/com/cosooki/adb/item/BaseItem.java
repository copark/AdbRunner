package com.cosooki.adb.item;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.cosooki.adb.util.IOUtils;

public abstract class BaseItem {
    public static final String FIELD_ID = "_id";
    public static final String FIELD_NAME = "_name";
    
    protected  Map<String, String> map = new LinkedHashMap<String, String>();
        
    public void setValue(String key, String value) {
        map.put(key, value);
    }
    
    public String getValue(String key) {
        return map.get(key);
    }
    
    public Set<String> keySet() {
        return map.keySet();
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");

        for (String key : map.keySet()) {
            sb.append(key).append(":").append(map.get(key)).append("\n");
        }
        return sb.toString();
    }
    
    public class IDCompare implements Comparator<BaseItem> {
        @Override
        public int compare(BaseItem arg0, BaseItem arg1) {
            return _compare(arg0, arg1, FIELD_ID, false);
        }
    }

    public class NameCompare implements Comparator<BaseItem> {
        @Override
        public int compare(BaseItem arg0, BaseItem arg1) {
            return _compare(arg0, arg1, FIELD_NAME, false);
        }
    }
    
    protected static int _compare(BaseItem arg0, BaseItem arg1, String field, boolean intValue) {
        String val0 = arg0.getValue(field);
        String val1 = arg1.getValue(field);
        if (IOUtils.isEmpty(val0) || IOUtils.isEmpty(val1)) {
            return 0;
        }
        
        if (intValue) {
            return IOUtils.parseInt(val0) - IOUtils.parseInt(val1);
        } else {
            return val0.compareTo(val1);
        }
    }
}
