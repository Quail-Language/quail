package me.tapeline.quailj.runtime.std.qml.window;

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

public class WindowFuncSetResizable extends QBuiltinFunc {


    public WindowFuncSetResizable(Runtime runtime) {
        super(
                "setResizable",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "isResizable",
                                QObject.Val(),
                                new int[] {ModifierConstants.BOOL},
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
        if (thisWindow.frame == null)
            runtime.error(new QMLWindowNotInitializedException());

        thisWindow.frame.setResizable(args.get("isResizable").boolValue());

        return thisWindow;
    }
}
