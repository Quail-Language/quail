package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QDerivationException extends QException {

    public static final String TARGET_FIELD = "target";

    public static final QDerivationException prototype = new QDerivationException(
            new Table(
                    Dict.make(
                            new Pair<>(TARGET_FIELD, QObject.Val())
                    )
            ),
            "DerivationException",
            QException.prototype,
            true
    );

    public QDerivationException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QDerivationException(Table table, String className,
                                QObject parent, boolean isPrototype,
                                String message, QObject target) {
        super(table, className, parent, isPrototype, message);
        set(TARGET_FIELD, target, null);
    }

    public QDerivationException(String message, QObject target) {
        this(new Table(), prototype.className, prototype, false, message, target);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to derive from non-prototype value", this));
        return new QDerivationException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QDerivationException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QDerivationException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
