package me.tapeline.quailj.runtime.std.qml.font;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FontFuncGetFontMetrics extends QBuiltinFunc {


    public FontFuncGetFontMetrics(Runtime runtime) {
        super(
                "getFontMetrics",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "text",
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
        if (!(args.get("this") instanceof QMLFont))
            runtime.error(new QUnsuitableTypeException("Font", args.get("this")));
        QMLFont thisFont = ((QMLFont) args.get("this"));
        if (!thisFont.isInitialized())
            runtime.error(new QMLFontNotInitializedException());

        Rectangle2D rect = thisFont.getFont().getStringBounds(
                args.get("text").strValue(),
                new FontRenderContext(new AffineTransform(), true, true)
        );

        return Val(new ArrayList<>(Arrays.asList(Val(rect.getWidth()), Val(rect.getHeight()))));
    }
}
