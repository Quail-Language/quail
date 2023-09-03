package me.tapeline.quailj.runtime.std.qml.window;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.qml.surface.QMLSurface;
import me.tapeline.quailj.runtime.std.qml.surface.QMLSurfaceNotInitializedException;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WindowFuncDraw extends QBuiltinFunc {


    public WindowFuncDraw(Runtime runtime) {
        super(
                "draw",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "surface",
                                QObject.Val(),
                                new int[] {},
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
        if (!(args.get("surface") instanceof QMLSurface))
            runtime.error(new QUnsuitableTypeException("Surface", args.get("surface")));
        QMLSurface surface = ((QMLSurface) args.get("surface"));
        if (!surface.isInitialized())
            runtime.error(new QMLSurfaceNotInitializedException());

        //thisWindow.frame.getGraphics().drawImage(surface.getImage(), 0, 0, null);
        thisWindow.canvas.setScheduledImage(surface.getImage());

        return thisWindow;
    }
}
