package me.tapeline.quailj.runtime.std.fs;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FsGetEncoding extends QBuiltinFunc {

    public FsGetEncoding(Runtime runtime) {
        super(
                "getEncoding",
                Collections.emptyList(),
                runtime,
                runtime.getMemory(),
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) {
        return Val(runtime.getIo().getEncoding());
    }

}
