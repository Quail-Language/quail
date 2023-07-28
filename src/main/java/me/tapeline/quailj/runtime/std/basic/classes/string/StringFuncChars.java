package me.tapeline.quailj.runtime.std.basic.classes.string;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class StringFuncChars extends QBuiltinFunc {

    public StringFuncChars(Runtime runtime) {
        super(
                "chars",
                Collections.singletonList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
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
        String thisString = args.get("this").strValue();
        List<QObject> result = new ArrayList<>();
        for (int i = 0; i < thisString.length(); i++)
            result.add(Val(thisString.charAt(i)));
        return Val(result);
    }

}
