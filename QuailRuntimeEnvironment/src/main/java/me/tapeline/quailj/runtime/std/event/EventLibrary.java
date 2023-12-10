package me.tapeline.quailj.runtime.std.event;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.runtime.std.event.event.Event;
import me.tapeline.quailj.runtime.std.event.eventmanager.EventManager;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EventLibrary implements BuiltinLibrary {

    @Override
    public String id() {
        return "lang/event";
    }

    @Override
    public Runtime initializeRuntime() {
        return new Runtime();
    }

    @Override
    public QObject constructLibrary(Runtime runtime) throws RuntimeStriker {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("Event", Event.prototype(runtime));
        contents.put("EventManager", EventManager.prototype(runtime));

        QObject defaultManager = EventManager.prototype.newObject(runtime,
                new ArrayList<>(), new HashMap<>());
        contents.put("defaultManager", defaultManager);
        contents.put("addHandler", new EventFuncAddHandler(runtime,
                runtime.getMemory()));
        contents.put("removeHandler", new EventFuncRemoveHandler(runtime,
                runtime.getMemory()));
        contents.put("fireEvent", new EventFuncFireEvent(runtime,
                runtime.getMemory()));
        contents.put("handleEvents", new EventFuncHandleEvents(runtime,
                runtime.getMemory()));

        runtime.getMemory().table.putAll(contents);

        return QObject.Val(contents);
    }

}
