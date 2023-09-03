package me.tapeline.quailj.runtime.std.qml.surface;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SurfaceFuncGetHeight extends QBuiltinFunc {


    public SurfaceFuncGetHeight(Runtime runtime) {
        super(
                "getHeight",
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
        if (!(args.get("this") instanceof QMLSurface))
            runtime.error(new QUnsuitableTypeException("Surface", args.get("this")));
        QMLSurface thisSurface = ((QMLSurface) args.get("this"));
        if (!thisSurface.isInitialized())
            runtime.error(new QMLSurfaceNotInitializedException());

        return Val(thisSurface.getImage().getHeight());
    }
}
