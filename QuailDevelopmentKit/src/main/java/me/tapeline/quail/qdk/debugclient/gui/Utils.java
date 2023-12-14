package me.tapeline.quail.qdk.debugclient.gui;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.BufferedReader;

public class Utils {

    public static String getDataFromReader(BufferedReader reader) {
        try {
            char[] cb = (char[]) FieldUtils.getField(BufferedReader.class, "cb", true).get(reader);
            String data = new String(cb);
            return data;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
