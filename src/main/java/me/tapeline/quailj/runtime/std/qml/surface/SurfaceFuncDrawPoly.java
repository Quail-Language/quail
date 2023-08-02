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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SurfaceFuncDrawPoly extends QBuiltinFunc {


    public SurfaceFuncDrawPoly(Runtime runtime) {
        super(
                "drawPoly",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "points",
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

    private boolean checkPointsValidity(List<QObject> points) {
        if (points == null) return false;
        for (QObject element : points) {
            if (!element.isList()) return false;
            if (element.listValue().size() != 2) return false;
            if (!element.listValue().get(0).isNum() ||
                !element.listValue().get(1).isNum()) return false;
        }
        return true;
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        if (!(args.get("this") instanceof QMLSurface))
            runtime.error(new QUnsuitableTypeException("Surface", args.get("this")));
        QMLSurface thisSurface = ((QMLSurface) args.get("this"));
        if (!thisSurface.isInitialized())
            runtime.error(new QMLSurfaceNotInitializedException());
        if (checkPointsValidity(args.get("points").listValue()))
            runtime.error(new QUnsuitableValueException("Points must be a list of [num x, num y] collections",
                    args.get("points")));

        List<QObject> pointsCollection = args.get("points").listValue();
        int pointCount = pointsCollection.size();
        int[] xPoints = new int[pointCount];
        int[] yPoints = new int[pointCount];

        for (int i = 0; i < pointCount; i++) {
            xPoints[i] = ((int) pointsCollection.get(i).listValue().get(0).numValue());
            yPoints[i] = ((int) pointsCollection.get(i).listValue().get(1).numValue());
        }

        thisSurface.graphics.drawPolygon(xPoints, yPoints, pointCount);

        return Val();
    }
}
