package me.tapeline.quailj.typing.classes;

import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.AlternativeCall;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.utils.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QFunc extends QObject {

    public static final QFunc prototype = new QFunc(
            new Table(),
            "Func",
            QObject.superObject,
            true
    );

    protected String name;
    protected List<FuncArgument> arguments = new ArrayList<>();
    protected Node code;
    protected Runtime boundRuntime = null;
    protected boolean isStatic;
    protected List<AlternativeCall> alternatives = new ArrayList<>();
    protected Memory closure;

    public QFunc(Table table, String className, QObject parent, boolean isPrototype) {
        super(table, className, parent, isPrototype);
    }

    public QFunc(Table table, String className, QObject parent, boolean isPrototype,
                 String name, List<FuncArgument> args, Node code, Runtime boundRuntime,
                 boolean isStatic, List<AlternativeCall> alternatives, Memory closure) {
        super(table, className, parent, isPrototype);
        this.name = name;
        this.arguments = args;
        this.code = code;
        this.boundRuntime = boundRuntime;
        this.isStatic = isStatic;
        this.alternatives = alternatives;
        this.closure = closure;
    }

    public QFunc(String name, List<FuncArgument> args, Node code,
                 Runtime boundRuntime, boolean isStatic,
                 List<AlternativeCall> alternatives, Memory closure) {
        this(new Table(), prototype.className, prototype, false);
        this.name = name;
        this.arguments = args;
        this.code = code;
        this.boundRuntime = boundRuntime;
        this.isStatic = isStatic;
        this.alternatives = alternatives;
        this.closure = closure;
    }

    protected Memory mapArguments(Runtime runtime,
                                  List<FuncArgument> arguments,
                                  List<QObject> args,
                                  HashMap<String, QObject> kwargs) throws RuntimeStriker {
        Memory enclosing = new Memory(closure);
        int argumentCount = arguments.size();
        int argsCount = args.size();
        for (int i = 0; i < argumentCount; i++) {
            FuncArgument argument = arguments.get(i);
            if (i >= argsCount) {
                if (!ModifierConstants.couldBeNull(argument.getModifiers()))
                    runtime.error("Argument mapping failed for argument #" + (i + 1) + ".\n" +
                            "Expected not null, but got null");
                enclosing.set(
                        argument.getName(),
                        argument.getDefaultValue(),
                        argument.getModifiers()
                );
            } else if (argument.getType() == LiteralFunction.Argument.POSITIONAL_CONSUMER) {
                enclosing.set(
                        argument.getName(),
                        Val(args.subList(i, argsCount)),
                        argument.getModifiers()
                );
            } else if (argument.getType() == LiteralFunction.Argument.KEYWORD_CONSUMER) {
                enclosing.set(
                        argument.getName(),
                        Val(kwargs),
                        argument.getModifiers()
                );
            } else if (ModifierConstants.matchesOnAssign(argument.getModifiers(), args.get(i))) {
                enclosing.set(
                        argument.getName(),
                        args.get(i),
                        argument.getModifiers()
                );
            } else runtime.error("Argument mapping failed for argument #" + (i + 1) + ".\n" +
                    "Value " + args.get(i) + " is inapplicable for " +
                    TextUtils.modifiersToStringRepr(argument.getModifiers()));
        }
        enclosing.table.putAll(kwargs);
        return enclosing;
    }

    protected QObject run(Runtime runtime, Memory enclosing,
                          Node code, List<QObject> args) throws RuntimeStriker {
        try {
            runtime.run(code, enclosing);
            if (name.equals("_constructor") && args.size() > 0)
                return args.get(0);
        } catch (RuntimeStriker striker) {
            if (striker.getType() == RuntimeStriker.Type.RETURN) {
                return striker.getCarryingReturnValue();
            } else if (striker.getType() == RuntimeStriker.Type.EXCEPTION ||
                    striker.getType() == RuntimeStriker.Type.EXIT) {
                throw striker;
            }
        }
        return QObject.Val();
    }

    public QObject call(Runtime runtime, List<QObject> args, HashMap<String, QObject> kwargs)
            throws RuntimeStriker {
        if (runtime == null)
            runtime = boundRuntime;
        if (runtime == null)
            throw new RuntimeException("No bound runtime! Stopping execution.");
        Memory enclosing;
        if (!alternatives.isEmpty()) for (AlternativeCall alternativeCall : alternatives) {
            try {
                enclosing = mapArguments(runtime, alternativeCall.getArguments(), args, kwargs);
            } catch (RuntimeStriker striker) { continue; }
            return run(runtime, enclosing, alternativeCall.getCode(), args);
        }
        enclosing = mapArguments(runtime, arguments, args, kwargs);
        return run(runtime, enclosing, code, args);
    }

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        return new QFunc(new Table(), className, this, false, name, arguments, code,
                boundRuntime, isStatic, alternatives, closure);
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        return new QFunc(new Table(), className, this, true, name, arguments, code,
                boundRuntime, isStatic, alternatives, closure);
    }

    @Override
    public QObject copy() {
        QObject copy = new QFunc(table, className, parent, isPrototype, name, arguments, code,
                boundRuntime, isStatic, alternatives, closure);
        copy.setInheritableFlag(isInheritable);
        return copy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FuncArgument> getArguments() {
        return arguments;
    }

    public void setArguments(List<FuncArgument> arguments) {
        this.arguments = arguments;
    }

    public Node getCode() {
        return code;
    }

    public void setCode(Node code) {
        this.code = code;
    }

    public Runtime getBoundRuntime() {
        return boundRuntime;
    }

    public void setBoundRuntime(Runtime boundRuntime) {
        this.boundRuntime = boundRuntime;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public List<AlternativeCall> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<AlternativeCall> alternatives) {
        this.alternatives = alternatives;
    }

    public Memory getClosure() {
        return closure;
    }

    public void setClosure(Memory closure) {
        this.closure = closure;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("<function " + name + "(");
        for (int i = 0; i < arguments.size(); i++)
            if (i + 1 == arguments.size())
                sb.append(arguments.get(i).toString()).append(")");
            else
                sb.append(arguments.get(i).toString()).append(", ");
        sb.append(">");
        return sb.toString();
    }

}
