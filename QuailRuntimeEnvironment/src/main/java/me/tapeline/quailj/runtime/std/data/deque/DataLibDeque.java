package me.tapeline.quailj.runtime.std.data.deque;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.runtime.std.data.bytes.DataLibBytes;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QIterationStopException;
import me.tapeline.quailj.typing.classes.errors.QNotInitializedException;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.Initializable;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;
import me.tapeline.quailj.runtime.std.data.deque.DequeSize;
import me.tapeline.quailj.runtime.std.data.deque.DequePopBack;
import me.tapeline.quailj.runtime.std.data.deque.DequeClear;
import me.tapeline.quailj.runtime.std.data.deque.DequeAddBack;
import me.tapeline.quailj.runtime.std.data.deque.DequeAddFront;
import me.tapeline.quailj.runtime.std.data.deque.DequePopFront;

import java.util.*;

public class DataLibDeque extends QObject implements Initializable {

    public static DataLibDeque prototype = null;
    public static DataLibDeque prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new DataLibDeque(
                    new Table(Dict.make(
                            new Pair<>("size", new DequeSize(runtime)),
                            new Pair<>("popBack", new DequePopBack(runtime)),
                            new Pair<>("clear", new DequeClear(runtime)),
                            new Pair<>("addBack", new DequeAddBack(runtime)),
                            new Pair<>("addFront", new DequeAddFront(runtime)),
                            new Pair<>("popFront", new DequePopFront(runtime))
                    )),
                    "Deque",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    protected Deque<QObject> deque = new ArrayDeque<>();
    protected Iterator<QObject> iterator = null;

    public Deque<QObject> getDeque() {
        return deque;
    }

    public static DataLibDeque validate(Runtime runtime, QObject object) throws RuntimeStriker {
        if (!(object instanceof DataLibDeque))
            runtime.error(new QUnsuitableTypeException(prototype.className, object));
        DataLibDeque thisObject = (DataLibDeque) object;
        if (!thisObject.isInitialized())
            runtime.error(new QNotInitializedException("Deque"));
        return thisObject;
    }

    public DataLibDeque(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public DataLibDeque(Table table, String className, QObject parent, boolean isPrototype, Deque<QObject> deque) {
        this(table, className, parent, isPrototype);
        this.deque = deque;
    }

    public DataLibDeque(Deque<QObject> deque) {
        this(new Table(), prototype.className, prototype, false);
        this.deque = deque;
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new DataLibDeque(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new DataLibDeque(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new DataLibDeque(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    @Override
    public boolean isInitialized() {
        return deque != null;
    }

    @Override
    public QObject defaultContainsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        return Val(deque.contains(other));
    }

    @Override
    public QObject defaultNotContainsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        return Val(!deque.contains(other));
    }

    @Override
    public QObject defaultIterateStart(Runtime runtime) throws RuntimeStriker {
        iterator = deque.iterator();
        return this;
    }

    @Override
    public QObject defaultIterateNext(Runtime runtime) throws RuntimeStriker {
        if (!iterator.hasNext())
            runtime.error(new QIterationStopException());
        return iterator.next();
    }
}