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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FuncWriteFile extends QBuiltinFunc {

    public FuncWriteFile(Runtime runtime) {
        super(
                "writeFile",
                Arrays.asList(
                        new FuncArgument("filePath",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument("contents",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
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
            runtime.getIo().writeFile(args.get("filePath").strValue(), args.get("contents").strValue());
        } catch (IOException e) {
            runtime.error(new QIOException(e.getMessage()));
        }
        return QObject.Val();
    }

}
