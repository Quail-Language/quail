package me.tapeline.quailj.runtime.std.basic.math;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FuncTan extends QBuiltinFunc {

    public FuncTan(Runtime runtime) {
        super(
                "tan",
                Collections.singletonList(
                        new FuncArgument(
                                "n",
                                QObject.Val(),
                                new int[] {ModifierConstants.NUM},
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
        return QObject.Val(Math.tan(args.get("n").numValue()));
    }

}
