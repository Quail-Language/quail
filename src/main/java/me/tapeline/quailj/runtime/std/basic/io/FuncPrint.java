package me.tapeline.quailj.runtime.std.basic.io;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QList;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FuncPrint extends QBuiltinFunc {

    public FuncPrint(Runtime runtime) {
        super(
                "print",
                Collections.singletonList(
                        new FuncArgument("values",
                                QObject.Val(),
                                new int[0],
                                LiteralFunction.Argument.POSITIONAL_CONSUMER
                        )
                ),
                runtime,
                runtime.getMemory(),
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) {
        QList values = (QList) args.get("values");
        for (QObject o : values.getValues())
            System.out.print(o.toString() + " ");
        System.out.println();
        return QObject.Val();
    }

}
