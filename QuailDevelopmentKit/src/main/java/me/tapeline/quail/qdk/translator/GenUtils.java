package me.tapeline.quail.qdk.translator;

import org.apache.commons.lang3.StringUtils;

public class GenUtils {

    public static <T> String arrayToCode(T[] array) {
        return "{" + StringUtils.join(array, ", ") + "}";
    }

    public static String[] tabulate(String[] lines) {
        String[] newLines = new String[lines.length];
        for (int i = 0; i < lines.length; i++) newLines[i] = "    " + lines[i];
        return newLines;
    }

    public static String[] tabulate(String[] lines, int times) {
        String tabulator = StringUtils.repeat("    ", times);
        String[] newLines = new String[lines.length];
        for (int i = 0; i < lines.length; i++) newLines[i] = tabulator + lines[i];
        return newLines;
    }


}
