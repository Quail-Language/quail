package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;
import me.tapeline.quailj.typing.classes.errors.QIndexOutOfBoundsException;
import me.tapeline.quailj.typing.classes.errors.QIterationStopException;
import me.tapeline.quailj.utils.QListUtils;
import org.apache.commons.collections.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class QList extends QObject {

    public static final QList prototype = new QList(
            new Table(),
            "List",
            QObject.superObject,
            true
    );

    protected List<QObject> values;
    protected int iteratorIndex;

    public QList(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QList(Table table, String className, QObject parent, boolean isPrototype, List<QObject> values) {
        super(table, className, parent, isPrototype);
        this.values = values;
    }

    public QList(List<QObject> values) {
        this(new Table(), prototype.className, prototype, false);
        this.values = values;
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QList(new Table(), className, this, false, values);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QList(new Table(), className, this, true, values);
    }

    @Override
    public QObject copy() {
        QObject copy = new QList(table, className, parent, isPrototype, new ArrayList<>(values));
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    @Override
    public QObject defaultSum(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(ListUtils.union(values, other.listValue()));
        return super.defaultSum(runtime, other);
    }

    @Override
    public QObject defaultMultiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.multiply(values, (int) other.numValue()));
        return super.defaultMultiply(runtime, other);
    }

    @Override
    public QObject defaultDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.divide(values, (int) other.numValue()));
        return super.defaultDivide(runtime, other);
    }

    @Override
    public QObject defaultIntDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.intDivide(values, (int) other.numValue()));
        return super.defaultIntDivide(runtime, other);
    }

    @Override
    public QObject defaultShiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.shift(values, (int) -other.numValue()));
        return super.defaultShiftLeft(runtime, other);
    }

    @Override
    public QObject defaultShiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.shift(values, (int) other.numValue()));
        return super.defaultShiftRight(runtime, other);
    }

    @Override
    public QObject defaultEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.equals(other.listValue()));
        return super.defaultEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultNotEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(!values.equals(other.listValue()));
        return super.defaultNotEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultGreater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() > other.listValue().size());
        return super.defaultGreater(runtime, other);
    }

    @Override
    public QObject defaultGreaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() >= other.listValue().size());
        return super.defaultGreaterEqual(runtime, other);
    }

    @Override
    public QObject defaultLess(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() < other.listValue().size());
        return super.defaultLess(runtime, other);
    }

    @Override
    public QObject defaultLessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() <= other.listValue().size());
        return super.defaultLessEqual(runtime, other);
    }

    @Override
    public QObject defaultContainsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        for (QObject value : values)
            if (value.equalsObject(runtime, other).boolValue()) return Val(true);
        return Val(false);
    }

    @Override
    public QObject defaultNotContainsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        for (QObject value : values)
            if (value.equalsObject(runtime, other).boolValue()) return Val(false);
        return Val(true);
    }

    @Override
    public QObject defaultIndex(Runtime runtime, QObject index) throws RuntimeStriker {
        if (index.isNum()) {
            if (values.size() <= index.numValue())
                runtime.error(new QIndexOutOfBoundsException(index, Val(values.size())));
            else if (index.numValue() < 0)
                return values.get(values.size() + (int) index.numValue());
            else
                return values.get((int) index.numValue());
        }
        return super.defaultIndex(runtime, index);
    }

    @Override
    public QObject defaultIndexSet(Runtime runtime, QObject index, QObject value) throws RuntimeStriker {
        if (index.isNum()) {
            if (values.size() <= index.numValue())
                runtime.error(new QIndexOutOfBoundsException(index, Val(values.size())));
            else if (index.numValue() < 0)
                values.set(values.size() + (int) index.numValue(), value);
            else
                values.set((int) index.numValue(), value);
            return value;
        }
        return super.defaultIndexSet(runtime, index, value);
    }

    @Override
    public QObject defaultSubscriptStartEnd(Runtime runtime, QObject start, QObject end) throws RuntimeStriker {
        if (start.isNum() && end.isNum())
            return Val(values.subList((int) start.numValue(), (int) end.numValue()));
        else if (start.isNum() && end.isNull())
            return Val(values.subList((int) start.numValue(), values.size()));
        else if (start.isNull() && end.isNum())
            return Val(values.subList(0, (int) end.numValue()));
        return super.defaultSubscriptStartEnd(runtime, start, end);
    }

    @Override
    public QObject defaultSubscriptStartEndStep(Runtime runtime, QObject start, QObject end, QObject step) {
        return Val(QListUtils.subscript(
                values,
                start.isNull()? null : (int) start.numValue(),
                end.isNull()? null : (int) end.numValue(),
                step.isNull()? null : (int) step.numValue()
        ));
    }

    @Override
    public QObject defaultIterateStart(Runtime runtime) {
        iteratorIndex = 0;
        return this;
    }

    @Override
    public QObject defaultIterateNext(Runtime runtime) throws RuntimeStriker {
        if (iteratorIndex == values.size())
            runtime.error(new QIterationStopException());
        return values.get(iteratorIndex++);
    }

    public List<QObject> getValues() {
        return values;
    }

    public void setValues(List<QObject> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (values == null)
            return "[]";
        if (values.isEmpty())
            return sb.append("]").toString();
        for (int i = 0; i < values.size(); i++)
            if (i + 1 == values.size())
                sb.append(values.get(i).toString()).append("]");
            else
                sb.append(values.get(i).toString()).append(", ");
        return sb.toString();
    }

    @Override
    public QObject defaultConvertToString(Runtime runtime) throws RuntimeStriker {
        return Val(toString());
    }

}
