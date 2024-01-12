package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
            return Integer.toString((int) value);
        return Double.toString(value);
    }

    @Override
    public QObject sum(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_add"))
            return callFromThis(
                    runtime,
                    "_add",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value + ((QNumber) other).value);
        return super.sum(runtime, other);
    }

    @Override
    public QObject subtract(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_sub"))
            return callFromThis(
                    runtime,
                    "_sub",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value - ((QNumber) other).value);
        return super.subtract(runtime, other);
    }

    @Override
    public QObject multiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_mul"))
            return callFromThis(
                    runtime,
                    "_mul",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value * ((QNumber) other).value);
        return super.multiply(runtime, other);
    }

    @Override
    public QObject divide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_div"))
            return callFromThis(
                    runtime,
                    "_div",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value / ((QNumber) other).value);
        return super.divide(runtime, other);
    }

    @Override
    public QObject intDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_intdiv"))
            return callFromThis(
                    runtime,
                    "_intdiv",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val((long) (value / ((QNumber) other).value));
        return super.intDivide(runtime, other);
    }

    @Override
    public QObject modulo(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_mod"))
            return callFromThis(
                    runtime,
                    "_mod",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value % ((QNumber) other).value);
        return super.modulo(runtime, other);
    }

    @Override
    public QObject power(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_pow"))
            return callFromThis(
                    runtime,
                    "_pow",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(Math.pow(value, ((QNumber) other).value));
        return super.power(runtime, other);
    }

    @Override
    public QObject shiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_shl"))
            return callFromThis(
                    runtime,
                    "_shl",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value - ((QNumber) other).value);
        return super.shiftLeft(runtime, other);
    }

    @Override
    public QObject shiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_shr"))
            return callFromThis(
                    runtime,
                    "_shr",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value + ((QNumber) other).value);
        return super.shiftRight(runtime, other);
    }

    @Override
    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_eq"))
            return callFromThis(
                    runtime,
                    "_eq",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value == ((QNumber) other).value);
        return super.equalsObject(runtime, other);
    }

    @Override
    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_neq"))
            return callFromThis(
                    runtime,
                    "_neq",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value != ((QNumber) other).value);
        return super.notEqualsObject(runtime, other);
    }

    @Override
    public QObject greater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_cmpg"))
            return callFromThis(
                    runtime,
                    "_cmpg",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value > ((QNumber) other).value);
        return super.greater(runtime, other);
    }

    @Override
    public QObject greaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_cmpge"))
            return callFromThis(
                    runtime,
                    "_cmpge",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value >= ((QNumber) other).value);
        return super.greaterEqual(runtime, other);
    }

    @Override
    public QObject less(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_cmpl"))
            return callFromThis(
                    runtime,
                    "_cmpl",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value < ((QNumber) other).value);
        return super.less(runtime, other);
    }

    @Override
    public QObject lessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (containsKey("_cmple"))
            return callFromThis(
                    runtime,
                    "_cmple",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        if (other.isNum())
            return QObject.Val(value <= ((QNumber) other).value);
        return super.lessEqual(runtime, other);
    }

    @Override
    public QObject negate(Runtime runtime) throws RuntimeStriker {
        if (containsKey("_neg"))
            return callFromThis(
                    runtime,
                    "_neg",
                    new ArrayList<>(),
                    new HashMap<>()
            );
        return QObject.Val(-value);
    }

    @Override
    public QObject convertToString(Runtime runtime) throws RuntimeStriker {
        if (containsKey("_tostring"))
            return callFromThis(
                    runtime,
                    "_tostring",
                    new ArrayList<>(),
                    new HashMap<>()
            );
        return QObject.Val(toString());
    }

    @Override
    public QObject convertToBool(Runtime runtime) throws RuntimeStriker {
        if (containsKey("_tobool"))
            return callFromThis(
                    runtime,
                    "_tobool",
                    new ArrayList<>(),
                    new HashMap<>()
            );
        return QObject.Val(value != 0);
    }

    @Override
    public QObject convertToNumber(Runtime runtime) throws RuntimeStriker {
        if (containsKey("_tonum"))
            return callFromThis(
                    runtime,
                    "_tonum",
                    new ArrayList<>(),
                    new HashMap<>()
            );
        return this;
    }


}
