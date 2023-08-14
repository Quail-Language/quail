package me.tapeline.quailj.runtime.std.basic.common;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableValueException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.*;

public class FuncZip extends QBuiltinFunc {

    public FuncZip(Runtime runtime) {
        super(
                "zip",
                Arrays.asList(
                        new FuncArgument(
                                "left",
                                QObject.Val(),
                                new int[] {ModifierConstants.LIST},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "right",
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
        List<QObject> left = args.get("left").listValue();
        List<QObject> right = args.get("right").listValue();
        List<QObject> result = new ArrayList<>();
        if (left.size() != right.size())
            runtime.error(new QUnsuitableValueException(
                    "Right list is not the same size as left (" + left.size() + ")", args.get("right")
            ));
        for (int i = 0; i < left.size(); i++)
            result.add(Val(new ArrayList<>(Arrays.asList(left.get(i), right.get(i)))));
        return QObject.Val(result);
    }

}
