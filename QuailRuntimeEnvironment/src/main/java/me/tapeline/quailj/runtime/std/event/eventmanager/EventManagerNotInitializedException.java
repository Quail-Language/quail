package me.tapeline.quailj.runtime.std.event.eventmanager;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;
import me.tapeline.quailj.typing.classes.errors.QException;

public class EventManagerNotInitializedException extends QException {

    public static final EventManagerNotInitializedException prototype = new EventManagerNotInitializedException(
            new Table(),
            "EventManagerNotInitializedException",
            QException.prototype,
            true
    );

    public EventManagerNotInitializedException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public EventManagerNotInitializedException(Table table, String className,
                                               QObject parent, boolean isPrototype,
                                               String message) {
        super(table, className, parent, isPrototype, message);
    }

    public EventManagerNotInitializedException() {
        this(new Table(), prototype.className, prototype, false,
                "Event manager was called, but was not initialized");
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to derive from non-prototype value", this));
        return new EventManagerNotInitializedException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new EventManagerNotInitializedException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new EventManagerNotInitializedException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
