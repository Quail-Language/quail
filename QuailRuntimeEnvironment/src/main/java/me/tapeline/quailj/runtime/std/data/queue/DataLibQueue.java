package me.tapeline.quailj.runtime.std.data.queue;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QIterationStopException;
import me.tapeline.quailj.typing.classes.errors.QNotInitializedException;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.Initializable;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class DataLibQueue extends QObject implements Initializable {

    public static DataLibQueue prototype = null;
    public static DataLibQueue prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new DataLibQueue(
                    new Table(Dict.make(
                            new Pair<>("add", new QueueAdd(runtime)),
                            new Pair<>("pop", new QueuePop(runtime)),
                            new Pair<>("size", new QueueSize(runtime)),
                            new Pair<>("clear", new QueueClear(runtime)),
                            new Pair<>("peek", new QueuePeek(runtime))
                    )),
                    "Queue",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    protected Queue<QObject> queue = new ArrayDeque<>();
    protected Iterator<QObject> iterator = null;

    public Queue<QObject> getQueue() {
        return queue;
    }

    public static DataLibQueue validate(Runtime runtime, QObject object) throws RuntimeStriker {
        if (!(object instanceof DataLibQueue))
            runtime.error(new QUnsuitableTypeException(prototype.className, object));
        DataLibQueue thisObject = (DataLibQueue) object;
        if (!thisObject.isInitialized())
            runtime.error(new QNotInitializedException("Queue"));
        return thisObject;
    }

    public DataLibQueue(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public DataLibQueue(Table table, String className, QObject parent, boolean isPrototype, Queue<QObject> queue) {
        this(table, className, parent, isPrototype);
        this.queue = queue;
    }

    public DataLibQueue(Queue<QObject> queue) {
        this(new Table(), prototype.className, prototype, false);
        this.queue = queue;
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new DataLibQueue(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new DataLibQueue(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new DataLibQueue(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    @Override
    public boolean isInitialized() {
        return queue != null;
    }

    @Override
    public QObject defaultContainsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        return Val(queue.contains(other));
    }

    @Override
    public QObject defaultNotContainsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        return Val(!queue.contains(other));
    }

    @Override
    public QObject defaultIterateStart(Runtime runtime) throws RuntimeStriker {
        iterator = queue.iterator();
        return this;
    }

    @Override
    public QObject defaultIterateNext(Runtime runtime) throws RuntimeStriker {
        if (!iterator.hasNext())
            runtime.error(new QIterationStopException());
        return iterator.next();
    }

}