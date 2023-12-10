package me.tapeline.quailj.typing.classes.utils;

import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.Table;
import me.tapeline.quailj.typing.classes.QFunc;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class QBuiltinFunc extends QFunc {

    public QBuiltinFunc(String name, List<FuncArgument> args,
                        Runtime boundRuntime,
                        Memory closure, boolean isStatic) {
        super(name, args, null, boundRuntime, isStatic, new ArrayList<>(), closure);
    }

    public abstract QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList)
            throws RuntimeStriker;

    protected QObject run(Runtime runtime, Memory enclosing,
                          Node code, List<QObject> args) throws RuntimeStriker {
        try {
            return action(runtime, enclosing.table.getValues(), args);
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

    @Override
    public QObject derive(Runtime runtime) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to derive from non-prototype value");
        try {
            return getClass().getConstructor(
                    Table.class, String.class, QObject.class, boolean.class,
                    String.class, List.class, Node.class, Runtime.class,
                    boolean.class, List.class, Memory.class
            ).newInstance(
                    new Table(), className, this, false, name, arguments, code,
                    boundRuntime, isStatic, alternatives, closure
            );
        } catch (NoSuchMethodException | InstantiationException
                 | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {
        if (!isPrototype)
            runtime.error("Attempt to inherit from non-prototype value");
        try {
            return getClass().getConstructor(
                    Table.class, String.class, QObject.class, boolean.class,
                    String.class, List.class, Node.class, Runtime.class,
                    boolean.class, List.class, Memory.class
            ).newInstance(
                    new Table(), className, this, false, name, arguments, code,
                    boundRuntime, isStatic, alternatives, closure
            );
        } catch (NoSuchMethodException | InstantiationException
                 | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public QObject copy() {
        try {
            QBuiltinFunc copy = getClass().getConstructor(Runtime.class).newInstance(boundRuntime);
            copy.setInheritableFlag(isInheritable);
            return copy;
        } catch (NoSuchMethodException | InstantiationException
                 | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
