package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QDict extends QObject {

    public static QDict prototype = new QDict(
            new Table(),
            "Dict",
            QObject.superObject,
            true
    );

    protected HashMap<String, QObject> values;

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
    public QObject derive() throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QDict(new Table(), className, this, false, values);
    }

    @Override
    public QObject extendAs(String className) throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QDict(new Table(), className, this, true, values);
    }

    @Override
    public QObject copy() {
        QObject copy = new QDict(table, className, parent, isPrototype, new HashMap<>(values));
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public HashMap<String, QObject> getValues() {
        return values;
    }

    public void setValues(HashMap<String, QObject> values) {
        this.values = values;
    }

}
