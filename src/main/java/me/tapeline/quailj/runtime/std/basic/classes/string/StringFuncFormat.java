package me.tapeline.quailj.runtime.std.basic.classes.string;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class StringFuncFormat extends QBuiltinFunc {

    public StringFuncFormat(Runtime runtime) {
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
                                "values",
                                QObject.Val(),
                                new int[] {
                                        ModifierConstants.LIST,
                                        ModifierConstants.DICT
                                },
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                runtime.getMemory(),
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        String result = args.get("this").strValue();
        if (args.get("values").isList()) {
            List<QObject> values = args.get("values").listValue();
            for (int i = 0; i < values.size(); i++)
                result = result.replaceAll(
                        Pattern.quote("%" + i),
                        values.get(i).convertToString(runtime).toString()
                );
        } else if (args.get("values").isDict()) {
            HashMap<String, QObject> values = args.get("values").dictValue();
            for (String key : values.keySet())
                result = result.replaceAll(
                        Pattern.quote("%" + key),
                        values.get(key).convertToString(runtime).toString()
                );
        }
        return Val(result);
    }

}
