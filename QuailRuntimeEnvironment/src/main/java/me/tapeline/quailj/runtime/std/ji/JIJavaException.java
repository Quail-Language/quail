package me.tapeline.quailj.runtime.std.ji;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;
import me.tapeline.quailj.typing.classes.errors.QException;

public class JIJavaException extends QException {

    public static final JIJavaException prototype = new JIJavaException(
            new Table(),
            "JavaException",
            QException.prototype,
            true
    );

    private Exception exception;

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public JIJavaException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public JIJavaException(Table table, String className, QObject parent, boolean isPrototype, Exception exception) {
        super(table, className, parent, isPrototype, "A java exception occurred " + exception.toString());
        this.exception = exception;
    }

    public JIJavaException(Exception exception) {
        this(new Table(), prototype.className, prototype, false, exception);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to derive from non-prototype value", this));
        return new JIJavaException(new Table(), className, this, false, exception);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new JIJavaException(new Table(), className, this, true, exception);
    }

    @Override
    public QObject copy() {
        QObject copy = new JIJavaException(table, className, parent, isPrototype, exception);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
