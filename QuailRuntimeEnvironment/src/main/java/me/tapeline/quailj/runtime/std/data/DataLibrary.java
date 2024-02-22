package me.tapeline.quailj.runtime.std.data;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.runtime.std.data.bytes.DataLibBytes;
import me.tapeline.quailj.runtime.std.data.deque.DataLibDeque;
import me.tapeline.quailj.runtime.std.data.queue.DataLibQueue;
import me.tapeline.quailj.runtime.std.data.set.DataLibSet;
import me.tapeline.quailj.runtime.std.math.MathFuncGcd;
import me.tapeline.quailj.runtime.std.math.MathFuncLcm;
import me.tapeline.quailj.runtime.std.math.MathFuncProduct;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.HashMap;

public class DataLibrary implements BuiltinLibrary {

    @Override
    public String id() {
        return "lang/data";
    }

    @Override
    public Runtime initializeRuntime() {
        return new Runtime();
    }

    @Override
    public QObject constructLibrary(Runtime runtime) {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("Bytes", DataLibBytes.prototype(runtime));
        contents.put("Queue", DataLibQueue.prototype(runtime));
        contents.put("Deque", DataLibDeque.prototype(runtime));
        contents.put("Set", DataLibSet.prototype(runtime));

        runtime.getMemory().table.putAll(contents);

        return QObject.Val(contents);
    }

}
