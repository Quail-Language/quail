package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;

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
    public QObject derive() throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QList(new Table(), className, this, false, values);
    }

    @Override
    public QObject extendAs(String className) throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QList(new Table(), className, this, true, values);
    }

    @Override
    public QObject copy() {
        QObject copy = new QList(table, className, parent, isPrototype, new ArrayList<>(values));
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public List<QObject> getValues() {
        return values;
    }

    public void setValues(List<QObject> values) {
        this.values = values;
    }

}
