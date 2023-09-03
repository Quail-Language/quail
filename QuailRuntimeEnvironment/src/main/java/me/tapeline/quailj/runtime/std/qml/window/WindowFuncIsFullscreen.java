package me.tapeline.quailj.runtime.std.qml.window;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WindowFuncIsFullscreen extends QBuiltinFunc {


    public WindowFuncIsFullscreen(Runtime runtime) {
        super(
                "isFullscreen",
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
        if (!(args.get("this") instanceof QMLWindow))
            runtime.error(new QUnsuitableTypeException("Window", args.get("this")));
        QMLWindow thisWindow = ((QMLWindow) args.get("this"));
        if (!thisWindow.isInitialized())
            runtime.error(new QMLWindowNotInitializedException());

        return Val(thisWindow.frame.getExtendedState() == Frame.MAXIMIZED_BOTH &&
                thisWindow.frame.isUndecorated());
    }
}
