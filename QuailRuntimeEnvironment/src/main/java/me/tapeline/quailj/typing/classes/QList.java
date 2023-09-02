package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.errors.QIndexOutOfBoundsException;
import me.tapeline.quailj.typing.classes.errors.QIterationStopException;
import me.tapeline.quailj.utils.QListUtils;
import org.apache.commons.collections.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class QList extends QObject {

    public static QList prototype = new QList(
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
            runtime.error("Attempt to inherit from non-prototype value");
        return new QList(new Table(), className, this, false, values);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QList(new Table(), className, this, true, values);
    }

    @Override
    public QObject copy() {
        QObject copy = new QList(table, className, parent, isPrototype, new ArrayList<>(values));
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    @Override
    public QObject sum(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(ListUtils.union(values, other.listValue()));
        return super.sum(runtime, other);
    }

    @Override
    public QObject multiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.multiply(values, (int) other.numValue()));
        return super.multiply(runtime, other);
    }

    @Override
    public QObject divide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.divide(values, (int) other.numValue()));
        return super.divide(runtime, other);
    }

    @Override
    public QObject intDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.intDivide(values, (int) other.numValue()));
        return super.intDivide(runtime, other);
    }

    @Override
    public QObject shiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.shift(values, (int) -other.numValue()));
        return super.shiftLeft(runtime, other);
    }

    @Override
    public QObject shiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum())
            return Val(QListUtils.shift(values, (int) other.numValue()));
        return super.shiftRight(runtime, other);
    }

    @Override
    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.equals(other.listValue()));
        return super.equalsObject(runtime, other);
    }

    @Override
    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(!values.equals(other.listValue()));
        return super.notEqualsObject(runtime, other);
    }

    @Override
    public QObject greater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() > other.listValue().size());
        return super.greater(runtime, other);
    }

    @Override
    public QObject greaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() >= other.listValue().size());
        return super.greaterEqual(runtime, other);
    }

    @Override
    public QObject less(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() < other.listValue().size());
        return super.less(runtime, other);
    }

    @Override
    public QObject lessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isList())
            return Val(values.size() <= other.listValue().size());
        return super.lessEqual(runtime, other);
    }

    @Override
    public QObject index(Runtime runtime, QObject index) throws RuntimeStriker {
        if (index.isNum()) {
            if (values.size() <= index.numValue())
                runtime.error(new QIndexOutOfBoundsException(index, Val(values.size())));
            else
                return values.get((int) index.numValue());
        }
        return super.index(runtime, index);
    }

    @Override
    public QObject indexSet(Runtime runtime, QObject index, QObject value) throws RuntimeStriker {
        if (index.isNum()) {
            if (values.size() <= index.numValue())
                runtime.error(new QIndexOutOfBoundsException(index, Val(values.size())));
            values.set((int) index.numValue(), value);
            return Val();
        }
        return super.indexSet(runtime, index, value);
    }

    @Override
    public QObject subscriptStartEnd(Runtime runtime, QObject start, QObject end) throws RuntimeStriker {
        if (start.isNum() && end.isNum())
            return Val(values.subList((int) start.numValue(), (int) end.numValue()));
        else if (start.isNum() && end.isNull())
            return Val(values.subList((int) start.numValue(), values.size()));
        else if (start.isNull() && end.isNum())
            return Val(values.subList(0, (int) end.numValue()));
        return super.subscriptStartEnd(runtime, start, end);
    }

    @Override
    public QObject subscriptStartEndStep(Runtime runtime, QObject start, QObject end, QObject step) {
        return Val(QListUtils.subscript(
                values,
                start.isNull()? null : (int) start.numValue(),
                end.isNull()? null : (int) end.numValue(),
                step.isNull()? null : (int) step.numValue()
        ));
    }

    @Override
    public QObject iterateStart(Runtime runtime) {
        iteratorIndex = 0;
        return this;
    }

    @Override
    public QObject iterateNext(Runtime runtime) throws RuntimeStriker {
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
        if (values.size() == 0)
            return sb.append("]").toString();
        for (int i = 0; i < values.size(); i++)
            if (i + 1 == values.size())
                sb.append(values.get(i).toString()).append("]");
            else
                sb.append(values.get(i).toString()).append(", ");
        return sb.toString();
    }

}
