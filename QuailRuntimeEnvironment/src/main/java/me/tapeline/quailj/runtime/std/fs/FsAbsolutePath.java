package me.tapeline.quailj.runtime.std.fs;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FsAbsolutePath extends QBuiltinFunc {

    public FsAbsolutePath(Runtime runtime) {
        super(
                "absolutePath",
                Collections.singletonList(
                        new FuncArgument(
                                "path",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        String path = args.get("path").strValue();
        if (path == null) {
            runtime.error(new QUnsuitableTypeException("String", args.get("path")));
            return Val();
        }
        return Val(runtime.getIo().file(path).getAbsolutePath());
    }

}
