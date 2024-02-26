package me.tapeline.quailj.runtime.std.data.bytes;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.errors.QIndexOutOfBoundsException;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BytesBitAt extends QBuiltinFunc {

    public BytesBitAt(Runtime runtime) {
        super(
                "bitAt",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {ModifierConstants.OBJ},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                               "pos",
                                QObject.Val(),
                                new int[] {ModifierConstants.NUM},
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
        byte[] data = DataLibBytes.validate(runtime, args.get("bytes")).data;
        if (data.length <= args.get("pos").numValue()) {
            runtime.error(new QIndexOutOfBoundsException(args.get("pos"), Val(data.length)));
            return Val();
        }
        int pos = ((int) args.get("pos").numValue());
        byte segment = data[pos / 8];
        int posInSegment = pos % 8;
        int mask = 1 << posInSegment;
        return Val((segment & mask) > 0);
    }

}
