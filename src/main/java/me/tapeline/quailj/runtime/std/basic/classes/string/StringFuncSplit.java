package me.tapeline.quailj.runtime.std.basic.classes.string;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class StringFuncSplit extends QBuiltinFunc {

    public StringFuncSplit(Runtime runtime) {
        super(
                "find",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[]{ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "delimiter",
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
        String thisString = args.get("this").strValue();
        String delimiter = args.get("delimiter").strValue();
        List<QObject> result = new ArrayList<>();
        String[] parts = thisString.split(Pattern.quote(delimiter));
        for (String part : parts)
            result.add(Val(part));
        return Val(result);
    }

}
