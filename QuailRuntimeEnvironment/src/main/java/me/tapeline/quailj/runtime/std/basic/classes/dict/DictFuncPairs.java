package me.tapeline.quailj.runtime.std.basic.classes.dict;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QDict;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.*;

public class DictFuncPairs extends QBuiltinFunc {

    public DictFuncPairs(Runtime runtime) {
        super(
                "pairs",
                Collections.singletonList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[]{ModifierConstants.DICT},
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
        QDict thisDict = ((QDict) args.get("this"));
        List<QObject> result = new ArrayList<>();
        for (String key : thisDict.getValues().keySet())
            result.add(Val(new ArrayList<>(Arrays.asList(Val(key), thisDict.getValues().get(key)))));
        return Val(result);
    }

}
