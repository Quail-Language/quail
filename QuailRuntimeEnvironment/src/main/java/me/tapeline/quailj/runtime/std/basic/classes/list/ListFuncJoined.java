package me.tapeline.quailj.runtime.std.basic.classes.list;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ListFuncJoined extends QBuiltinFunc {

    public ListFuncJoined(Runtime runtime) {
        super(
                "joined",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {ModifierConstants.LIST},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "separator",
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
        String separator = args.get("separator").strValue();
        List<QObject> values = args.get("this").listValue();
        return Val(StringUtils.join(values, separator));
    }

}
