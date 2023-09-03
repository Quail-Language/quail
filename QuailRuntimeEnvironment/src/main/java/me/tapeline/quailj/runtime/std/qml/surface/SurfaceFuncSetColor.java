package me.tapeline.quailj.runtime.std.qml.surface;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableValueException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SurfaceFuncSetColor extends QBuiltinFunc {


    public SurfaceFuncSetColor(Runtime runtime) {
        super(
                "setColor",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "color",
                                QObject.Val(),
                                new int[] {ModifierConstants.LIST},
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
        List<QObject> colorComponents = args.get("color").listValue();
        if (colorComponents == null || colorComponents.size() != 3)
            runtime.error(new QUnsuitableValueException("Expected list of [num r, num g, num b]", args.get("color")));
        if (!colorComponents.get(0).isNum() ||
                !colorComponents.get(1).isNum() ||
                !colorComponents.get(2).isNum())
            runtime.error(new QUnsuitableValueException("Expected list of [num r, num g, num b]", args.get("color")));

        thisSurface.getGraphics().setColor(new Color(
                ((int) colorComponents.get(0).numValue()),
                ((int) colorComponents.get(1).numValue()),
                ((int) colorComponents.get(2).numValue())
        ));

        return Val();
    }
}
