package me.tapeline.quailj.runtime.std.event;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.event.eventmanager.EventManager;
import me.tapeline.quailj.runtime.std.event.eventmanager.EventManagerNotInitializedException;
import me.tapeline.quailj.typing.classes.QFunc;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EventFuncRemoveHandler extends QBuiltinFunc {


    public EventFuncRemoveHandler(Runtime runtime, Memory closure) {
        super(
                "removeHandler",
                Arrays.asList(
                        new FuncArgument(
                                "event",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "handler",
                                QObject.Val(),
                                new int[] {ModifierConstants.FUNC},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                closure,
                false
        );
    }

    public EventFuncRemoveHandler(Runtime runtime) {
        this(runtime, runtime.getMemory());
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        if (!(closure.get("defaultManager") instanceof EventManager))
            runtime.error(new QUnsuitableTypeException("EventManager", closure.get("defaultManager")));
        EventManager thisManager = ((EventManager) closure.get("defaultManager"));
        if (thisManager.getEventHandlers() == null || thisManager.getEventQueue() == null)
            runtime.error(new EventManagerNotInitializedException());
        QFunc handler = ((QFunc) args.get("handler"));
        String event = args.get("event").toString();

        if (thisManager.getEventHandlers().containsKey(event))
            thisManager.getEventHandlers().get(event).remove(handler);

        return Val();
    }

}
