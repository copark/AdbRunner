package com.cosooki.adb.util;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public class Utils {
    

    public static boolean isWindowOS() {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().startsWith("window")) {
            return true;
        } else {
            return false;
        }
    }

    public static String shCommand() {        
        if (isWindowOS()) {
            return "bash.exe";
        } else {
            return "/bin/sh";
        }
    }
    
    public static InputStream executeProcess(String selectProcess, String[] commandSet) {
        InputStream retStream = null;
        
        try {
            ProcessBuilder psBuilder = new ProcessBuilder(commandSet);
            Map<String, String> env = psBuilder.environment();
            env.putAll(System.getenv());

//            String path = isWindowOS() ? ("Path") : ("PATH");
//            String appender = isWindowOS() ? (";") : (":");
//            String str = env.get(path);
//            str = str + appender + System.getProperty("user.dir");
//            env.put(path, str);
            
            File cmdFile = new File(commandSet[0]);
            commandSet[0] = cmdFile.getAbsolutePath();
            Logger.v("COMMAND", commandSet[0], true);

//            psBuilder.directory(new File(System.getProperty("user.dir")));
//            psBuilder.redirectErrorStream(true);
            
            Process ps = psBuilder.start();
            Logger.v(selectProcess + " execute!!!");

            retStream = IOUtils.copyInputStream(ps.getInputStream());

            int processValue = ps.waitFor();
            int exitValue = ps.exitValue();

            ps.destroy();
            if (exitValue != 0) {
                Logger.v("", selectProcess + " fail !!!", true);                
                return null;
            } else {
                return retStream;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.v("", selectProcess + " fail !!!", true);
            System.exit(-1);
            return null;
        }
    }

    private Utils() {} 
}
