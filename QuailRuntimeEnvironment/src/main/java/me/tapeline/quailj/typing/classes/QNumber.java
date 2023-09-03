package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;

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
            runtime.error("Attempt to inherit from non-prototype value");
        return new QNumber(new Table(), className, this, false, value);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
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
            return Integer.toString((int) value);
        return Double.toString(value);
    }

    @Override
    public QObject sum(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value + ((QNumber) other).value);
        return super.sum(runtime, other);
    }

    @Override
    public QObject subtract(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value - ((QNumber) other).value);
        return super.subtract(runtime, other);
    }

    @Override
    public QObject multiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value * ((QNumber) other).value);
        return super.multiply(runtime, other);
    }

    @Override
    public QObject divide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value / ((QNumber) other).value);
        return super.divide(runtime, other);
    }

    @Override
    public QObject intDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val((long) (value / ((QNumber) other).value));
        return super.intDivide(runtime, other);
    }

    @Override
    public QObject modulo(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value % ((QNumber) other).value);
        return super.modulo(runtime, other);
    }

    @Override
    public QObject power(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(Math.pow(value, ((QNumber) other).value));
        return super.power(runtime, other);
    }

    @Override
    public QObject shiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value - ((QNumber) other).value);
        return super.shiftLeft(runtime, other);
    }

    @Override
    public QObject shiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value + ((QNumber) other).value);
        return super.shiftRight(runtime, other);
    }

    @Override
    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value == ((QNumber) other).value);
        return super.equalsObject(runtime, other);
    }

    @Override
    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value != ((QNumber) other).value);
        return super.notEqualsObject(runtime, other);
    }

    @Override
    public QObject greater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value > ((QNumber) other).value);
        return super.greater(runtime, other);
    }

    @Override
    public QObject greaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value >= ((QNumber) other).value);
        return super.greaterEqual(runtime, other);
    }

    @Override
    public QObject less(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value < ((QNumber) other).value);
        return super.less(runtime, other);
    }

    @Override
    public QObject lessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return QObject.Val(value <= ((QNumber) other).value);
        return super.lessEqual(runtime, other);
    }

    @Override
    public QObject negate(Runtime runtime) {
        return QObject.Val(-value);
    }

    @Override
    public QObject convertToString(Runtime runtime) {
        return QObject.Val(toString());
    }

    @Override
    public QObject convertToBool(Runtime runtime) {
        return QObject.Val(value != 0);
    }

    @Override
    public QObject convertToNumber(Runtime runtime) {
        return this;
    }


}
