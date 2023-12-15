package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;

public class QNotInitializedException extends QException {
    // TODO replace all *NotInitializedException with this
    public static final QNotInitializedException prototype = new QNotInitializedException(
            new Table(),
            "NotInitializedException",
            QException.prototype,
            true
    );

    public QNotInitializedException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QNotInitializedException(Table table, String className,
                                    QObject parent, boolean isPrototype,
                                    String message) {
        super(table, className, parent, isPrototype, message);
    }

    public QNotInitializedException(String className) {
        this(new Table(), prototype.className, prototype, false,
                className + " method was called, but object was not initialized");
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to derive from non-prototype value", this));
        return new QNotInitializedException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QNotInitializedException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QNotInitializedException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
