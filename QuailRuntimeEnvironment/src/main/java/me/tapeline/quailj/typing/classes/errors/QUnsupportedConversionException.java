package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QUnsupportedConversionException extends QException {

    public static final String OPERAND_FIELD = "operand";
    public static final String TARGET_TYPE_FIELD = "targetType";

    public static final QUnsupportedConversionException prototype = new QUnsupportedConversionException(
            new Table(
                    Dict.make(
                            new Pair<>(OPERAND_FIELD, QObject.Val()),
                            new Pair<>(TARGET_TYPE_FIELD, QObject.Val())
                    )
            ),
            "UnsupportedConversionException",
            QException.prototype,
            true
    );

    public QUnsupportedConversionException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QUnsupportedConversionException(Table table, String className,
                                           QObject parent, boolean isPrototype,
                                           String message, QObject left,
                                           String operator) {
        super(table, className, parent, isPrototype, message);
        set(OPERAND_FIELD, left, null);
        set(TARGET_TYPE_FIELD, QObject.Val(operator), null);
    }

    public QUnsupportedConversionException(QObject left, String operator) {
        this(
                new Table(),
                prototype.className,
                prototype,
                false,
                "Unsupported type conversion of " + left.getClassName() + " to " + operator,
                left,
                operator
        );
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to derive from non-prototype value", this));
        return new QUnsupportedConversionException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QUnsupportedConversionException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QUnsupportedConversionException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
