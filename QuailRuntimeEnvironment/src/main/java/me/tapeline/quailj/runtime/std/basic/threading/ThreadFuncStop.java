package me.tapeline.quailj.runtime.std.basic.threading;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ThreadFuncStop extends QBuiltinFunc {

    public ThreadFuncStop(Runtime runtime) {
        super(
                "stop",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {},
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
        QThread thisThread = QThread.validate(runtime, args.get("this"));
        thisThread.getThread().interrupt();
        return Val();
    }

}
