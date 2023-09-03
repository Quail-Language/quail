package me.tapeline.quailj.runtime.std.qml.font;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QException;

public class QMLFontNotInitializedException extends QException {

    public static final QMLFontNotInitializedException prototype = new QMLFontNotInitializedException(
            new Table(),
            "FontNotInitializedException",
            QException.prototype,
            true
    );

    public QMLFontNotInitializedException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QMLFontNotInitializedException(Table table, String className,
                                          QObject parent, boolean isPrototype,
                                          String message) {
        super(table, className, parent, isPrototype, message);
    }

    public QMLFontNotInitializedException() {
        this(new Table(), prototype.className, prototype, false,
                "Font method was called, but font was not initialized");
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        return new QMLFontNotInitializedException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QMLFontNotInitializedException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QMLFontNotInitializedException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
