package com.cosooki.adb.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cosooki.adb.command.Command;
import com.cosooki.adb.util.DateUtils;
import com.cosooki.adb.util.Logger;


public abstract class Handler {
    private static final String TAG = Handler.class.getSimpleName();
    
    protected String _startTime;
    protected String _endTime;
    
    public Handler() {
        _startTime = DateUtils.getTimeString(System.currentTimeMillis());
    }
    
    
    public void stop() {
        _endTime = DateUtils.getTimeString(System.currentTimeMillis());
    }

    public abstract String getCommand();
    
    public abstract <T> void handle(Command<T> command);

    public abstract void onWriteTo(FileWriter file, boolean verbose);

    public void writeTo(File file, boolean verbose) {        
        stop();
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
            writer.write("\n\n");            
            onWriteTo(writer, verbose);
        } catch (IOException e) {
            Logger.v(TAG, e.toString());
        } finally {
            try { if (writer != null) writer.close(); } catch (Exception e) {}
        }
         
    }
}
