package me.tapeline.quailj.runtime.std.cli;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.runtime.std.cli.decorators.CliCommand;
import me.tapeline.quailj.runtime.std.cli.decorators.CliTick;
import me.tapeline.quailj.runtime.std.math.MathFuncGcd;
import me.tapeline.quailj.runtime.std.math.MathFuncLcm;
import me.tapeline.quailj.runtime.std.math.MathFuncProduct;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CliLibrary implements BuiltinLibrary {

    @Override
    public String id() {
        return "lang/cli";
    }

    @Override
    public Runtime initializeRuntime() {
        return new Runtime();
    }

    @Override
    public QObject constructLibrary(Runtime runtime) {
        HashMap<String, QObject> contents = new HashMap<>();
        runtime.getMemory().set("prefix", QObject.Val("> "));
        runtime.getMemory().set("commands", QObject.Val(new HashMap<>()));
        runtime.getMemory().set("ticking", QObject.Val(new ArrayList<>()));
        runtime.getMemory().set("isRunning", QObject.Val(false));
        runtime.getMemory().set("unknownCommandMessage", QObject.Val("Unknown command"));

        contents.put("getConsoleArgString", new CliGetConsoleArgString(runtime));
        contents.put("parseConsoleArgs", new CliParseConsoleArgs(runtime));
        contents.put("runApp", new CliRunApp(runtime));
        contents.put("stopApp", new CliStopApp(runtime));
        contents.put("setPrefix", new CliSetPrefix(runtime));
        contents.put("setUnknownCommandMessage", new CliSetUnknownCommandMessage(runtime));

        contents.put("command", new CliCommand(runtime));
        contents.put("tick", new CliTick(runtime));

        runtime.getMemory().table.putAll(contents);

        return QObject.Val(contents);
    }

}
