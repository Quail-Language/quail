package me.tapeline.quailj.runtime.std.basic.classes.list;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ListFuncRemoveElementAt extends QBuiltinFunc {

    public ListFuncRemoveElementAt(Runtime runtime) {
        super(
                "removeElementAt",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {ModifierConstants.LIST},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "index",
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
        List<QObject> list = args.get("this").listValue();
        int index = ((int) args.get("index").numValue());
        return list.remove(index);
    }

}
