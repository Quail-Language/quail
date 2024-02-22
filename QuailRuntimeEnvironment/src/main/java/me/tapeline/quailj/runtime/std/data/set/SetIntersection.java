package me.tapeline.quailj.runtime.std.data.set;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;

import java.util.*;

public class SetIntersection extends QBuiltinFunc {

    public SetIntersection(Runtime runtime) {
        super(
                "intersection",
                Arrays.asList(
                        new FuncArgument(
                               "a",
                                QObject.Val(),
                                new int[0],
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                               "b",
                                QObject.Val(),
                                new int[0],
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
        DataLibSet a = DataLibSet.validate(runtime, args.get("a"));
        DataLibSet b = DataLibSet.validate(runtime, args.get("b"));
        Set<QObject> result = new HashSet<>(a.getSet());
        result.retainAll(b.getSet());
        return new DataLibSet(result);
    }

}
