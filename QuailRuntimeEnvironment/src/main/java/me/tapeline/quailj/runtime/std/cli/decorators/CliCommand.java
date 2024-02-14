package me.tapeline.quailj.runtime.std.cli.decorators;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CliCommand extends QBuiltinFunc {

    public CliCommand(Runtime runtime) {
        super(
                "command",
                Arrays.asList(
                        new FuncArgument(
                               "f",
                                QObject.Val(),
                                new int[0],
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                               "commandName",
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
        boundRuntime.getMemory().get("commands").indexSet(boundRuntime, args.get("commandName"), args.get("f"));
        return Val();
    }

}
