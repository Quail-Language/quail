package me.tapeline.quailj.runtime.std.basic.numbers;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.util.*;

public class FuncDec extends QBuiltinFunc {

    public FuncDec(Runtime runtime) {
        super(
                "dec",
                Arrays.asList(
                        new FuncArgument(
                                "n",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "base",
                                QObject.Val(),
                                new int[] {ModifierConstants.NUM},
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
        return QObject.Val(Integer.parseInt(args.get("n").toString(), ((int) args.get("base").numValue())));
    }

}
