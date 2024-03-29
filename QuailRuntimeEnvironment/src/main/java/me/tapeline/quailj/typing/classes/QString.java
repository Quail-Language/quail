package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;
import me.tapeline.quailj.typing.classes.errors.QIterationStopException;
import me.tapeline.quailj.utils.QStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.regex.Pattern;

public class QString extends QObject {

    // TODO Revise all toString usages and migrate to strValue()

    public static final QString prototype = new QString(
            new Table(),
            "String",
            QObject.superObject,
            true
    );

    protected String value;
    protected int iteratorIndex;

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
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QString(new Table(), className, this, false, value);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
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

    public QObject defaultSum(Runtime runtime, QObject other) {
        return Val(value + other.toString());
    }

    public QObject defaultSubtract(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.replaceAll(Pattern.quote(other.toString()), ""));
        return super.defaultSubtract(runtime, other);
    }

    @Override
    public QObject defaultMultiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(StringUtils.repeat(value, (int) other.numValue()));
        return super.defaultMultiply(runtime, other);
    }

    @Override
    public QObject defaultDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.divide(value, (int) other.numValue()));
        return super.defaultDivide(runtime, other);
    }

    @Override
    public QObject defaultIntDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.intDivide(value, (int) other.numValue()));
        return super.defaultIntDivide(runtime, other);
    }

    @Override
    public QObject defaultShiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.shift(value, (int) -other.numValue()));
        return super.defaultShiftLeft(runtime, other);
    }

    @Override
    public QObject defaultShiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.shift(value, (int) other.numValue()));
        return super.defaultShiftRight(runtime, other);
    }

    @Override
    public QObject defaultEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.equals(other.toString()));
        return super.defaultEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultNotEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(!value.equals(other.toString()));
        return super.defaultNotEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultContainsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr()) return Val(value.contains(Objects.requireNonNull(other.strValue())));
        return super.defaultContainsObject(runtime, other);
    }

    @Override
    public QObject defaultNotContainsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr()) return Val(!value.contains(Objects.requireNonNull(other.strValue())));
        return super.defaultNotContainsObject(runtime, other);
    }

    @Override
    public QObject defaultGreater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() > other.toString().length());
        return super.defaultGreater(runtime, other);
    }

    @Override
    public QObject defaultGreaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() >= other.toString().length());
        return super.defaultGreaterEqual(runtime, other);
    }

    @Override
    public QObject defaultLess(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() < other.toString().length());
        return super.defaultLess(runtime, other);
    }

    @Override
    public QObject defaultLessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() <= other.toString().length());
        return super.defaultLessEqual(runtime, other);
    }

    @Override
    public QObject defaultIterateStart(Runtime runtime) {
        iteratorIndex = 0;
        return this;
    }

    @Override
    public QObject defaultIterateNext(Runtime runtime) throws RuntimeStriker {
        if (iteratorIndex == value.length())
            runtime.error(new QIterationStopException());
        return Val(String.valueOf(value.charAt(iteratorIndex++)));
    }

    @Override
    public QObject defaultConvertToString(Runtime runtime) {
        return this;
    }

    @Override
    public QObject defaultConvertToBool(Runtime runtime) {
        return Val(Boolean.parseBoolean(value));
    }

    @Override
    public QObject defaultConvertToNumber(Runtime runtime) {
        return Val(Double.parseDouble(value));
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Only for things like HashSets.
     * This thing shouldn't be used for
     * actual comparison of objects.
     * Use {@link QObject#equalsObject} instead!
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof QString && ((QString) obj).value.equals(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
