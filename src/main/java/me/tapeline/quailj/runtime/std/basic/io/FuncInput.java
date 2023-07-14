package me.tapeline.quailj.runtime.std.basic.io;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QException;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FuncInput extends QBuiltinFunc {

    public FuncInput(Runtime runtime) {
        super(
                "input",
                Collections.singletonList(
                        new FuncArgument(
                                "prompt",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList)
            throws RuntimeStriker {
        if (!args.get("prompt").isNull())
            runtime.getIo().print(args.get("prompt").toString());
        try {
            return QObject.Val(runtime.getIo().readLine());
        } catch (IOException e) {
            runtime.error(new QException(e.toString()));
            return Val();
        }
    }

}
