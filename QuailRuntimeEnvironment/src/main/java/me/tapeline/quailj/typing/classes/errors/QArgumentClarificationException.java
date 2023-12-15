package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QArgumentClarificationException extends QException {

    public static final String VALUE_FIELD = "value";
    public static final String CLARIFIERS_FIELD = "clarifiers";
    public static final String NAME_FIELD = "name";

    public static final QArgumentClarificationException prototype = new QArgumentClarificationException(
            new Table(
                    Dict.make(
                            new Pair<>(VALUE_FIELD, QObject.Val()),
                            new Pair<>(CLARIFIERS_FIELD, QObject.Val()),
                            new Pair<>(NAME_FIELD, QObject.Val())
                    )
            ),
            "ArgumentClarificationException",
            QException.prototype,
            true
    );

    public QArgumentClarificationException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QArgumentClarificationException(Table table, String className,
                                           QObject parent, boolean isPrototype,
                                           String message, String argName, QObject target) {
        super(table, className, parent, isPrototype, message);
        set(VALUE_FIELD, target, null);
    }

    public QArgumentClarificationException(String clarifiers, String argName, QObject target) {
        this(new Table(), prototype.className, prototype, false,
                "Attempt to pass wrong data to clarified argument " + argName + ". Expected " +
                        clarifiers + ", but got " + target.getClassName(), argName, target);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to derive from non-prototype value", this));
        return new QArgumentClarificationException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QArgumentClarificationException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QArgumentClarificationException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
