package me.tapeline.quailj.runtime.std.time;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.runtime.std.data.bytes.DataLibBytes;
import me.tapeline.quailj.runtime.std.data.deque.DataLibDeque;
import me.tapeline.quailj.runtime.std.data.queue.DataLibQueue;
import me.tapeline.quailj.runtime.std.data.set.DataLibSet;
import me.tapeline.quailj.runtime.std.time.datetime.TimeDateTime;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.HashMap;

public class TimeLibrary implements BuiltinLibrary {

    @Override
    public String id() {
        return "lang/time";
    }

    @Override
    public Runtime initializeRuntime() {
        return new Runtime();
    }

    @Override
    public QObject constructLibrary(Runtime runtime) {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("DateTime", TimeDateTime.prototype(runtime));
        contents.put("parseDateTime", new TimeParseDateTime(runtime));

        runtime.getMemory().table.putAll(contents);

        return QObject.Val(contents);
    }

}

