package me.tapeline.quailj.utils;

import me.tapeline.quailj.typing.classes.QList;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QListUtils {

    public static List<QObject> multiply(List<QObject> values, int value) {
        List<QObject> v = new ArrayList<>();
        for (int i = 0; i < value; i++) v.addAll(values);
        return v;
    }

    public static List<QObject> divide(List<QObject> list, int count) {
        int partSize = list.size() / count;
        QObject[] parts = new QObject[partSize * count == list.size()? count : count + 1];
        for (int i = 0; i < count; i++)
            parts[i] = QObject.Val(list.subList(i * partSize, (i + 1) * partSize));
        if (parts.length != count)
            parts[count] = QObject.Val(list.subList(count * partSize, list.size()));
        return Arrays.asList(parts);
    }

    public static List<QObject> intDivide(List<QObject> list, int count) {
        int partSize = list.size() / count;
        QObject[] parts = new QObject[count];
        for (int i = 0; i < count; i++)
            parts[i] = QObject.Val(list.subList(i * partSize, (i + 1) * partSize));
        return Arrays.asList(parts);
    }

    public static List<QObject> shift(List<QObject> s, int amount) {
        List<QObject> list = new ArrayList<>();
        if (amount > 0) {
            list.addAll(s.subList(s.size() - amount, s.size()));
            list.addAll(s.subList(0, s.size() - amount));
        } else if (amount < 0) {
            list.addAll(s.subList(Math.abs(amount), s.size()));
            list.addAll(s.subList(0, Math.abs(amount)));
        }
        return list;
    }

    public static List<QObject> subscript(List<QObject> list, Integer start, Integer end, Integer step) {
        if (start == null)
            start = 0;
        if (end == null)
            end = list.size();
        if (step == null)
            step = 1;
        List<QObject> lb = new ArrayList<>();
        if (step > 0)
            for (int i = start; i < end; i += step)
                lb.add(list.get(i));
        else
            for (int i = end - 1; i >= start; i += step)
                lb.add(list.get(i));
        return lb;
    }

    public static List<String> qStringListToStrings(QList list) {
        List<String> strings = new ArrayList<>();
        for (QObject element : list.getValues())
            strings.add(element.strValue());
        return strings;
    }

}