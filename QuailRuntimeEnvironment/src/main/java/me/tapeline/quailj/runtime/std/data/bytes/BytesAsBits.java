package me.tapeline.quailj.runtime.std.data.bytes;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.apache.commons.codec.binary.BinaryCodec;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class BytesAsBits extends QBuiltinFunc {

    public BytesAsBits(Runtime runtime) {
        super(
                "asBits",
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
        return Val(BinaryCodec.toAsciiString(DataLibBytes.validate(runtime, args.get("bytes")).data));
    }

}
