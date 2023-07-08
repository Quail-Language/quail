package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;

import java.util.*;

public class QObject {

    public static QObject Val() {
        return new QNull();
    }
    public static QObject Val(double value) {
        return new QNumber(value);
    }
    public static QObject Val(boolean value) {
        return new QBool(value);
    }
    public static QObject Val(String value) {
        return new QString(value);
    }
    public static QObject Val(List<QObject> value) {
        return superObject;
    }
    public static QObject Val(HashMap<String, QObject> value) {
        return superObject;
    }
    public static QObject superObject = new QObject(new Table(), "Object", null, true);

    protected Table table = new Table();
    protected String className;
    protected QObject parent;

    protected boolean isPrototype;
    protected boolean isInheritable = true;

    public QObject(Table table, String className, QObject parent, boolean isPrototype) {
        this.table = table;
        this.className = className;
        this.parent = parent;
        this.isPrototype = isPrototype;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public QObject getParent() {
        return parent;
    }

    public void setParent(QObject superClass) {
        this.parent = superClass;
    }

    public boolean isPrototype() {
        return isPrototype;
    }

    public void setPrototypeFlag(boolean prototype) {
        isPrototype = prototype;
    }

    public boolean isInheritable() {
        return isInheritable;
    }

    public void setInheritableFlag(boolean inheritable) {
        isInheritable = inheritable;
    }

    public QObject getSuperClass() {
        return isPrototype? parent : parent.getSuperClass();
    }

    public QObject getPrototype() {
        return isPrototype? this : parent;
    }

    public QObject derive() throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QObject(new Table(), className, this, false);
    }

    public QObject extendAs(String className) throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QObject(new Table(), className, this, true);
    }

    public QObject newObject(Runtime runtime, List<QObject> args) throws RuntimeStriker {
        QObject blank = derive();
        blank = blank.callFromThis(runtime, "_constructor", args, new HashMap<>());
        return blank;
    }

    public QObject copy() {
        QObject copy = new QObject(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public QObject clone() {
        QObject copy = copy();
        copy.getTable().clear();
        table.forEach((k, v) -> copy.getTable().put(
                k,
                v.clone(),
                table.getModifiersFor(k)
        ));
        copy.setPrototypeFlag(isPrototype());
        return copy;
    }

    public QObject get(String name) {
        if (name.startsWith("_")) {
            if (name.equals("_className")) return new QString(className);
            else if (name.equals("_superClass")) return getSuperClass();
            else if (name.equals("_objectPrototype")) return getPrototype();
            else if (name.equals("_isPrototype")) return new QBool(isPrototype);
            else if (name.equals("_isInheritable")) return new QBool(isInheritable);
        }
        if (table.containsKey(name))
            return table.get(name);
        else if (parent != null)
            return parent.get(name);
        else
            return new QNull();
    }

    public void set(String name, QObject value) throws RuntimeStriker {
        table.put(name, value);
    }

    public void set(String name, QObject value, int[] modifiers) {
        table.put(name, value, modifiers);
    }

    public void forceSet(String name, QObject value) {
        table.put(name, value, new int[0]);
    }

    public QObject getOverridable(Runtime runtime, String name) throws RuntimeStriker {
        QObject getter = get("_get");
        if (getter instanceof QFunc)
            return callFromThis(
                    runtime,
                    getter,
                    Collections.singletonList(QObject.Val(name)),
                    new HashMap<>()
            );
        return getter;
    }

    public final QObject callFromThis(Runtime runtime, QObject func, List<QObject> args,
                                      HashMap<String, QObject> kwargs) throws RuntimeStriker {
        if (!isPrototype())
            args.add(0, this);
        return func.call(runtime, args, kwargs);
    }

    public final QObject callFromThis(Runtime runtime, String func, List<QObject> args,
                                      HashMap<String, QObject> kwargs) throws RuntimeStriker {
        return callFromThis(runtime, get(func), args, kwargs);
    }

    // Actions

    public QObject call(Runtime runtime, List<QObject> args, HashMap<String, QObject> kwargs)
            throws RuntimeStriker {
        if (isPrototype()) {
            QObject newObject = derive();
            return newObject.callFromThis(runtime, "_constructor", args, kwargs);
        }
        if (table.containsKey("_call"))
            return callFromThis(
                    runtime,
                    "_call",
                    Arrays.asList(QObject.Val(args), QObject.Val(kwargs)),
                    new HashMap<>()
            );
        Runtime.error(getClassName() + " is not callable");
        return Val();
    }

}
