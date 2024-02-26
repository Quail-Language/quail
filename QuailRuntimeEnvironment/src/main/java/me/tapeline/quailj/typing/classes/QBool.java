package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;

public class QBool extends QObject {

    public static final QBool prototype = new QBool(
            new Table(),
            "Bool",
            QObject.superObject,
            true
    );

    public static final QBool globalTrue = new QBool(true);
    public static final QBool globalFalse = new QBool(false);

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
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QBool(new Table(), className, this, false, value);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
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

    @Override
    public QObject defaultNot(Runtime runtime) {
        return value? QBool.globalFalse : QBool.globalTrue;
    }

    @Override
    public QObject defaultConvertToString(Runtime runtime) {
        return Val(toString());
    }

    @Override
    public QObject defaultConvertToBool(Runtime runtime) {
        return this;
    }

    @Override
    public QObject defaultConvertToNumber(Runtime runtime) {
        return value? Val(1) : Val(0);
    }

    @Override
    public QObject defaultAnd(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isBool())
            return Val(value && ((QBool) other).value);
        return super.and(runtime, other);
    }

    @Override
    public QObject defaultOr(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isBool())
            return Val(value || other.boolValue());
        return super.or(runtime, other);
    }

    /**
     * Only for things like HashSets.
     * This thing shouldn't be used for
     * actual comparison of objects.
     * Use {@link QObject#equalsObject} instead!
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof QBool && ((QBool) obj).value == value;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }

}
