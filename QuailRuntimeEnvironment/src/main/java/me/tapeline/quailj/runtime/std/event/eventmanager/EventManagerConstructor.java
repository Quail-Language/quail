package me.tapeline.quailj.runtime.std.event.eventmanager;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class EventManagerConstructor extends QBuiltinFunc {


    public EventManagerConstructor(Runtime runtime) {
        super(
                "_constructor",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "queueCapacity",
                                QObject.Val(32),
                                new int[] {ModifierConstants.NUM},
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
        thisManager.eventQueue = new ArrayBlockingQueue<>(((int) args.get("queueCapacity").numValue()));
        thisManager.eventHandlers = new HashMap<>();
        return thisManager;
    }

}
