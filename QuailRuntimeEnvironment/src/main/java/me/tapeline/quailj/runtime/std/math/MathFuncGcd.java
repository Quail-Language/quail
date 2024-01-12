package me.tapeline.quailj.runtime.std.math;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;
import org.apache.commons.math3.util.ArithmeticUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MathFuncGcd extends QBuiltinFunc {

    public MathFuncGcd(Runtime runtime) {
        super(
                "gcd",
                Arrays.asList(
                        new FuncArgument(
                               "a",
                                QObject.Val(),
                                new int[] {ModifierConstants.NUM},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                               "b",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        return Val(ArithmeticUtils.gcd((long) args.get("a").numValue(), (long) args.get("b").numValue()));
    }

}
