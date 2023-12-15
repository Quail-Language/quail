package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QUnpackingException extends QException {

    public static final String PRESENT_FIELD = "present";
    public static final String EXPECTED_FIELD = "expected";

    public static final QUnpackingException prototype = new QUnpackingException(
            new Table(
                    Dict.make(
                            new Pair<>(PRESENT_FIELD, QObject.Val()),
                            new Pair<>(EXPECTED_FIELD, QObject.Val())
                    )
            ),
            "UnpackingException",
            QException.prototype,
            true
    );

    public QUnpackingException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QUnpackingException(Table table, String className,
                               QObject parent, boolean isPrototype,
                               String message,
                               int expected, int present) {
        super(table, className, parent, isPrototype, message);
        set(PRESENT_FIELD, Val(present), null);
        set(EXPECTED_FIELD, Val(expected), null);
    }

    public QUnpackingException(int expected, int present) {
        this(
                new Table(),
                prototype.className,
                prototype,
                false,
                "Unpacking failed: collection size = " + present + ", expected size = " + expected,
                expected,
                present
        );
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to derive from non-prototype value", this));
        return new QUnpackingException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QUnpackingException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QUnpackingException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
