package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.errors.QIterationNotStartedException;
import me.tapeline.quailj.typing.classes.errors.QIterationStopException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QDict extends QObject {

    public static final QDict prototype = new QDict(
            new Table(),
            "Dict",
            QObject.superObject,
            true
    );

    protected HashMap<String, QObject> values;
    protected List<String> iterableKeys = null;
    protected int iterator;

    public QDict(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QDict(Table table, String className, QObject parent, boolean isPrototype, HashMap<String, QObject> values) {
        super(table, className, parent, isPrototype);
        this.values = values;
    }

    public QDict(HashMap<String, QObject> values) {
        this(new Table(), prototype.className, prototype, false);
        this.values = values;
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QDict(new Table(), className, this, false, values);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QDict(new Table(), className, this, true, values);
    }

    @Override
    public QObject copy() {
        QObject copy = new QDict(table, className, parent, isPrototype, new HashMap<>(values));
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    @Override
    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (!other.isDict())
            return super.equalsObject(runtime, other);
        if (other.dictValue().size() != values.size())
            return Val(false);
        if (!other.dictValue().keySet().equals(values.keySet()))
            return Val(false);
        for (String key : values.keySet())
            if (values.get(key).equalsObject(runtime, other.dictValue().get(key)).isFalse())
                return Val(false);
        return Val(true);
    }

    @Override
    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        QBool result = ((QBool) equalsObject(runtime, other));
        result.setValue(!result.getValue());
        return result;
    }

    @Override
    public QObject index(Runtime runtime, QObject index) {
        return nullSafe(values.get(index.toString()));
    }

    @Override
    public QObject indexSet(Runtime runtime, QObject index, QObject value) {
        values.put(index.toString(), value);
        return value;
    }

    @Override
    public QObject iterateStart(Runtime runtime) {
        iterator = 0;
        iterableKeys = new ArrayList<>(values.keySet());
        return this;
    }

    @Override
    public QObject iterateNext(Runtime runtime) throws RuntimeStriker {
        if (iterableKeys == null)
            runtime.error(new QIterationNotStartedException());
        if (iterator == iterableKeys.size()) {
            runtime.error(new QIterationStopException());
            iterableKeys = null;
            iterator = 0;
        }
        return values.get(iterableKeys.get(iterator));
    }

    public HashMap<String, QObject> getValues() {
        return values;
    }

    public void setValues(HashMap<String, QObject> values) {
        this.values = values;
    }

    @Override
    public QObject get(String name) {
        if (values != null && values.containsKey(name))
            return values.get(name);
        return super.get(name);
    }

    @Override
    public void set(Runtime runtime, String name, QObject value) throws RuntimeStriker {
        if (values.containsKey(name))
            values.put(name, value);
        else
            super.set(runtime, name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (values == null)
            return "{}";
        if (values.isEmpty())
            return sb.append("}").toString();
        int i = 0;
        for (Map.Entry<String, QObject> entry : values.entrySet()) {
            if (i + 1 == values.size())
                sb.append("\"").append(entry.getKey()).append("\" = ").append(entry.getValue().toString())
                        .append("}");
            else
                sb.append("\"").append(entry.getKey()).append("\" = ").append(entry.getValue().toString())
                        .append(", ");
            i++;
        }
        return sb.toString();
    }

}
