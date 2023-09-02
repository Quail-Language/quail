package me.tapeline.quailj.runtime.std.basic.classes.list;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QFunc;
import me.tapeline.quailj.typing.classes.QList;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ListFuncSorted extends QBuiltinFunc {

    public ListFuncSorted(Runtime runtime) {
        super(
                "sorted",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {ModifierConstants.LIST},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "comparator",
                                QObject.Val(),
                                new int[] {ModifierConstants.FUNC},
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
        QList thisList = ((QList) args.get("this"));
        List<QObject> copy = thisList.getValues().subList(0, thisList.getValues().size());
        QFunc comparator = ((QFunc) args.get("comparator"));
        copy.sort((o1, o2) -> {
            try {
                QObject result = comparator.call(runtime, Arrays.asList(o1, o2), new HashMap<>());
                return ((int) result.numValue());
            } catch (RuntimeStriker striker) {
                return 0;
            }
        });
        return Val(copy);
    }

}
