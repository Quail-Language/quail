package me.tapeline.quailj.runtime.std.event.event;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

public class Event extends QObject {

    public static Event prototype = null;
    public static Event prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new Event(
                    new Table(Dict.make(
                            new Pair<>("_constructor", new EventConstructor(runtime)),
                            new Pair<>("name", Val("event"))
                    )),
                    "Event",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    public Event(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new Event(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new Event(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new Event(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
