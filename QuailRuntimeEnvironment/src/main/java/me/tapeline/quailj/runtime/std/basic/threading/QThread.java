package me.tapeline.quailj.runtime.std.basic.threading;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.runtime.std.qml.font.*;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QDerivationException;
import me.tapeline.quailj.typing.classes.errors.QNotInitializedException;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;
import org.jetbrains.annotations.NotNull;

public class QThread extends QObject {

    public static QThread prototype = null;
    public static QThread prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new QThread(
                    new Table(Dict.make(
                            new Pair<>("_constructor", new ThreadConstructor(runtime)),
                            new Pair<>("start", new ThreadFuncStart(runtime)),
                            new Pair<>("stop", new ThreadFuncStop(runtime)),
                            new Pair<>("getResult", new ThreadFuncGetResult(runtime)),
                            new Pair<>("waitForResult", new ThreadFuncWaitForResult(runtime)),
                            new Pair<>("isAlive", new ThreadFuncIsAlive(runtime))
                    )),
                    "Thread",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    protected QuailThread thread;

    public boolean isInitialized() {
        return thread != null;
    }

    public QThread(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QThread(Table table, String className, QObject parent, boolean isPrototype, QuailThread thread) {
        super(table, className, parent, isPrototype);
        this.thread = thread;
    }

    public QThread(QuailThread thread) {
        this(new Table(), prototype.className, prototype, false, thread);
    }

    public static @NotNull QThread validate(Runtime runtime, QObject object) throws RuntimeStriker {
        if (!(object instanceof QThread))
            runtime.error(new QUnsuitableTypeException(prototype.className, object));
        QThread thisObject = (QThread) object;
        if (!thisObject.isInitialized())
            runtime.error(new QNotInitializedException("Thread"));
        return thisObject;
    }

    public QuailThread getThread() {
        return thread;
    }

    public void setThread(QuailThread thread) {
        this.thread = thread;
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QThread(new Table(), className, this, false, thread);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error(new QDerivationException("Attempt to inherit from non-prototype value", this));
        return new QThread(new Table(), className, this, true, thread);
    }

    @Override
    public QObject copy() {
        QObject copy = new QThread(table, className, parent, isPrototype, thread);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

}
