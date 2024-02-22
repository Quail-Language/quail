package me.tapeline.quailj.runtime.std.data.bytes;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BytesAsUnsignedInt extends QBuiltinFunc {

    public BytesAsUnsignedInt(Runtime runtime) {
        super(
                "asUnsignedInt",
                Arrays.asList(
                        new FuncArgument(
                               "bytes",
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
        long value = 0;
        byte[] data = DataLibBytes.validate(runtime, args.get("bytes")).data;
        for (int i = 0; i < Math.min(data.length, 8); i++) {
            value += (long) (Byte.toUnsignedInt(data[i]) * Math.pow(256, i));
        }
        return Val(value);
    }

}
