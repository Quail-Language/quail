package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QUnsuitableValueException extends QException {

    public static final String VALUE_FIELD = "value";

    public static QUnsuitableValueException prototype = new QUnsuitableValueException(
            new Table(
                    Dict.make(
                            new Pair<>(VALUE_FIELD, QObject.Val())
                    )
            ),
            "UnsuitableTypeException",
            QException.prototype,
            true
    );

    public QUnsuitableValueException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QUnsuitableValueException(Table table, String className,
                                     QObject parent, boolean isPrototype,
                                     String message, QObject target) {
        super(table, className, parent, isPrototype, message);
        set(VALUE_FIELD, target, null);
    }

    public QUnsuitableValueException(String message, QObject target) {
        this(new Table(), prototype.className, prototype, false, message, target);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        return new QUnsuitableValueException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QUnsuitableValueException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QUnsuitableValueException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
