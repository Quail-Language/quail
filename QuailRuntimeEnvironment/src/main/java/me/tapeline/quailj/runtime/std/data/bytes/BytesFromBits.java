package me.tapeline.quailj.runtime.std.data.bytes;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class BytesFromBits extends QBuiltinFunc {

    public BytesFromBits(Runtime runtime) {
        super(
                "fromBits",
                Arrays.asList(
                        new FuncArgument(
                               "bits",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        String bits = args.get("bits").strValue();
        bits = StringUtils.repeat('0', bits.length() % 8) + bits;
        byte[] data = BinaryCodec.fromAscii(bits.toCharArray());
        return new DataLibBytes(data);
    }

}
