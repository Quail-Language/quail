package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;

public class QIterationNotStartedException extends QException {

    public static final QIterationNotStartedException prototype = new QIterationNotStartedException(
            new Table(),
            "IterationNotStartedException",
            QException.prototype,
            true
    );

    public QIterationNotStartedException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QIterationNotStartedException(Table table, String className, QObject parent, boolean isPrototype, String message) {
        super(table, className, parent, isPrototype, message);
    }

    public QIterationNotStartedException() {
        this(new Table(), prototype.className, prototype, false, "Iteration not started, but next element requested");
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        return new QIterationNotStartedException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QIterationNotStartedException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QIterationNotStartedException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
