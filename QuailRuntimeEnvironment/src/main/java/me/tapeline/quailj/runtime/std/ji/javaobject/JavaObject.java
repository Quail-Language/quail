package me.tapeline.quailj.runtime.std.ji.javaobject;

import me.tapeline.quailj.GlobalFlags;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.runtime.std.ji.Arg;
import me.tapeline.quailj.runtime.std.ji.JIJavaException;
import me.tapeline.quailj.runtime.std.ji.javamethod.JavaMethod;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class JavaObject extends QObject {

    public static JavaObject prototype = null;
    public static JavaObject prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new JavaObject(
                    new Table(Dict.make(
                        new Pair<>("pack", new JavaObjectFuncPack(runtime)),
                        new Pair<>("getClass", new JavaObjectFuncGetClass(runtime))
                    )),
                    "JavaObject",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    private Object object;
    protected FieldCache fieldCache = new FieldCache(GlobalFlags.jiFieldCacheSize);
    protected Set<String> fieldNames = new HashSet<>();
    protected Set<String> methodNames = new HashSet<>();
    protected MethodCache methodCache = new MethodCache(GlobalFlags.jiMethodCacheSize);

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public JavaObject(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public JavaObject(Table table, String className, QObject parent, boolean isPrototype,
                      Object object) {
        super(table, className, parent, isPrototype);
        this.object = object;
        initializeNameCache();
    }

    public JavaObject(Object object) {
        this(new Table(), prototype.className, prototype, false, object);
    }

    protected void initializeNameCache() {
        for (Field field : FieldUtils.getAllFields(object.getClass()))
            fieldNames.add(field.getName());
        for (Method method : object.getClass().getMethods())
            methodNames.add(method.getName());
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new JavaObject(new Table(), className, this, false, object);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new JavaObject(new Table(), className, this, true, object);
    }

    @Override
    public QObject copy() {
        QObject copy = new JavaObject(table, className, parent, isPrototype, object);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    @Override
    public void set(Runtime runtime, String name, QObject value) {
        if (object == null) {
            super.set(name, value);
            return;
        }
        set(name, value);
    }

    @Override
    public void set(String name, QObject value, int[] modifiers) {
        if (object == null) {
            super.set(name, value);
            return;
        }
        set(name, value);
    }

    @Override
    public void set(String name, QObject value) {
        if (object == null) {
            super.set(name, value);
            return;
        }
        try {
            Field target = fieldCache.tryToGetCachedValue(name);
            if (target == null) {
                for (Field field : FieldUtils.getAllFields(object.getClass())) {
                    if (field.getName().equals(name)) {
                        target = field;
                        break;
                    }
                }
                if (target != null)
                    fieldCache.cacheValue(target);
            }
            if (target == null) return;
            if (!target.isAccessible()) {
                target.setAccessible(true);
                target.set(object, Arg.transform(value));
                target.setAccessible(false);
            } else {
                target.set(object, Arg.transform(value));
            }
        } catch (IllegalAccessException e) {
            super.set(name, value);
        }
    }

    @Override
    public QObject get(String name) {
        if (object == null) return super.get(name);
        if (fieldNames.contains(name)) {
            try {
                Field target = fieldCache.tryToGetCachedValue(name);
                if (target == null) {
                    for (Field field : FieldUtils.getAllFields(object.getClass())) {
                        if (field.getName().equals(name)) {
                            target = field;
                            break;
                        }
                    }
                    fieldCache.cacheValue(target);
                }
                if (!target.isAccessible()) {
                    target.setAccessible(true);
                    Object value = target.get(object);
                    target.setAccessible(false);
                    return Arg.transformBack(value);
                } else {
                    return Arg.transformBack(target.get(object));
                }
            } catch (IllegalAccessException e) {
                return super.get(name);
            }
        } else if (methodNames.contains(name)) {
            JavaMethod target = methodCache.tryToGetCachedValue(name);
            if (target == null) {
                List<Method> foundMethods = new ArrayList<>();
                for (Method method : object.getClass().getMethods()) {
                    if (method.getName().equals(name))
                        foundMethods.add(method);
                }
                target = new JavaMethod(
                        object,
                        null,
                        foundMethods.toArray(new Method[]{})
                );
                methodCache.cacheValue(target);
            }
            return target;
        } else
            return super.get(name);
    }

    @Override
    public QObject callFromThis(Runtime runtime, String func, List<QObject> args, HashMap<String, QObject> kwargs) throws RuntimeStriker {
        Method[] methods = object.getClass().getMethods();
        Class<?>[] classes = Arg.getClassesFromArgs(args);
        Method foundMethod = null;
        for (Method method : methods) {
            if (!method.getName().equals(func))
                continue;
            if (Arg.isApplicable(method.getParameterTypes(), classes)) {
                foundMethod = method;
                break;
            }
        }
        if (foundMethod == null)
            return super.callFromThis(runtime, func, args, kwargs);
        try {
            Object result = foundMethod.invoke(object, Arg.transformArgs(args));
            return Arg.transformBack(result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            runtime.error(new JIJavaException(e));
        }
        return Val();
    }
}
