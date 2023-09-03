package me.tapeline.quailj.runtime.std.fs;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FsSetEncoding extends QBuiltinFunc {

    public FsSetEncoding(Runtime runtime) {
        super(
                "setEncoding",
                Collections.singletonList(
                        new FuncArgument(
                                "encoding",
                                QObject.Val(),
                                new int[]{ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                runtime.getMemory(),
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) {
        runtime.getIo().setEncoding(args.get("encoding").strValue());
        return Val();
    }

}
