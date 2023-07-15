package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.errors.*;

import java.util.*;

public class QObject {

    public static QNull Val() {
        return new QNull();
    }
    public static QNumber Val(double value) {
        return new QNumber(value);
    }
    public static QBool Val(boolean value) {
        return new QBool(value);
    }
    public static QString Val(String value) {
        return new QString(value);
    }
    public static QList Val(List<QObject> value) {
        return new QList(value);
    }
    public static QDict Val(HashMap<String, QObject> value) {
        return new QDict(value);
    }
    public static QObject superObject = new QObject(new Table(), "Object", null, true);
    public static QObject nullSafe(QObject object) {
        return object == null? Val() : object;
    }

    protected Table table = new Table();
    protected String className;
    protected QObject parent;

    protected boolean isPrototype;
    protected boolean isInheritable = true;

    public QObject(Table table, String className, QObject parent, boolean isPrototype) {
        this.table = table;
        this.className = className;
        this.parent = parent;
        this.isPrototype = isPrototype;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public QObject getParent() {
        return parent;
    }

    public void setParent(QObject superClass) {
        this.parent = superClass;
    }

    public boolean isPrototype() {
        return isPrototype;
    }

    public void setPrototypeFlag(boolean prototype) {
        isPrototype = prototype;
    }

    public boolean isInheritable() {
        return isInheritable;
    }

    public void setInheritableFlag(boolean inheritable) {
        isInheritable = inheritable;
    }

    public QObject getSuperClass() {
        return isPrototype? parent : parent.getSuperClass();
    }

    public QObject getPrototype() {
        return isPrototype? this : parent;
    }

    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QObject(new Table(), className, this, false);
    }

    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QObject(new Table(), className, this, true);
    }

    public QObject newObject(Runtime runtime, List<QObject> args, HashMap<String, QObject> kwargs)
            throws RuntimeStriker {
        QObject blank = derive(runtime);
        blank = blank.callFromThis(runtime, "_constructor", args, kwargs);
        return blank;
    }

    public QObject copy() {
        QObject copy = new QObject(table, className, parent, isPrototype);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public QObject clone() {
        QObject copy = copy();
        copy.getTable().clear();
        table.forEach((k, v) -> copy.getTable().put(
                k,
                v.clone(),
                table.getModifiersFor(k)
        ));
        copy.setPrototypeFlag(isPrototype());
        return copy;
    }

    public QObject get(String name) {
        if (name.startsWith("_")) {
            if (name.equals("_className")) return new QString(className);
            else if (name.equals("_superClass")) return getSuperClass();
            else if (name.equals("_objectPrototype")) return getPrototype();
            else if (name.equals("_isPrototype")) return new QBool(isPrototype);
            else if (name.equals("_isInheritable")) return new QBool(isInheritable);
        }
        if (table.containsKey(name))
            return table.get(name);
        else if (parent != null)
            return parent.get(name);
        else
            return new QNull();
    }

    public void set(Runtime runtime, String name, QObject value) throws RuntimeStriker {
        table.put(runtime, name, value);
    }

    public void set(String name, QObject value, int[] modifiers) {
        table.put(name, value, modifiers);
    }

    public void forceSet(String name, QObject value) {
        table.put(name, value, new int[0]);
    }

    public QObject getOverridable(Runtime runtime, String name) throws RuntimeStriker {
        if (table.containsKey("_get"))
            return callFromThis(runtime, "_get", Collections.singletonList(Val(name)), new HashMap<>());
        if (table.containsKey("_get_" + name))
            return callFromThis(runtime, "_get_" + name, new ArrayList<>(), new HashMap<>());
        return get(name);
    }

    public final void setOverridable(Runtime runtime, String name, QObject value) throws RuntimeStriker {
        if (table.containsKey("_set"))
            callFromThis(runtime, "_set", Arrays.asList(QObject.Val(name), value), new HashMap<>());
        else if (table.containsKey("_set_" + name))
            callFromThis(runtime, "_set_" + name, Collections.singletonList(value), new HashMap<>());
        else set(runtime, name, value);
    }

    public final QObject callFromThis(Runtime runtime, QObject func, List<QObject> args,
                                      HashMap<String, QObject> kwargs) throws RuntimeStriker {
        if (!isPrototype())
            args.add(0, this);
        return func.call(runtime, args, kwargs);
    }

    public final QObject callFromThis(Runtime runtime, String func, List<QObject> args,
                                      HashMap<String, QObject> kwargs) throws RuntimeStriker {
        return callFromThis(runtime, get(func), args, kwargs);
    }

    public final boolean instanceOf(QObject parent) {
        // If parent == superObject -> true
        if (parent == superObject) return true;

        // if parent == prototype -> true
        // if same class -> true
        if (parent == getPrototype() || parent == this) return true;

        // if super.instanceof parent -> true
        if (getPrototype() != null && getPrototype().getSuperClass() != null && getPrototype().getSuperClass()
                .instanceOf(parent))
            return true;

        return getSuperClass() != null && getSuperClass().instanceOf(parent);

        // -> false
    }

    public final boolean isNum() {
        return this instanceof QNumber;
    }

    public final boolean isBool() {
        return this instanceof QBool;
    }

    public final boolean isNull() {
        return this instanceof QNull;
    }

    public final boolean isStr() {
        return this instanceof QString;
    }

    public final boolean isList() {
        return this instanceof QList;
    }

    public final boolean isDict() {
        return this instanceof QDict;
    }

    public final boolean isFunc() {
        return this instanceof QFunc;
    }

    public final double numValue() {
        if (this instanceof QNumber)
            return ((QNumber) this).getValue();
        return 0;
    }

    public final boolean boolValue() {
        if (this instanceof QBool)
            return ((QBool) this).getValue();
        return false;
    }

    public final boolean isTrue() {
        if (this instanceof QBool)
            return ((QBool) this).getValue();
        return false;
    }

    public final boolean isFalse() {
        if (this instanceof QBool)
            return !((QBool) this).getValue();
        return false;
    }

    public final String strValue() {
        if (this instanceof QString)
            return ((QString) this).getValue();
        return null;
    }

    public final List<QObject> listValue() {
        if (this instanceof QList)
            return ((QList) this).getValues();
        return null;
    }

    public final HashMap<String, QObject> dictValue() {
        if (this instanceof QDict)
            return ((QDict) this).getValues();
        return null;
    }

    // Actions

    public QObject call(Runtime runtime, List<QObject> args, HashMap<String, QObject> kwargs)
            throws RuntimeStriker {
        if (isPrototype()) {
            return newObject(runtime, args, kwargs);
        }
        if (table.containsKey("_call"))
            return callFromThis(
                    runtime,
                    "_call",
                    Arrays.asList(QObject.Val(args), QObject.Val(kwargs)),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedUnaryOperationException(this, "call"));
        return Val();
    }

    public QObject sum(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_add"))
            return callFromThis(
                    runtime,
                    "_add",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "+", other));
        return Val();
    }

    public QObject subtract(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_sub"))
            return callFromThis(
                    runtime,
                    "_sub",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "-", other));
        return Val();
    }

    public QObject multiply(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_mul"))
            return callFromThis(
                    runtime,
                    "_mul",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "*", other));
        return Val();
    }

    public QObject divide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_div"))
            return callFromThis(
                    runtime,
                    "_div",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "/", other));
        return Val();
    }

    public QObject intDivide(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_intdiv"))
            return callFromThis(
                    runtime,
                    "_intdiv",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "//", other));
        return Val();
    }

    public QObject modulo(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_mod"))
            return callFromThis(
                    runtime,
                    "_mod",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "%", other));
        return Val();
    }

    public QObject power(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_pow"))
            return callFromThis(
                    runtime,
                    "_pow",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "^", other));
        return Val();
    }

    public QObject shiftLeft(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_shl"))
            return callFromThis(
                    runtime,
                    "_shl",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "<<", other));
        return Val();
    }

    public QObject shiftRight(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_shr"))
            return callFromThis(
                    runtime,
                    "_shr",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, ">>", other));
        return Val();
    }

    public QObject equalsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_eq"))
            return callFromThis(
                    runtime,
                    "_eq",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        return Val(table.getValues().equals(other.table.getValues()));
    }

    public QObject notEqualsObject(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_neq"))
            return callFromThis(
                    runtime,
                    "_neq",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        return Val(!table.getValues().equals(other.table.getValues()));
    }

    public QObject greater(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_cmpg"))
            return callFromThis(
                    runtime,
                    "_cmpg",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, ">", other));
        return Val();
    }

    public QObject greaterEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_cmpge"))
            return callFromThis(
                    runtime,
                    "_cmpge",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, ">=", other));
        return Val();
    }

    public QObject less(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_cmpl"))
            return callFromThis(
                    runtime,
                    "_cmpl",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "<", other));
        return Val();
    }

    public QObject lessEqual(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_cmple"))
            return callFromThis(
                    runtime,
                    "_cmple",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "<=", other));
        return Val();
    }

    public QObject not(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_not"))
            return callFromThis(
                    runtime,
                    "_not",
                    new ArrayList<>(),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedUnaryOperationException(this, "!"));
        return Val();
    }

    public QObject negate(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_neg"))
            return callFromThis(
                    runtime,
                    "_neg",
                    new ArrayList<>(),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedUnaryOperationException(this, "-"));
        return Val();
    }

    public QObject convertToString(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_tostring"))
            return callFromThis(
                    runtime,
                    "_tostring",
                    new ArrayList<>(),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedConversionException(this, "string"));
        return Val();
    }

    public QObject convertToBool(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_tobool"))
            return callFromThis(
                    runtime,
                    "_tobool",
                    new ArrayList<>(),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedConversionException(this, "bool"));
        return Val();
    }

    public QObject convertToNumber(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_tonum"))
            return callFromThis(
                    runtime,
                    "_tonum",
                    new ArrayList<>(),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedConversionException(this, "num"));
        return Val();
    }

    public QObject and(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_and"))
            return callFromThis(
                    runtime,
                    "_and",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "and", other));
        return Val();
    }

    public QObject or(Runtime runtime, QObject other) throws RuntimeStriker {
        if (table.containsKey("_or"))
            return callFromThis(
                    runtime,
                    "_or",
                    Collections.singletonList(other),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedOperationException(this, "or", other));
        return Val();
    }

    public QObject index(Runtime runtime, QObject index) throws RuntimeStriker {
        if (table.containsKey("_index"))
            return callFromThis(
                    runtime,
                    "_index",
                    Collections.singletonList(index),
                    new HashMap<>()
            );
        return get(index.toString());
    }

    public QObject indexSet(Runtime runtime, QObject index, QObject value) throws RuntimeStriker {
        if (table.containsKey("_indexSet"))
            return callFromThis(
                    runtime,
                    "_indexSet",
                    Arrays.asList(index, value),
                    new HashMap<>()
            );
        set(runtime, index.toString(), value);
        return Val();
    }

    public QObject subscriptStartEnd(Runtime runtime, QObject start, QObject end) throws RuntimeStriker {
        if (table.containsKey("_subscriptStartEnd"))
            return callFromThis(
                    runtime,
                    "_subscriptStartEnd",
                    Arrays.asList(start, end),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedSubscriptException(this));
        return Val();
    }

    public QObject subscriptStartEndStep(Runtime runtime, QObject start, QObject end, QObject step)
            throws RuntimeStriker {
        if (table.containsKey("_subscriptStartEndStep"))
            return callFromThis(
                    runtime,
                    "_subscriptStartEndStep",
                    Arrays.asList(start, end, step),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedStepSubscriptException(this));
        return Val();
    }

    public QObject iterateStart(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_iterate"))
            return callFromThis(
                    runtime,
                    "_iterate",
                    new ArrayList<>(),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedIterationException(this));
        return Val();
    }

    public QObject iterateNext(Runtime runtime) throws RuntimeStriker {
        if (table.containsKey("_next"))
            return callFromThis(
                    runtime,
                    "_next",
                    new ArrayList<>(),
                    new HashMap<>()
            );
        runtime.error(new QUnsupportedIterationException(this));
        return Val();
    }

    @Override
    public String toString() {
        return "<instance of " + className + ">";
    }

}
