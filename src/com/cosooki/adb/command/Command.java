package com.cosooki.adb.command;

import java.util.HashMap;
import java.util.List;

import com.cosooki.adb.util.DateUtils;

public class Command<T> {
    private static final String TAG = Command.class.getSimpleName();
    
    public static final HashMap<String, Class<?>> SUPPORT_COMMANDS;
    public static final HashMap<String, String[]> COMMAND_ARGUMENTS;
    
    static {
        SUPPORT_COMMANDS = new HashMap<String, Class<?>>();
        SUPPORT_COMMANDS.put("top", TopCommand.class);
        
        COMMAND_ARGUMENTS = new HashMap<String, String[]>();
        COMMAND_ARGUMENTS.put("top", new String[]{"-m", "20", "-n", "1"});
    }
    
    protected List<T> mItems;
    
    protected String callTime;
    
    public Command() {
        callTime = DateUtils.getTimeString(System.currentTimeMillis());
    }
    
    public List<T> getItems() {
        return mItems;
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("["
                + this.getClass().getSimpleName() + "]").append("\n");
        if (mItems != null) {
            for (T item : mItems) {
                sb.append("[item] = ").append(item.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}
