package me.tapeline.quailj.runtime.std.basic.threading;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.qml.font.QMLFont;
import me.tapeline.quailj.typing.classes.QFunc;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ThreadConstructor extends QBuiltinFunc {

    public ThreadConstructor(Runtime runtime) {
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
                                "f",
                                QObject.Val(),
                                new int[] {ModifierConstants.FUNC},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "args",
                                QObject.Val(),
                                new int[] {ModifierConstants.LIST, ModifierConstants.NULL},
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
        if (!(args.get("this") instanceof QThread))
            runtime.error(new QUnsuitableTypeException("Thread", args.get("this")));
        QThread thisThread = ((QThread) args.get("this"));
        thisThread.setThread(new QuailThread(runtime, ((QFunc) args.get("f")), args.get("args").listValue()));
        return thisThread;
    }
}
