package me.tapeline.quailj.runtime.std.basic.classes.string;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.QString;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StringFuncCentered extends QBuiltinFunc {

    public StringFuncCentered(Runtime runtime) {
        super(
                "centered",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[]{ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "size",
                                QObject.Val(),
                                new int[]{ModifierConstants.NUM},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                runtime.getMemory(),
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) {
        QString thisString = ((QString) args.get("this"));
        int rowSize = ((int) args.get("size").numValue());
        double margin = (double) (thisString.getValue().length() - rowSize) / 2;
        return Val(
                StringUtils.repeat(' ', (int) Math.ceil(margin)) +
                thisString.getValue() +
                StringUtils.repeat(' ', (int) Math.floor(margin))
        );
    }

}
