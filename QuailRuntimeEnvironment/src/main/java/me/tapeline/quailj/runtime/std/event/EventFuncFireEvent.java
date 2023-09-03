package me.tapeline.quailj.runtime.std.event;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.event.event.Event;
import me.tapeline.quailj.runtime.std.event.eventmanager.EventManager;
import me.tapeline.quailj.runtime.std.event.eventmanager.EventManagerNotInitializedException;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EventFuncFireEvent extends QBuiltinFunc {


    public EventFuncFireEvent(Runtime runtime, Memory closure) {
        super(
                "fireEvent",
                Collections.singletonList(
                        new FuncArgument(
                                "event",
                                QObject.Val(),
                                new int[]{},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                closure,
                false
        );
    }

    public EventFuncFireEvent(Runtime runtime) {
        this(runtime, runtime.getMemory());
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        if (!(closure.get("defaultManager") instanceof EventManager))
            runtime.error(new QUnsuitableTypeException("EventManager", closure.get("defaultManager")));
        EventManager thisManager = ((EventManager) closure.get("defaultManager"));
        if (thisManager.getEventHandlers() == null || thisManager.getEventQueue() == null)
            runtime.error(new EventManagerNotInitializedException());
        Event event = ((Event) args.get("event"));

        thisManager.getEventQueue().add(event);

        return Val();
    }

}
