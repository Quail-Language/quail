package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;

public class QString extends QObject {

    public static QString prototype = new QString(
            new Table(),
            "String",
            QObject.superObject,
            true
    );

    protected String value;

    public QString(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QString(Table table, String className, QObject parent, boolean isPrototype, String value) {
        super(table, className, parent, isPrototype);
        this.value = value;
    }

    public QString(String value) {
        this(new Table(), prototype.className, prototype, false);
        this.value = value;
    }

    @Override
    public QObject derive() throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QString(new Table(), className, this, false, value);
    }

    @Override
    public QObject extendAs(String className) throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QString(new Table(), className, this, true, value);
    }

    @Override
    public QObject copy() {
        QObject copy = new QString(table, className, parent, isPrototype, value);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
