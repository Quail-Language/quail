package me.tapeline.quailj.runtime.std.ji.javaobject;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class JavaObjectFuncPack extends QBuiltinFunc {


    public JavaObjectFuncPack(Runtime runtime) {
        super(
                "pack",
                Collections.singletonList(
                        new FuncArgument(
                                "obj",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) {
        return new JavaObject(args.get("obj"));
    }

}
