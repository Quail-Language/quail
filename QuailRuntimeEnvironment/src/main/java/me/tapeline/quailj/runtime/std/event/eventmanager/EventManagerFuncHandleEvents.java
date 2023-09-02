package me.tapeline.quailj.runtime.std.event.eventmanager;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.event.event.Event;
import me.tapeline.quailj.typing.classes.QFunc;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class EventManagerFuncHandleEvents extends QBuiltinFunc {


    public EventManagerFuncHandleEvents(Runtime runtime) {
        super(
                "handleEvents",
                Collections.singletonList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[]{},
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
        if (!(args.get("this") instanceof EventManager))
            runtime.error(new QUnsuitableTypeException("EventManager", args.get("this")));
        EventManager thisManager = ((EventManager) args.get("this"));
        if (thisManager.eventHandlers == null || thisManager.eventQueue == null)
            runtime.error(new EventManagerNotInitializedException());

        while (thisManager.eventQueue.size() > 0) {
            Event event = thisManager.eventQueue.remove();
            String eventName = event.get("name").strValue();
            if (!thisManager.eventHandlers.containsKey(eventName))
                continue;
            for (QFunc handler : thisManager.eventHandlers.get(eventName))
                handler.call(runtime, Collections.singletonList(event), new HashMap<>());
        }

        return Val();
    }

}
