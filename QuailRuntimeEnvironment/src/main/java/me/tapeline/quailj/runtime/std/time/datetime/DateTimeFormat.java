package me.tapeline.quailj.runtime.std.time.datetime;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimeFormat extends QBuiltinFunc {

    public DateTimeFormat(Runtime runtime) {
        super(
                "format",
                Arrays.asList(
                        new FuncArgument(
                               "this",
                                QObject.Val(),
                                new int[0],
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                               "template",
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
        TimeDateTime thisObject = TimeDateTime.validate(runtime, args.get("this"));
        SimpleDateFormat format = new SimpleDateFormat(args.get("template").strValue());
        format.setTimeZone(TimeZone.getTimeZone(thisObject.getTimezone()));
        Date date = new Date(thisObject.getTimestamp());
        return Val(format.format(date));
    }

}
