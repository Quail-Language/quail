package me.tapeline.quailj.runtime.std.time.datetime;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DateTimeConstructor extends QBuiltinFunc {

    public DateTimeConstructor(Runtime runtime) {
        super(
                "_constructor",
                Arrays.asList(
                        new FuncArgument(
                               "this",
                                QObject.Val(),
                                new int[0],
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "timestamp",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        TimeDateTime thisObject = TimeDateTime.validate(runtime, args.get("this"));
        thisObject.set("timestamp", args.get("timestamp"));
        return thisObject;
    }

}
