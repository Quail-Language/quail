package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QClarificationException extends QException {

    public static final String VALUE_FIELD = "value";
    public static final String CLARIFIERS_FIELD = "clarifiers";

    public static final QClarificationException prototype = new QClarificationException(
            new Table(
                    Dict.make(
                            new Pair<>(VALUE_FIELD, QObject.Val())
                    )
            ),
            "ClarificationException",
            QException.prototype,
            true
    );

    public QClarificationException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QClarificationException(Table table, String className,
                                   QObject parent, boolean isPrototype,
                                   String message, QObject target) {
        super(table, className, parent, isPrototype, message);
        set(VALUE_FIELD, target, null);
    }

    public QClarificationException(String expected, QObject target) {
        this(new Table(), prototype.className, prototype, false,
                "Attempt to assign wrong data to clarified variable. Expected " +
                        expected + ", but got " + target.getClassName(), target);
    }

    public QClarificationException(QObject target, String message) {
        this(new Table(), prototype.className, prototype, false, message, target);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to derive from non-prototype value", this));
        return new QClarificationException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QClarificationException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QClarificationException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
