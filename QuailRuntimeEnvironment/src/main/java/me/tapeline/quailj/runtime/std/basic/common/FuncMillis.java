package me.tapeline.quailj.runtime.std.basic.common;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FuncMillis extends QBuiltinFunc {

    public FuncMillis(Runtime runtime) {
        super(
                "millis",
                Collections.emptyList(),
                runtime,
                runtime.getMemory(),
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) {
        return QObject.Val(System.currentTimeMillis());
    }

}
