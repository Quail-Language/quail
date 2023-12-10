package me.tapeline.quailj.runtime.std.basic.classes.list;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ListFuncReversed extends QBuiltinFunc {

    public ListFuncReversed(Runtime runtime) {
        super(
                "reversed",
                Collections.singletonList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[]{ModifierConstants.LIST},
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
        Collections.reverse(args.get("this").listValue());
        return Val();
    }

}
