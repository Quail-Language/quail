package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.errors.QIterationStopException;
import me.tapeline.quailj.utils.QStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class QString extends QObject {

    public static QString prototype = new QString(
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
            runtime.error("Attempt to inherit from non-prototype value");
        return new QString(new Table(), className, this, false, value);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
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

    public QObject sum(Runtime runtime, QObject other) {
        return Val(value + other.toString());
    }

    public QObject subtract(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.replaceAll(Pattern.quote(other.toString()), ""));
        return super.subtract(runtime, other);
    }

    @Override
    public QObject multiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(StringUtils.repeat(value, (int) other.numValue()));
        return super.multiply(runtime, other);
    }

    @Override
    public QObject divide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.divide(value, (int) other.numValue()));
        return super.divide(runtime, other);
    }

    @Override
    public QObject intDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.intDivide(value, (int) other.numValue()));
        return super.intDivide(runtime, other);
    }

    @Override
    public QObject shiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.shift(value, (int) -other.numValue()));
        return super.shiftLeft(runtime, other);
    }

    @Override
    public QObject shiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QStringUtils.shift(value, (int) other.numValue()));
        return super.shiftRight(runtime, other);
    }

    @Override
    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.equals(other.toString()));
        return super.equalsObject(runtime, other);
    }

    @Override
    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(!value.equals(other.toString()));
        return super.equalsObject(runtime, other);
    }

    @Override
    public QObject greater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() > other.toString().length());
        return super.greater(runtime, other);
    }

    @Override
    public QObject greaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() >= other.toString().length());
        return super.greaterEqual(runtime, other);
    }

    @Override
    public QObject less(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() < other.toString().length());
        return super.less(runtime, other);
    }

    @Override
    public QObject lessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isStr())
            return Val(value.length() <= other.toString().length());
        return super.lessEqual(runtime, other);
    }

    @Override
    public QObject iterateStart(Runtime runtime) throws RuntimeStriker {
        iteratorIndex = 0;
        return this;
    }

    @Override
    public QObject iterateNext(Runtime runtime) throws RuntimeStriker {
        if (iteratorIndex == value.length())
            runtime.error(new QIterationStopException());
        return Val(String.valueOf(value.charAt(iteratorIndex++)));
    }

    @Override
    public QObject convertToString(Runtime runtime) throws RuntimeStriker {
        return this;
    }

    @Override
    public QObject convertToBool(Runtime runtime) throws RuntimeStriker {
        return Val(Boolean.parseBoolean(value));
    }

    @Override
    public QObject convertToNumber(Runtime runtime) throws RuntimeStriker {
        return Val(Double.parseDouble(value));
    }

    @Override
    public String toString() {
        return value;
    }

}
