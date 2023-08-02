package me.tapeline.quailj.runtime.std.qml.surface;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QException;

public class QMLSurfaceNotInitializedException extends QException {

    public static QMLSurfaceNotInitializedException prototype = new QMLSurfaceNotInitializedException(
            new Table(),
            "SurfaceNotInitializedException",
            QException.prototype,
            true
    );

    public QMLSurfaceNotInitializedException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QMLSurfaceNotInitializedException(Table table, String className,
                                             QObject parent, boolean isPrototype,
                                             String message) {
        super(table, className, parent, isPrototype, message);
    }

    public QMLSurfaceNotInitializedException() {
        this(new Table(), prototype.className, prototype, false,
                "Surface method was called, but surface was not initialized");
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        return new QMLSurfaceNotInitializedException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QMLSurfaceNotInitializedException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QMLSurfaceNotInitializedException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
