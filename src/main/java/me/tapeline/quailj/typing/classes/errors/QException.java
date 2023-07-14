package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QException extends QObject {

    public static final String MESSAGE_FIELD = "message";

    public static QException prototype = new QException(
            new Table(
                    Dict.make(
                            new Pair<>(MESSAGE_FIELD, QObject.Val("Unknown error"))
                    ),
                    Dict.make(
                            new Pair<>(MESSAGE_FIELD, new int[] {ModifierConstants.STR})
                    )
            ),
            "Exception",
            QObject.superObject,
            true
    );

    public QException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QException(Table table, String className, QObject parent, boolean isPrototype, String message) {
        this(table, className, parent, isPrototype);
        set(MESSAGE_FIELD, QObject.Val(message), table.getModifiersFor(MESSAGE_FIELD));
    }

    public QException(String message) {
        this(new Table(), prototype.className, prototype, false, message);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        return new QException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
