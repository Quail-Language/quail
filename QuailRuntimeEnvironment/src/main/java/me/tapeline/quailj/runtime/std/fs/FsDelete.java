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
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FsDelete extends QBuiltinFunc {

    public FsDelete(Runtime runtime) {
        super(
                "delete",
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
        File file = new File(path);
        if (file.isDirectory()) {
            try {
                FileUtils.deleteDirectory(file);
                return Val(true);
            } catch (IOException e) {
                return Val(false);
            }
        } else {
            try {
                FileUtils.delete(file);
                return Val(true);
            } catch (IOException e) {
                return Val(false);
            }
        }
    }

}
