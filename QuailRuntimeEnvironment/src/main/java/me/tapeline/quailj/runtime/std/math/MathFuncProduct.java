package me.tapeline.quailj.runtime.std.math;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.*;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathFuncProduct extends QBuiltinFunc {

    public MathFuncProduct(Runtime runtime) {
        super(
                "product",
                Arrays.asList(
                        new FuncArgument(
                               "values",
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
        double v = 1;
        for (QObject object : args.get("values").listValue()) {
            if (!object.isNum())
                runtime.error(new QUnsuitableTypeException("Number", object));
            v *= object.numValue();
        }
        return Val(v);
    }

}
