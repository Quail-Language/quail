package me.tapeline.quailj.runtime.std.basic.common;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FuncAny extends QBuiltinFunc {

    public FuncAny(Runtime runtime) {
        super(
                "any",
                Collections.singletonList(
                        new FuncArgument(
                                "collection",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) {
        List<QObject> values = args.get("collection").listValue();
        int size = values.size();
        for (int i = 0; i < size; i++)
            if (values.get(i).isTrue())
                return QObject.Val(true);
        return QObject.Val(false);
    }

}
