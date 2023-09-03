package me.tapeline.quailj.runtime.std.ji;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QFunc;

import java.util.HashMap;

public class JIMethodRegistry {

    private static final HashMap<Long, QFunc> methods = new HashMap<>();
    private static final HashMap<Long, Runtime> assignedRuntimes = new HashMap<>();

    public static void add(long id, QFunc method, Runtime runtime) {
        methods.put(id, method);
        assignedRuntimes.put(id, runtime);
    }

    public static long add(QFunc method, Runtime runtime) {
        int hash = method.hashCode();
        add(hash, method, runtime);
        return hash;
    }

    public static QFunc get(long id) {
        return methods.get(id);
    }

    public static Runtime getRuntime(long id) {
        return assignedRuntimes.get(id);
    }

    public static int size() {
        return methods.size();
    }

}
