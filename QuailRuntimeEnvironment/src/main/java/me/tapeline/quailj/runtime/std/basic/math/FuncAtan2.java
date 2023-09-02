package me.tapeline.quailj.runtime.std.basic.math;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FuncAtan2 extends QBuiltinFunc {

    public FuncAtan2(Runtime runtime) {
        super(
                "atan2",
                Arrays.asList(
                        new FuncArgument(
                                "x",
                                QObject.Val(),
                                new int[] {ModifierConstants.NUM},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "y",
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
        return QObject.Val(Math.atan2(args.get("x").numValue(), args.get("y").numValue()));
    }

}
