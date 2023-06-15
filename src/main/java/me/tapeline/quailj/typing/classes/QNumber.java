package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;

public class QNumber extends QObject {

    public static QNumber prototype = new QNumber(
            new Table(),
            "Number",
            QObject.superObject,
            true
    );

    protected double value;

    public QNumber(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QNumber(Table table, String className, QObject parent, boolean isPrototype, double value) {
        super(table, className, parent, isPrototype);
        this.value = value;
    }

    public QNumber(double value) {
        this(new Table(), prototype.className, prototype, false);
        this.value = value;
    }

    @Override
    public QObject derive() throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QNumber(new Table(), className, this, false, value);
    }

    @Override
    public QObject extendAs(String className) throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QNumber(new Table(), className, this, true, value);
    }

    @Override
    public QObject copy() {
        QObject copy = new QNumber(table, className, parent, isPrototype, value);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
