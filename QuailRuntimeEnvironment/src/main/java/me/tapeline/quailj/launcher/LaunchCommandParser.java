package me.tapeline.quailj.launcher;

import java.util.HashMap;

public class LaunchCommandParser {

    public static final char INT_FLAG_TYPE = 'i';
    public static final char DOUBLE_FLAG_TYPE = 'd';
    public static final char STRING_FLAG_TYPE = 's';
    public static final char BOOLEAN_FLAG_TYPE = 'b';

    private final String[] receivedConsoleArgs;
    private final HashMap<String, Object> globalFlags;
    private final HashMap<String, Object> userFlags;
    private String selectedRunStrategy;
    private String targetScript;

    private String outputFile;

    public LaunchCommandParser(String[] consoleArgs) throws LauncherException {
        if (consoleArgs.length < 1)
            throw new LauncherException("Provide a valid mode");
        this.receivedConsoleArgs = consoleArgs;
        this.globalFlags = new HashMap<>();
        this.userFlags = new HashMap<>();
    }


    public static boolean toBoolean(String strValue) {
        return strValue.equalsIgnoreCase("true") ||
                strValue.equalsIgnoreCase("1") ||
                strValue.equalsIgnoreCase("enable");
    }

    public void parseReceivedArgs() {
        boolean strategySet = false;
        boolean waitingOutputFile = false;
        for (String arg : receivedConsoleArgs) {
            if (arg.startsWith("-")) parseFlag(arg);
            else if (!strategySet) {
                selectedRunStrategy = arg;
                strategySet = true;
                if (selectedRunStrategy.equals("info"))
                    break;
            } else if (arg.equalsIgnoreCase(">")) {
                waitingOutputFile = true;
            } else if (waitingOutputFile) {
                outputFile = arg;
                break;
            } else {
                targetScript = arg;
                if (selectedRunStrategy.equalsIgnoreCase("run") ||
                    selectedRunStrategy.equalsIgnoreCase("profile") ||
                    selectedRunStrategy.equalsIgnoreCase("debug"))
                    break;
            }
        }
    }

    private void parseFlag(String flag) {
        if (flag.startsWith("-G")) {
            if (!flag.contains("="))
                globalFlags.put(flag.substring(3), true);
            else {
                String flagName = flag.substring(3, flag.indexOf('='));
                if (flagName.equals("encoding"))
                    globalFlags.put("encoding", flag.substring(flag.indexOf('=' + 1)));
                if (flagName.equals("ignoreDocs"))
                    globalFlags.put("ignoreDocs", toBoolean(flag.substring(flag.indexOf('=' + 1))));
            }
        } else {
            int assignPosition = 0;
            for (int i = 0; i < flag.length(); i++)
                if (flag.charAt(i) == '=') {
                    assignPosition = i;
                    break;
                }
            char valueType = flag.charAt(1);
            String key = flag.substring(4, assignPosition);
            String strValue = flag.substring(assignPosition + 1);
            Object flagValue = null;
            switch (valueType) {
                case INT_FLAG_TYPE: flagValue = Integer.valueOf(strValue); break;
                case DOUBLE_FLAG_TYPE: flagValue = Double.valueOf(strValue); break;
                case STRING_FLAG_TYPE: flagValue = strValue; break;
                case BOOLEAN_FLAG_TYPE: flagValue = toBoolean(strValue); break;
            }
            if (flagValue != null) {
                userFlags.put(key, flagValue);
            }
        }
    }

    public String[] getReceivedConsoleArgs() {
        return receivedConsoleArgs;
    }

    public HashMap<String, Object> getGlobalFlags() {
        return globalFlags;
    }

    public HashMap<String, Object> getUserFlags() {
        return userFlags;
    }

    public String getSelectedRunStrategy() {
        return selectedRunStrategy;
    }

    public String getTargetScript() {
        return targetScript;
    }

    public String getOutputFile() {
        return outputFile;
    }
}
