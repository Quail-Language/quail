package me.tapeline.quailj.runtime.std.data.set;

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
import me.tapeline.quailj.runtime.std.data.set.SetAdd;
import me.tapeline.quailj.runtime.std.data.set.SetSize;
import me.tapeline.quailj.runtime.std.data.set.SetIntersection;
import me.tapeline.quailj.runtime.std.data.set.SetClear;
import me.tapeline.quailj.runtime.std.data.set.SetUnion;
import me.tapeline.quailj.runtime.std.data.set.SetRemove;

import java.util.*;

public class DataLibSet extends QObject implements Initializable {

    public static DataLibSet prototype = null;
    public static DataLibSet prototype(Runtime runtime) {
        if (prototype == null)
            prototype = new DataLibSet(
                    new Table(Dict.make(
                            new Pair<>("_constructor", new SetConstructor(runtime)),
                            new Pair<>("add", new SetAdd(runtime)),
                            new Pair<>("size", new SetSize(runtime)),
                            new Pair<>("intersection", new SetIntersection(runtime)),
                            new Pair<>("clear", new SetClear(runtime)),
                            new Pair<>("union", new SetUnion(runtime)),
                            new Pair<>("remove", new SetRemove(runtime))
                    )),
                    "Set",
                    QObject.superObject,
                    true
            );
        return prototype;
    }

    protected Set<QObject> set = new HashSet<>();
    protected Iterator<QObject> iterator = null;

    public Set<QObject> getSet() {
        return set;
    }

    public static DataLibSet validate(Runtime runtime, QObject object) throws RuntimeStriker {
        if (!(object instanceof DataLibSet))
            runtime.error(new QUnsuitableTypeException(prototype.className, object));
        DataLibSet thisObject = (DataLibSet) object;
        if (!thisObject.isInitialized())
            runtime.error(new QNotInitializedException("Set"));
        return thisObject;
    }

    public DataLibSet(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public DataLibSet(Table table, String className, QObject parent, boolean isPrototype, Set<QObject> set) {
        this(table, className, parent, isPrototype);
        this.set = set;
    }

    public DataLibSet(Set<QObject> set) {
        this(new Table(), prototype.className, prototype, false);
        this.set = set;
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new DataLibSet(new Table(), className, this, false);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new DataLibSet(new Table(), className, this, true);
    }

    @Override
    public QObject copy() {
        QObject copy = new DataLibSet(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    @Override
    public boolean isInitialized() {
        return set != null;
    }

    @Override
    public QObject defaultEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof DataLibSet)
            return Val(((DataLibSet) other).set.equals(set));
        return super.defaultEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultNotEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other instanceof DataLibSet)
            return Val(!((DataLibSet) other).set.equals(set));
        return super.defaultNotEqualsObject(runtime, other);
    }

    @Override
    public QObject defaultContainsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum() || other.isStr() || other.isBool())
            return Val(set.contains(other));
        return super.defaultContainsObject(runtime, other);
    }

    @Override
    public QObject defaultNotContainsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isNum() || other.isStr() || other.isBool())
            return Val(!set.contains(other));
        return super.defaultNotContainsObject(runtime, other);
    }

    @Override
    public QObject defaultIterateStart(Runtime runtime) throws RuntimeStriker {
        iterator = set.iterator();
        return this;
    }

    @Override
    public QObject defaultIterateNext(Runtime runtime) throws RuntimeStriker {
        if (!iterator.hasNext())
            runtime.error(new QIterationStopException());
        return iterator.next();
    }

}