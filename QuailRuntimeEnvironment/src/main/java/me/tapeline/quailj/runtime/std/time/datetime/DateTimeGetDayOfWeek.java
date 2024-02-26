package me.tapeline.quailj.runtime.std.time.datetime;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DateTimeGetDayOfWeek extends QBuiltinFunc {

    public DateTimeGetDayOfWeek(Runtime runtime) {
        super(
                "getDayOfWeek",
                Arrays.asList(
                        new FuncArgument(
                               "this",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        TimeDateTime thisObject = TimeDateTime.validate(runtime, args.get("this"));
        Instant instant = Instant.ofEpochMilli(thisObject.getTimestamp());
        int day = instant.atZone(ZoneId.of(thisObject.getTimezone())).getDayOfWeek().getValue();
        return Val(day);
    }

}
