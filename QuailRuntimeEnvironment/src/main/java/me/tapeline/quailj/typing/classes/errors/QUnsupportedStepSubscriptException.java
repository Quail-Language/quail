package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QUnsupportedStepSubscriptException extends QException {

    public static final String OPERAND_FIELD = "operand";

    public static final QUnsupportedStepSubscriptException prototype = new QUnsupportedStepSubscriptException(
            new Table(
                    Dict.make(
                            new Pair<>(OPERAND_FIELD, QObject.Val())
                    )
            ),
            "UnsupportedStepSubscriptException",
            QException.prototype,
            true
    );

    public QUnsupportedStepSubscriptException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QUnsupportedStepSubscriptException(Table table, String className,
                                              QObject parent, boolean isPrototype,
                                              String message, QObject left) {
        super(table, className, parent, isPrototype, message);
        set(OPERAND_FIELD, left, null);
    }

    public QUnsupportedStepSubscriptException(QObject left) {
        this(
                new Table(),
                prototype.className,
                prototype,
                false,
                "Unsupported subscript with step for " + left.getClassName(),
                left
        );
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        return new QUnsupportedStepSubscriptException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QUnsupportedStepSubscriptException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QUnsupportedStepSubscriptException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
