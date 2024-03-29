package me.tapeline.quailj.runtime.std.basic.classes.dict;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.*;

public class DictFuncContainsKey extends QBuiltinFunc {

    public DictFuncContainsKey(Runtime runtime) {
        super(
                "containsKey",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[]{ModifierConstants.DICT},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "key",
                                QObject.Val(),
                                new int[]{ModifierConstants.STR},
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
        return Val(args.get("this").dictValue().containsKey(args.get("key").toString()));
    }

}
