package com.cosooki.adb;

import com.cosooki.adb.util.Logger;

public class Main {    
    private static final String TAG = Main.class.getSimpleName();

    public static void main(String[] args) {
        Logger.v(TAG, "TopProcessChecker Stat..", true);
        
        Arguments arguments = new Arguments();

        try {
            arguments.parse(args);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
/*
        try {
            arguments.parse(new String[] { 
                   // "--adb=/Users/android-sdks/platform-tools/adb",
                    "--command=top", 
                    "--repeat=5"});
        } catch (Exception e) {}
*/
        Logger.init(arguments.getVerbose());
        
        printArguments(arguments);
        
        Processor processor = new Processor(arguments);
        processor.process();
    }
    
    private static void printArguments(Arguments arguments) {
        Logger.v(TAG, "ADB = " + arguments.getAdbCommand(), true);
        Logger.v(TAG, "COMMAND = " + arguments.getCommand(), true);
        Logger.v(TAG, "REPEAT = " + arguments.getRepeatCount(), true);
    }
}
