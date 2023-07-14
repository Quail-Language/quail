package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QUnsupportedOperationException extends QException {

    public static final String LEFT_FIELD = "left";
    public static final String RIGHT_FIELD = "right";
    public static final String OPERATOR_FIELD = "operator";

    public static QUnsupportedOperationException prototype = new QUnsupportedOperationException(
            new Table(
                    Dict.make(
                            new Pair<>(LEFT_FIELD, QObject.Val()),
                            new Pair<>(RIGHT_FIELD, QObject.Val()),
                            new Pair<>(OPERATOR_FIELD, QObject.Val())
                    )
            ),
            "UnsupportedOperationException",
            QException.prototype,
            true
    );

    public QUnsupportedOperationException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QUnsupportedOperationException(Table table, String className,
                                          QObject parent, boolean isPrototype,
                                          String message, QObject left,
                                          QObject right, String operator) {
        super(table, className, parent, isPrototype, message);
        set(LEFT_FIELD, left, null);
        set(RIGHT_FIELD, right, null);
        set(OPERATOR_FIELD, QObject.Val(operator), null);
    }

    public QUnsupportedOperationException(QObject left, String operator, QObject right) {
        this(
                new Table(),
                prototype.className,
                prototype,
                false,
                "Unsupported operation " + operator + " between " +
                        left.getClassName() + " and " + right.getClassName(),
                left,
                right,
                operator
        );
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        return new QUnsupportedOperationException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QUnsupportedOperationException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QUnsupportedOperationException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
