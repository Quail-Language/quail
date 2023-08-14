package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QUnsupportedUnaryOperationException extends QException {

    public static final String OPERAND_FIELD = "operand";
    public static final String OPERATOR_FIELD = "operator";

    public static QUnsupportedUnaryOperationException prototype = new QUnsupportedUnaryOperationException(
            new Table(
                    Dict.make(
                            new Pair<>(OPERAND_FIELD, QObject.Val()),
                            new Pair<>(OPERATOR_FIELD, QObject.Val())
                    )
            ),
            "UnsupportedUnaryOperationException",
            QException.prototype,
            true
    );

    public QUnsupportedUnaryOperationException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QUnsupportedUnaryOperationException(Table table, String className,
                                               QObject parent, boolean isPrototype,
                                               String message, QObject left,
                                               String operator) {
        super(table, className, parent, isPrototype, message);
        set(OPERAND_FIELD, left, null);
        set(OPERATOR_FIELD, QObject.Val(operator), null);
    }

    public QUnsupportedUnaryOperationException(QObject left, String operator) {
        this(
                new Table(),
                prototype.className,
                prototype,
                false,
                "Unsupported operation " + operator + " on " + left.getClassName(),
                left,
                operator
        );
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        return new QUnsupportedUnaryOperationException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QUnsupportedUnaryOperationException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QUnsupportedUnaryOperationException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
