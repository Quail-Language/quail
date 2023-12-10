package me.tapeline.quailj.runtime.std.qml.surface;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.qml.font.QMLFont;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SurfaceFuncGetFont extends QBuiltinFunc {


    public SurfaceFuncGetFont(Runtime runtime) {
        super(
                "getFont",
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

        return QMLFont.prototype(runtime).newObject(runtime, Arrays.asList(
                Val(thisSurface.graphics.getFont().getName()),
                Val(thisSurface.graphics.getFont().getStyle()),
                Val(thisSurface.graphics.getFont().getSize())
        ), new HashMap<>());
    }
}
