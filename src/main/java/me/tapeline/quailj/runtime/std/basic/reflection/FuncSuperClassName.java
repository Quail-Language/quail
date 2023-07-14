package me.tapeline.quailj.runtime.std.basic.reflection;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FuncSuperClassName extends QBuiltinFunc {

    public FuncSuperClassName(Runtime runtime) {
        super(
                "superClassName",
                Collections.singletonList(
                        new FuncArgument(
                                "obj",
                                QObject.Val(),
                                new int[0],
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                runtime.getMemory(),
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) {
        return QObject.Val(args.get("obj").getPrototype().getSuperClass().getClassName());
    }

}
