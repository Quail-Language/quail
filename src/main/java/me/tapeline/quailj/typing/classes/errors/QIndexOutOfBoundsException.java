package me.tapeline.quailj.typing.classes.errors;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class QIndexOutOfBoundsException extends QException {

    public static final String INDEX_FIELD = "index";
    public static final String SIZE_FIELD = "size";

    public static QIndexOutOfBoundsException prototype = new QIndexOutOfBoundsException(
            new Table(
                    Dict.make(
                            new Pair<>(INDEX_FIELD, QObject.Val()),
                            new Pair<>(SIZE_FIELD, QObject.Val())
                    )
            ),
            "IndexOutOfBoundsException",
            QException.prototype,
            true
    );

    public QIndexOutOfBoundsException(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QIndexOutOfBoundsException(Table table, String className,
                                      QObject parent, boolean isPrototype,
                                      String message,
                                      QObject index, QObject size) {
        super(table, className, parent, isPrototype, message);
        set(INDEX_FIELD, index, null);
        set(SIZE_FIELD, size, null);
    }

    public QIndexOutOfBoundsException(QObject index, QObject size) {
        this(
                new Table(),
                prototype.className,
                prototype,
                false,
                "Index out of bounds (size=" + size.numValue() + ", index=" + index.numValue() + ")",
                index,
                size
        );
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        return new QIndexOutOfBoundsException(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QIndexOutOfBoundsException(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new QIndexOutOfBoundsException(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
