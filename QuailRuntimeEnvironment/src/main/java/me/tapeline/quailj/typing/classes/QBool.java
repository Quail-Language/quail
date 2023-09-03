package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;

public class QBool extends QObject {

    public static final QBool prototype = new QBool(
            new Table(),
            "Bool",
            QObject.superObject,
            true
    );

    protected boolean value;

    public QBool(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QBool(Table table, String className, QObject parent, boolean isPrototype, boolean value) {
        super(table, className, parent, isPrototype);
        this.value = value;
    }

    public QBool(boolean value) {
        this(new Table(), prototype.className, prototype, false);
        this.value = value;
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QBool(new Table(), className, this, false, value);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QBool(new Table(), className, this, true, value);
    }

    @Override
    public QObject copy() {
        QObject copy = new QBool(table, className, parent, isPrototype, value);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

}
