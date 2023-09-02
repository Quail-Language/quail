package me.tapeline.quailj.utils;

import me.tapeline.quailj.typing.classes.QObject;

import java.util.Arrays;
import java.util.List;

public class QStringUtils {

    public static List<QObject> divide(String string, int count) {
        int partSize = string.length() / count;
        QObject[] parts = new QObject[partSize * count == string.length()? count : count + 1];
        for (int i = 0; i < count; i++)
            parts[i] = QObject.Val(string.substring(i * partSize, (i + 1) * partSize));
        if (parts.length != count)
            parts[count] = QObject.Val(string.substring(count * partSize));
        return Arrays.asList(parts);
    }

    public static List<QObject> intDivide(String string, int count) {
        int partSize = string.length() / count;
        QObject[] parts = new QObject[count];
        for (int i = 0; i < count; i++)
            parts[i] = QObject.Val(string.substring(i * partSize, (i + 1) * partSize));
        return Arrays.asList(parts);
    }

    public static String reverse(String s) {
        StringBuilder r = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) r.append(s.charAt(i));
        return r.toString();
    }

    public static String capitalize(String s) {
        return (s.charAt(0) + "").toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String shift(String s, int amount) {
        String str = "";
        if (amount > 0)
            str += s.substring(s.length() - amount) + s.substring(0, s.length() - amount);
        else if (amount < 0)
            str += s.substring(Math.abs(amount)) + s.substring(0, Math.abs(amount));
        return str;
    }

    public static String subscript(String string, Integer start, Integer end, Integer step) {
        if (start == null)
            start = 0;
        if (end == null)
            end = string.length();
        if (step == null)
            step = 1;
        StringBuilder sb = new StringBuilder();
        if (step > 0)
            for (int i = start; i < end; i += step)
                sb.append(string.charAt(i));
        else
            for (int i = end - 1; i >= start; i += step)
                sb.append(string.charAt(i));
        return sb.toString();
    }

}