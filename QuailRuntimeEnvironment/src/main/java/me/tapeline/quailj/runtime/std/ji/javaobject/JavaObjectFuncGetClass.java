package me.tapeline.quailj.runtime.std.ji.javaobject;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.ji.javaclass.JavaClass;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class JavaObjectFuncGetClass extends QBuiltinFunc {


    public JavaObjectFuncGetClass(Runtime runtime) {
        super(
                "getClass",
                Collections.singletonList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[]{},
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
