package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;

public class QIOException extends QException {

    public static final QIOException prototype = new QIOException(
            new Table(),
            "IOException",
            QException.prototype,
            true
    );

    public QIOException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QIOException(Table table, String className, QObject parent, boolean isPrototype, String message) {
        super(table, className, parent, isPrototype, message);
    }

    public QIOException(String message) {
        this(new Table(), prototype.className, prototype, false, message);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to derive from non-prototype value", this));
        return new QIOException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QIOException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QIOException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
