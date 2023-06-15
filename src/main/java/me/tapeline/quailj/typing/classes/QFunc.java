package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.utils.AlternativeCall;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.ArrayList;
import java.util.List;

public class QFunc extends QObject {

    public static QFunc prototype = new QFunc(
            new Table(),
            "Func",
            QObject.superObject,
            true
    );

    protected String name;
    protected List<FuncArgument> args = new ArrayList<>();
    protected Node code;
    protected Runtime boundRuntime = null;
    protected boolean isStatic;
    protected List<AlternativeCall> alternatives = new ArrayList<>();
    protected final Memory closure;

    public QFunc(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QFunc(Table table, String className, QObject parent, boolean isPrototype,
                 String name, List<FuncArgument> args, Node code, Runtime boundRuntime,
                 boolean isStatic, List<AlternativeCall> alternatives, Memory closure) {
        super(table, className, parent, isPrototype);
        this.name = name;
        this.args = args;
        this.code = code;
        this.boundRuntime = boundRuntime;
        this.isStatic = isStatic;
        this.alternatives = alternatives;
        this.closure = closure;
    }

    public QFunc(boolean value) {
        this(new Table(), prototype.className, prototype, false);
        this.value = value;
    }

    @Override
    public QObject derive() throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to derive from non-prototype value");
        return new QFunc(new Table(), className, this, false, value);
    }

    @Override
    public QObject extendAs(String className) throws RuntimeStriker {
        if (!isPrototype)
            Runtime.error("Attempt to inherit from non-prototype value");
        return new QFunc(new Table(), className, this, true, value);
    }

    @Override
    public QObject copy() {
        QObject copy = new QFunc(table, className, parent, isPrototype, value);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

}
