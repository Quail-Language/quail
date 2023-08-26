package me.tapeline.quailj.runtime.std.ji;

import me.tapeline.quailj.runtime.std.ji.javaclass.JavaClass;
import me.tapeline.quailj.runtime.std.ji.javaobject.JavaObject;
import me.tapeline.quailj.typing.classes.*;
import org.apache.commons.lang3.ClassUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arg {

    public static Object transform(QObject object) {
        if (object instanceof QNumber) {
            if (((double) ((int) object.numValue())) == object.numValue()) {
                if (object.numValue() <= Integer.MAX_VALUE)
                    return (int) object.numValue();
                else
                    return (long) object.numValue();
            } else {
                if (object.numValue() <= Float.MAX_VALUE)
                    return (float) object.numValue();
                else
                    return object.numValue();
            }
        } else if (object instanceof QString) {
            return object.strValue();
        } else if (object instanceof QBool) {
            return object.boolValue();
        } else if (object instanceof QNull) {
            return null;
        } else if (object instanceof QList) {
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < object.listValue().size(); i++) {
                list.add(transform(object.listValue().get(i)));
            }
            return list;
        } else if (object instanceof QDict) {
            HashMap<String, Object> dict = new HashMap<>();
            for (Map.Entry<String, QObject> entry : object.dictValue().entrySet()) {
                dict.put(entry.getKey(), transform(entry.getValue()));
            }
            return dict;
        } else if (object instanceof JavaClass) {
            return ((JavaClass) object).getClazz();
        }
        return object;
    }

    public static Class<?> getArgClass(QObject object) {
        if (object instanceof QNumber) {
            if (((double) ((int) object.numValue())) == object.numValue()) {
                if (object.numValue() <= Integer.MAX_VALUE)
                    return int.class;
                else
                    return long.class;
            } else {
                if (object.numValue() <= Float.MAX_VALUE)
                    return float.class;
                else
                    return double.class;
            }
        } else if (object instanceof QString) {
            return String.class;
        } else if (object instanceof QBool) {
            return boolean.class;
        } else if (object instanceof QNull) {
            return null;
        } else if (object instanceof QList) {
            return List.class;
        } else if (object instanceof QDict) {
            return HashMap.class;
        } else if (object instanceof JavaClass) {
            return Class.class;
        }
        return object.getClass();
    }

    public static Object[] transformArgs(List<QObject> args) {
        Object[] transformed = new Object[args.size()];
        for (int i = 0; i < args.size(); i++) {
            transformed[i] = transform(args.get(i));
        }
        return transformed;
    }

    public static Class<?>[] getClassesFromArgs(List<QObject> args) {
        Class<?>[] classes = new Class<?>[args.size()];
        for (int i = 0; i < args.size(); i++) {
            classes[i] = getArgClass(args.get(i));
        }
        return classes;
    }

    public static boolean isApplicable(Class<?>[] defined, Class<?>[] provided) {
        if (defined.length != provided.length) return false;
        for (int i = 0; i < provided.length; i++) {
            if (provided[i] == null) continue;
            if (!ClassUtils.isAssignable(provided[i], defined[i], true)) {
                return false;
            }
        }
        return true;
    }

    public static QObject transformBack(Object obj) {
        if (obj instanceof Number)
            return QObject.Val(((Number) obj).doubleValue());
        else if (obj instanceof String)
            return QObject.Val(((String) obj));
        else if (obj instanceof Boolean)
            return QObject.Val(((Boolean) obj));
        else if (obj == null)
            return QObject.Val();
        else if (obj instanceof List<?>) {
            List<QObject> list = new ArrayList<>();
            for (int i = 0; i < ((List<?>) obj).size(); i++)
                list.add(transformBack(((List<?>) obj).get(i)));
            return QObject.Val(list);
        } else if (obj instanceof Map<?, ?>) {
            HashMap<String, QObject> dict = new HashMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet())
                dict.put(entry.getKey().toString(), transformBack(entry.getValue()));
            return QObject.Val(dict);
        } else if (obj instanceof QObject)
            return ((QObject) obj);
        else
            return new JavaObject(obj);
    }

    public static List<QObject> transformArgsBack(Object... args) {
        List<QObject> list = new ArrayList<>();
        for (Object arg : args)
            list.add(transformBack(arg));
        return list;
    }

}
