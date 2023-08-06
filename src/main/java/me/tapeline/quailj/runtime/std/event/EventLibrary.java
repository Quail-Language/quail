package me.tapeline.quailj.runtime.std.event;

import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;
import me.tapeline.quailj.runtime.std.event.event.Event;
import me.tapeline.quailj.runtime.std.event.eventmanager.EventManager;
import me.tapeline.quailj.runtime.std.event.eventmanager.EventManagerFuncFireEvent;
import me.tapeline.quailj.runtime.std.qml.window.QMLWindow;
import me.tapeline.quailj.typing.classes.QObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EventLibrary implements BuiltinLibrary {

    @Override
    public String id() {
        return "lang/event";
    }

    @Override
    public QObject constructLibrary(Runtime runtime) throws RuntimeStriker {
        HashMap<String, QObject> contents = new HashMap<>();
        contents.put("Event", Event.prototype(runtime));
        contents.put("EventManager", EventManager.prototype(runtime));

        Memory scope = new Memory(runtime.getMemory());
        QObject defaultManager = EventManager.prototype.newObject(runtime, new ArrayList<>(), new HashMap<>());
        contents.put("defaultManager", defaultManager);
        scope.set("defaultManager", defaultManager);

        contents.put("addHandler", new EventFuncAddHandler(runtime, scope));
        contents.put("removeHandler", new EventFuncRemoveHandler(runtime, scope));
        contents.put("fireEvent", new EventFuncFireEvent(runtime, scope));
        contents.put("handleEvents", new EventFuncHandleEvents(runtime, scope));

        return QObject.Val(contents);
    }

}
