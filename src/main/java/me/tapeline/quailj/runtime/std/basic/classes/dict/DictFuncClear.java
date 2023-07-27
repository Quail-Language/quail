package me.tapeline.quailj.runtime.std.basic.classes.dict;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QDict;
import me.tapeline.quailj.typing.classes.QList;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DictFuncClear extends QBuiltinFunc {

    public DictFuncClear(Runtime runtime) {
        super(
                "clear",
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
        thisDict.getValues().clear();
        return Val();
    }

}
