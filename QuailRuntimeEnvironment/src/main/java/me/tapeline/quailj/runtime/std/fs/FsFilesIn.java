package me.tapeline.quailj.runtime.std.fs;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FsFilesIn extends QBuiltinFunc {

    public FsFilesIn(Runtime runtime) {
        super(
                "filesIn",
                Collections.singletonList(
                        new FuncArgument(
                                "directory",
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
        String path = args.get("directory").strValue();
        if (path == null) {
            runtime.error(new QUnsuitableTypeException("String", args.get("directory")));
            return Val();
        }
        File file = runtime.getIo().file(path);
        return Val(
                FileUtils.listFiles(file, null, false).stream()
                        .map(f -> Val(f.getName()))
                        .collect(Collectors.toList())
        );
    }

}
