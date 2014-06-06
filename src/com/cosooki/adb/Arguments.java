package com.cosooki.adb;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
import com.cosooki.adb.command.Command;
import com.cosooki.adb.util.IOUtils;
import com.cosooki.adb.util.Utils;

public class Arguments {

    private static final String ADB_CMD_UNIX = "adb";
    private static final String ADB_CMD_WINDOW = "adb.exe";
    private static final int REPEAT_COUNT = 100;

    private boolean verbose;    
    private String adbCmd;
    private String command;
    private int repeatCount;
    private String output;

    public Arguments() {
        verbose = false;
        adbCmd = (Utils.isWindowOS()) ? ADB_CMD_WINDOW : ADB_CMD_UNIX;
        repeatCount = REPEAT_COUNT;
        command = "top";
    }

    public boolean getVerbose() {
        return verbose;
    }

    public String getAdbCommand() {
        return adbCmd;
    }

    public int getRepeatCount() {
        return repeatCount;
    }
    public String getCommand() {
        return command;
    }
    
    public String[] getCommandArgs() {
        return Command.COMMAND_ARGUMENTS.get(command);
    }
    
    public String getOutputFile() {
        return output;
    }

    public void parse(String[] args) throws Exception {
        ArgumentsParser parser = new ArgumentsParser(args);

        while (parser.getNext()) {
            if (parser.isArg("--verbose")) {
                verbose = true;
            } else if (parser.isArg("--adb=")) {
                adbCmd = parser.getLastValue();
            } else if (parser.isArg("--command=")) {
                command = parser.getLastValue();
            }else if (parser.isArg("--repeat=")) {
                try {
                    repeatCount = Integer.parseInt(parser.getLastValue());
                } catch (NumberFormatException nfe) {}
            } else if (parser.isArg("--output=")) {
                output = parser.getLastValue();
            }
        }
        
        if (IOUtils.isEmpty(command) || Command.SUPPORT_COMMANDS.get(command) == null) {
            System.err.println("Not Support command: " + command);
            throw new Exception("Not Support command: " + command);
        }
    }

    private static class ArgumentsParser {
        /** The arguments to process. */
        private final String[] arguments;
        /** The index of the next argument to process. */
        private int index;
        /** The current argument being processed after a {@link #getNext()} call. */
        private String current;
        /** The last value of an argument processed by {@link #isArg(String)}. */
        private String lastValue;

        public ArgumentsParser(String[] arguments) {
            this.arguments = arguments;
            index = 0;
        }

        public String getCurrent() {
            return current;
        }

        public String getLastValue() {
            return lastValue;
        }

        /**
         * Moves on to the next argument.
         * Returns false when we ran out of arguments that start with --.
         */
        public boolean getNext() {
            if (index >= arguments.length) {
                return false;
            }
            current = arguments[index];
            if (current.equals("--") || !current.startsWith("--")) {
                return false;
            }
            index++;
            return true;
        }

        /**
         * Similar to {@link #getNext()}, this moves on the to next argument.
         * It does not check however whether the argument starts with --
         * and thus can be used to retrieve values.
         */
        private boolean getNextValue() {
            if (index >= arguments.length) {
                return false;
            }
            current = arguments[index];
            index++;
            return true;
        }

        /**
         * Returns all the arguments that have not been processed yet.
         */
        public String[] getRemaining() {
            int n = arguments.length - index;
            String[] remaining = new String[n];
            if (n > 0) {
                System.arraycopy(arguments, index, remaining, 0, n);
            }
            return remaining;
        }

        /**          
         * Checks the current argument against the given prefix.
         * If prefix is in the form '--name=', an extra value is expected.
         * The argument can then be in the form '--name=value' or as a 2-argument
         * form '--name value'.
         * @throws Exception
         */
        public boolean isArg(String prefix) throws Exception {
            int n = prefix.length();
            if (n > 0 && prefix.charAt(n-1) == '=') {
                // Argument accepts a value. Capture it.
                if (current.startsWith(prefix)) {
                    // Argument is in the form --name=value, split the value out
                    lastValue = current.substring(n);
                    return true;
                } else {
                    // Check whether we have "--name value" as 2 arguments
                    prefix = prefix.substring(0, n-1);
                    if (current.equals(prefix)) {
                        if (getNextValue()) {
                            lastValue = current;
                            return true;
                        } else {
                            System.err.println("Missing value after parameter " + prefix);
                            throw new Exception();
                        }
                    }
                    return false;
                }
            } else {
                // Argument does not accept a value.
                return current.equals(prefix);
            }
        }
    }       
}
