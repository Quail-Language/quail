package me.tapeline.quailj.runtime.std.basic.math;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FuncMax extends QBuiltinFunc {

    public FuncMax(Runtime runtime) {
        super(
                "max",
                Collections.singletonList(
                        new FuncArgument(
                                "values",
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
        List<QObject> values = args.get("values").listValue();
        int count = values.size();
        double maxValue = values.get(0).numValue();
        for (int i = 0; i < count; i++) {
            QObject val = values.get(i);
            if (!val.isNum()) runtime.error("Cannot find max among non-num values: " + val);
            if (val.numValue() > maxValue) maxValue = val.numValue();
        }
        return QObject.Val(maxValue);
    }

}
