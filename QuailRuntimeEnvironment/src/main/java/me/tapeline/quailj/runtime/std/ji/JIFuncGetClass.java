package me.tapeline.quailj.runtime.std.ji;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.ji.javaclass.JavaClass;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.*;

public class JIFuncGetClass extends QBuiltinFunc {


    public JIFuncGetClass(Runtime runtime, Memory closure) {
        super(
                "getClass",
                Collections.singletonList(
                        new FuncArgument(
                                "name",
                                QObject.Val(),
                                new int[]{ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                closure,
                false
        );
    }

    public JIFuncGetClass(Runtime runtime) {
        this(runtime, runtime.getMemory());
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        try {
            Class<?> clazz = Class.forName(args.get("name").strValue());
            JavaClass javaClass = (JavaClass) JavaClass.prototype(runtime).newObject(
                    runtime, new ArrayList<>(), new HashMap<>());
            javaClass.setClass(clazz);
            return javaClass;
        } catch (ClassNotFoundException e) {
            return Val();
        }
    }

}
