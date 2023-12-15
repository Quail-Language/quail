package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;

public class QIterationStopException extends QException {

    public static final QIterationStopException prototype = new QIterationStopException(
            new Table(),
            "IterationStopException",
            QException.prototype,
            true
    );

    public QIterationStopException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QIterationStopException(Table table, String className, QObject parent, boolean isPrototype, String message) {
        super(table, className, parent, isPrototype, message);
    }

    public QIterationStopException() {
        this(new Table(), prototype.className, prototype, false, "Iteration stopped");
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to derive from non-prototype value", this));
        return new QIterationStopException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QIterationStopException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QIterationStopException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
