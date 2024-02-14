package me.tapeline.quailj.runtime.std.cli;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QDict;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;

import java.util.*;

public class CliRunApp extends QBuiltinFunc {

    public CliRunApp(Runtime runtime) {
        super(
                "runApp",
                Arrays.asList(
                ),
                runtime,
                runtime.getMemory(),
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        boundRuntime.getMemory().set("isRunning", Val(true));
        while (boundRuntime.getMemory().get("isRunning").boolValue()) {
            for (QObject ticking : Objects.requireNonNull(boundRuntime.getMemory().get("ticking").listValue()))
                if (ticking.isFunc()) ticking.call(runtime, new ArrayList<>(), new HashMap<>());
            String command = boundRuntime.getMemory().get("input").call(
                    boundRuntime, Collections.singletonList(boundRuntime.getMemory().get("prefix")), new HashMap<>()
            ).strValue();
            assert command != null;
            command = command.trim();
            String commandName = command.indexOf(' ') == -1?
                    command : command.substring(0, command.indexOf(' '));
            String commandArgs = command.indexOf(' ') == -1?
                    "" : command.substring(command.indexOf(' ') + 1);
            QDict argDict = (QDict) boundRuntime.getMemory().get("parseConsoleArgs").call(
                    boundRuntime,
                    Collections.singletonList(Val(commandArgs)),
                    new HashMap<>()
            );

            QObject executor = boundRuntime.getMemory().get("commands").get(commandName);
            if (!executor.isFunc()) {
                runtime.getIo().println(boundRuntime.getMemory().get("unknownCommandMessage").strValue());
                continue;
            }

            executor.call(runtime, Collections.singletonList(argDict), new HashMap<>());
        }
        return Val();
    }

}
