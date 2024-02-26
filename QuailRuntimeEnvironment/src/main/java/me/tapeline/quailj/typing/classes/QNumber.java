package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;

public class QNumber extends QObject {

    public static final QNumber prototype = new QNumber(
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
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QNumber(new Table(), className, this, false, value);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
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

    @Override
    public String toString() {
        if ((value % 1) == 0)
            return Long.toString((long) value);
        return Double.toString(value);
    }

    @Override
    public QObject defaultSum(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value + ((QNumber) other).value);
        return super.sum(runtime, other);
    }

    @Override
    public QObject defaultSubtract(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value - ((QNumber) other).value);
        return super.subtract(runtime, other);
    }

    @Override
    public QObject defaultMultiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value * ((QNumber) other).value);
        return super.multiply(runtime, other);
    }

    @Override
    public QObject defaultDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value / ((QNumber) other).value);
        return super.divide(runtime, other);
    }

    @Override
    public QObject defaultIntDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val((long) (value / ((QNumber) other).value));
        return super.intDivide(runtime, other);
    }

    @Override
    public QObject defaultModulo(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value % ((QNumber) other).value);
        return super.modulo(runtime, other);
    }

    @Override
    public QObject defaultPower(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(Math.pow(value, ((QNumber) other).value));
        return super.power(runtime, other);
    }

    @Override
    public QObject defaultShiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value - ((QNumber) other).value);
        return super.shiftLeft(runtime, other);
    }

    @Override
    public QObject defaultShiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value + ((QNumber) other).value);
        return super.shiftRight(runtime, other);
    }

    @Override
    public QObject defaultEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value == ((QNumber) other).value);
        return super.equalsObject(runtime, other);
    }

    @Override
    public QObject defaultNotEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value != ((QNumber) other).value);
        return super.notEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultGreater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value > ((QNumber) other).value);
        return super.greater(runtime, other);
    }

    @Override
    public QObject defaultGreaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value >= ((QNumber) other).value);
        return super.greaterEqual(runtime, other);
    }

    @Override
    public QObject defaultLess(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value < ((QNumber) other).value);
        return super.less(runtime, other);
    }

    @Override
    public QObject defaultLessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value <= ((QNumber) other).value);
        return super.lessEqual(runtime, other);
    }

    @Override
    public QObject defaultNegate(Runtime runtime) {
        return QObject.Val(-value);
    }

    @Override
    public QObject defaultConvertToString(Runtime runtime) {
        return QObject.Val(toString());
    }

    @Override
    public QObject defaultConvertToBool(Runtime runtime) {
        return QObject.Val(value != 0);
    }

    @Override
    public QObject defaultConvertToNumber(Runtime runtime) {
        return this;
    }

    /**
     * Only for things like HashSets.
     * This thing shouldn't be used for
     * actual comparison of objects.
     * Use {@link QObject#equalsObject} instead!
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof QNumber && ((QNumber) obj).value == value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

}
