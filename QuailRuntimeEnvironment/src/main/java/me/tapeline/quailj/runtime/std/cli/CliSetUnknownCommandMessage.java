package me.tapeline.quailj.runtime.std.cli;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CliSetUnknownCommandMessage extends QBuiltinFunc {

    public CliSetUnknownCommandMessage(Runtime runtime) {
        super(
                "setUnknownCommandMessage",
                Arrays.asList(
                        new FuncArgument(
                               "message",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                runtime.getMemory(),
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        boundRuntime.getMemory().set("unknownCommandMessage", args.get("message"));
        return Val();
    }

}
