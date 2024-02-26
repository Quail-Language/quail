package me.tapeline.quailj.runtime.std.data.bytes;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BytesNor extends QBuiltinFunc {

    public BytesNor(Runtime runtime) {
        super(
                "nor",
                Arrays.asList(
                        new FuncArgument(
                               "a",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR, ModifierConstants.OBJ},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                               "b",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR, ModifierConstants.OBJ},
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
        byte[] a = BytesUtils.getByteArray(runtime, args.get("a"));
        byte[] b = BytesUtils.getByteArray(runtime, args.get("b"));
        if (a == null) {
            runtime.error(new QUnsuitableTypeException("Bytes | String", args.get("a")));
            return Val();
        }
        if (b == null) {
            runtime.error(new QUnsuitableTypeException("Bytes | String", args.get("b")));
            return Val();
        }
        byte[] result = new byte[Math.min(a.length, b.length)];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) ~(a[i] | b[i]);
        }
        return new DataLibBytes(result);
    }

}
