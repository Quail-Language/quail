package me.tapeline.quailj.runtime.std.ji.javaclass;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.runtime.std.ji.Arg;
import me.tapeline.quailj.runtime.std.ji.JIJavaException;
import me.tapeline.quailj.runtime.std.ji.javaobject.JavaObject;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableValueException;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;

public class JavaClass extends QObject {

    public static JavaClass prototype = null;
    public static JavaClass prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new JavaClass(
                    new Table(Dict.make(
                            new Pair<>("CLASS", QObject.Val(0)),
                            new Pair<>("ABSTRACT", QObject.Val(1)),
                            new Pair<>("INTERFACE", QObject.Val(2))
                    )),
                    "JavaClass",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    private Class<?> clazz;

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClass(Class<?> clazz) {
        this.clazz = clazz;

    }

    public JavaClass(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public JavaClass(Table table, String className, QObject parent, boolean isPrototype,
                     Class<?> clazz) {
        super(table, className, parent, isPrototype);
        this.clazz = clazz;
    }

    public JavaClass(Class<?> clazz) {
        this(new Table(), prototype.className, prototype, false, clazz);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new JavaClass(new Table(), className, this, false, clazz);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new JavaClass(new Table(), className, this, true, clazz);
    }

    @Override
    public QObject copy() {
        QObject copy = new JavaClass(table, className, parent, isPrototype, clazz);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    @Override
    public QObject call(Runtime runtime, List<QObject> args, HashMap<String, QObject> kwargs) throws RuntimeStriker {
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))
            runtime.error(new QDerivationException(
                    "Cannot instantinate abstract class or interface", this));
        Constructor<?>[] constructors = clazz.getConstructors();
        Constructor<?> foundConstructor = null;
        Class<?>[] classes = Arg.getClassesFromArgs(args);
        for (Constructor<?> constructor : constructors) {
            if (Arg.isApplicable(constructor.getParameterTypes(), classes)) {
                foundConstructor = constructor;
                break;
            }
        }
        if (foundConstructor == null) {
            runtime.error(new QUnsuitableValueException(
                    "Suitable constructor not found", this));
            return Val();
        }
        try {
            Object object = foundConstructor.newInstance(Arg.transformArgs(args));
            return new JavaObject(object);
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException e) {
            runtime.error(new JIJavaException(e));
        }
        return Val();
    }
}
