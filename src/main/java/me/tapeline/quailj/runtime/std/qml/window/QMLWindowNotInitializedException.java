package me.tapeline.quailj.runtime.std.qml.window;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QException;

public class QMLWindowNotInitializedException extends QException {

    public static QMLWindowNotInitializedException prototype = new QMLWindowNotInitializedException(
            new Table(),
            "WindowNotInitializedException",
            QException.prototype,
            true
    );

    public QMLWindowNotInitializedException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QMLWindowNotInitializedException(Table table, String className,
                                            QObject parent, boolean isPrototype,
                                            String message) {
        super(table, className, parent, isPrototype, message);
    }

    public QMLWindowNotInitializedException() {
        this(new Table(), prototype.className, prototype, false,
                "Window method was called, but window was not initialized");
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        return new QMLWindowNotInitializedException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QMLWindowNotInitializedException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QMLWindowNotInitializedException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
