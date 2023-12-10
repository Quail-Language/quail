package me.tapeline.quailj.runtime.std.basic.io;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QIOException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FuncReadFile extends QBuiltinFunc {

    public FuncReadFile(Runtime runtime) {
        super(
                "readFile",
                Collections.singletonList(
                        new FuncArgument("filePath",
                                QObject.Val(),
                                new int[]{ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                runtime.getMemory(),
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        try {
            return Val(runtime.getIo().readFile(args.get("filePath").strValue()));
        } catch (IOException e) {
            runtime.error(new QIOException(e.getMessage()));
        }
        return QObject.Val();
    }

}
