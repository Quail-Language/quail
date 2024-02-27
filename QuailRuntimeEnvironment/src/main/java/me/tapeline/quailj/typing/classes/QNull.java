package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;

public class QNull extends QObject {

    public static final QNull prototype = new QNull(
            new Table(),
            "Null",
            QObject.superObject,
            true
    );

    public static final QNull globalNull = new QNull();

    public QNull(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QNull() {
        this(new Table(), prototype.className, prototype, false);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QNull(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QNull(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QNull(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    @Override
    public QObject defaultEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNull()) return Val(true);
        return super.defaultEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultNotEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNull()) return Val(false);
        return super.defaultNotEqualsObject(runtime, other);
    }

    @Override
    public String toString() {
        return "null";
    }

}
