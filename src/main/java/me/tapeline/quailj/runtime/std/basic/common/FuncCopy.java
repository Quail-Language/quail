package me.tapeline.quailj.runtime.std.basic.common;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FuncCopy extends QBuiltinFunc {

    public FuncCopy(Runtime runtime) {
        super(
                "copy",
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
        return args.get("obj").copy();
    }

}
