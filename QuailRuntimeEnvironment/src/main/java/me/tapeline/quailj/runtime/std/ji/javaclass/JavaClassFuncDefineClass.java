package me.tapeline.quailj.runtime.std.ji.javaclass;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.ji.javaobject.JavaObject;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JavaClassFuncDefineClass extends QBuiltinFunc {


    public JavaClassFuncDefineClass(Runtime runtime) {
        super(
                "defineClass",
                Arrays.asList(
                        new FuncArgument(
                                "name",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "modifiers",
                                QObject.Val(),
                                new int[] {ModifierConstants.NUM},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "fields",
                                QObject.Val(),
                                new int[] {ModifierConstants.LIST},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "methods",
                                QObject.Val(),
                                new int[] {ModifierConstants.LIST},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                runtime.getMemory(),
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        if (!(args.get("this") instanceof JavaObject))
            runtime.error(new QUnsuitableTypeException(args.get("this"), "JavaObject"));

        return new JavaClass(((JavaObject) args.get("this")).getObject().getClass());
    }

}
