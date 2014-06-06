package com.cosooki.adb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.cosooki.adb.command.Command;
import com.cosooki.adb.handler.Handler;
import com.cosooki.adb.handler.TopHandler;
import com.cosooki.adb.util.DateUtils;
import com.cosooki.adb.util.IOUtils;
import com.cosooki.adb.util.Logger;
import com.cosooki.adb.util.Utils;
import com.cosooki.topcheck.tools.ParseException;
import com.cosooki.topcheck.tools.Parser;

public class Processor {
    private static final String TAG = Processor.class.getSimpleName();
    
    long firstTime;
    long lastTime;
    
    Arguments mArgument;
    public Processor(Arguments argument) {
        mArgument = argument;
    }
    
    public <T> void process() {
        Logger.v(TAG, "Processor Stat..", true);
        
        int repeatCount = mArgument.getRepeatCount();
        Handler handler = initHandler(mArgument);
        
        for (int i=0; i<repeatCount; i++) {
            ArrayList<String> commandSet = new ArrayList<String>();
            commandSet.add(mArgument.getAdbCommand());
            commandSet.add("shell");
            commandSet.add(mArgument.getCommand());
            for ( String arg : mArgument.getCommandArgs()) {
                if(!IOUtils.isEmpty(arg)) {
                    commandSet.add(arg);
                }
            }
            String[] arraySet = new String[commandSet.size()];
            
            InputStream in = Utils.executeProcess("Process " + (i+1) + " count(s)",
                    commandSet.toArray(arraySet));
            if (in == null) {
                Logger.v(TAG, "Process execute failed.", true);        
                continue;
            }

            Logger.v(TAG, "Process " + (i+1) + " count(s) succeed", true);
            
            Parser parser = new Parser();
            try {
                // parse
                Class<?> cmdClass = Command.SUPPORT_COMMANDS.get(mArgument.getCommand());
                Command<T> command = (Command<T>) parser.parse(cmdClass, in);
                Logger.v(TAG, command.toString());
                // handle
                handler.handle(command);
            } catch (ParseException e) {
                Logger.v(TAG, "error in process " + e.toString());
            } finally {
                try { if (in != null) in.close(); } catch (IOException e) {}
            }
        }
        
        // write file
        String fileName = writeFile(handler, mArgument);
        Logger.v(TAG, "Processor terminated..", true);
        Logger.v(TAG, fileName + " is created.", true);
     }
    
    private Handler initHandler(Arguments argument) {
        Handler handler = null;
        if ("top".equals(argument.getCommand())) {
            handler = new TopHandler();
        }

        return handler;
    }
    
    private String getFileName(Arguments argument) {
        return argument.getCommand() + "_" + DateUtils.getCurrentTimeString() + ".xml";
    }
    
    private String writeFile(Handler handler, Arguments argument) {
        File file;
        String fileName = argument.getOutputFile();
        if (fileName == null) {
            fileName = getFileName(argument);
        }
        
        file = new File(fileName);
        handler.writeTo(file, argument.getVerbose());
        
        return file.getAbsolutePath();
    }
}
