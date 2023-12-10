package me.tapeline.quailj.runtime.std.event;

import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.event.event.Event;
import me.tapeline.quailj.runtime.std.event.eventmanager.EventManager;
import me.tapeline.quailj.runtime.std.event.eventmanager.EventManagerNotInitializedException;
import me.tapeline.quailj.typing.classes.QFunc;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EventFuncHandleEvents extends QBuiltinFunc {


    public EventFuncHandleEvents(Runtime runtime, Memory closure) {
        super(
                "handleEvents",
                new ArrayList<>(),
                runtime,
                closure,
                false
        );
    }

    public EventFuncHandleEvents(Runtime runtime) {
        this(runtime, runtime.getMemory());
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        if (!(closure.get("defaultManager") instanceof EventManager))
            runtime.error(new QUnsuitableTypeException("EventManager", closure.get("defaultManager")));
        EventManager thisManager = ((EventManager) closure.get("defaultManager"));
        if (thisManager.getEventHandlers() == null || thisManager.getEventQueue() == null)
            runtime.error(new EventManagerNotInitializedException());

        while (!thisManager.getEventQueue().isEmpty()) {
            Event event = thisManager.getEventQueue().remove();
            String eventName = event.get("name").strValue();
            if (!thisManager.getEventHandlers().containsKey(eventName))
                continue;
            for (QFunc handler : thisManager.getEventHandlers().get(eventName))
                handler.call(runtime, Collections.singletonList(event), new HashMap<>());
        }

        return Val();
    }

}
