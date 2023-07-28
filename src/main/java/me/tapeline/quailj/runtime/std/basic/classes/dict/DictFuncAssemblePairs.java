package me.tapeline.quailj.runtime.std.basic.classes.dict;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DictFuncAssemblePairs extends QBuiltinFunc {

    public DictFuncAssemblePairs(Runtime runtime) {
        super(
                "assemblePairs",
                Collections.singletonList(
                        new FuncArgument(
                                "pairs",
                                QObject.Val(),
                                new int[]{ModifierConstants.LIST},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                runtime.getMemory(),
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        List<QObject> pairs = args.get("pairs").listValue();
        HashMap<String, QObject> dict = new HashMap<>();
        for (QObject pair : pairs) {
            if (!pair.isList())
                runtime.error(new QUnsuitableTypeException("List", pair));
            List<QObject> pairContents = pair.listValue();
            if (pairContents.size() != 2)
                runtime.error(new QUnsuitableValueException("Expected size of 2", pair));
            dict.put(pairContents.get(0).toString(), pairContents.get(1));
        }
        return Val(dict);
    }

}
