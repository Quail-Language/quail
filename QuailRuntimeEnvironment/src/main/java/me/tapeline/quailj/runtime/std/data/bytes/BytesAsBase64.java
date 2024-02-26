package me.tapeline.quailj.runtime.std.data.bytes;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class BytesAsBase64 extends QBuiltinFunc {

    public BytesAsBase64(Runtime runtime) {
        super(
                "asBase64",
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
        return Val(new String(Base64.getEncoder().encode(DataLibBytes.validate(runtime, args.get("bytes")).data)));
    }

}
