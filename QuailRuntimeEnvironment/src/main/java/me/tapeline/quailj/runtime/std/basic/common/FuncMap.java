package me.tapeline.quailj.runtime.std.basic.common;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QFunc;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.util.*;

public class FuncMap extends QBuiltinFunc {

    public FuncMap(Runtime runtime) {
        super(
                "map",
                Arrays.asList(
                        new FuncArgument(
                                "callback",
                                QObject.Val(),
                                new int[] {ModifierConstants.FUNC},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "collection",
                                QObject.Val(),
                                new int[] {ModifierConstants.LIST},
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
        List<QObject> values = args.get("collection").listValue();
        QFunc callback = ((QFunc) args.get("callback"));
        List<QObject> result = new ArrayList<>();
        int count = values.size();
        for (QObject value : values)
            result.add(callback.call(runtime, Collections.singletonList(value), new HashMap<>()));
        return QObject.Val(result);
    }

}
