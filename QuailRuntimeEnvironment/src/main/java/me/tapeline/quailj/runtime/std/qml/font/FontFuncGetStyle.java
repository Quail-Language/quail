package me.tapeline.quailj.runtime.std.qml.font;

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

public class FontFuncGetStyle extends QBuiltinFunc {


    public FontFuncGetStyle(Runtime runtime) {
        super(
                "getStyle",
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
        if (!(args.get("this") instanceof QMLFont))
            runtime.error(new QUnsuitableTypeException("Font", args.get("this")));
        QMLFont thisFont = ((QMLFont) args.get("this"));
        if (!thisFont.isInitialized())
            runtime.error(new QMLFontNotInitializedException());

        return Val(thisFont.getFont().getStyle());
    }
}
