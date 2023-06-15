package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;

public class QBool extends QObject {

    public static QBool prototype = new QBool(
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
    public QObject derive() throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QBool(new Table(), className, this, false, value);
    }

    @Override
    public QObject extendAs(String className) throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QBool(new Table(), className, this, true, value);
    }

    @Override
    public QObject copy() {
        QObject copy = new QBool(table, className, parent, isPrototype, value);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

}
