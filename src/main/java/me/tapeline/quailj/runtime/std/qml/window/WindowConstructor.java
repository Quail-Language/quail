package me.tapeline.quailj.runtime.std.qml.window;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WindowConstructor extends QBuiltinFunc {


    public WindowConstructor(Runtime runtime) {
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
                                "title",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
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
        if (!(args.get("this") instanceof QMLWindow))
            runtime.error(new QUnsuitableTypeException("Window", args.get("this")));
        QMLWindow thisWindow = ((QMLWindow) args.get("this"));
        thisWindow.frame = new Frame(args.get("title").strValue());
        thisWindow.mouseHandler = new QMLWindow.MouseHandler();
        thisWindow.windowHandler = new QMLWindow.WindowHandler();
        thisWindow.frame.addMouseListener(thisWindow.mouseHandler);
        thisWindow.frame.addWindowListener(thisWindow.windowHandler);
        return thisWindow;
    }
}
