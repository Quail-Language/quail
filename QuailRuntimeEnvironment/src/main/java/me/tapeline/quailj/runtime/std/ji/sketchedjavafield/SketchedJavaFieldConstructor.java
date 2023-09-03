package me.tapeline.quailj.runtime.std.ji.sketchedjavafield;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.ji.JIJavaException;
import me.tapeline.quailj.runtime.std.ji.StringifiedSignature;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.burningwave.core.classes.VariableSourceGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SketchedJavaFieldConstructor extends QBuiltinFunc {


    public SketchedJavaFieldConstructor(Runtime runtime) {
        super(
                "_constructor",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                QObject.Val(),
                                new int[] {},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "signature",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "default",
                                QObject.Val(),
                                new int[] {},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                runtime.getMemory(),
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        if (!(args.get("this") instanceof SketchedJavaField))
            runtime.error(new QUnsuitableTypeException(args.get("this"),
                    "SketchedJavaMethod"));
        SketchedJavaField javaField = ((SketchedJavaField) args.get("this"));

        try {
            StringifiedSignature signature = StringifiedSignature.parse(args.get("signature").strValue());
            VariableSourceGenerator variable = VariableSourceGenerator
                    .create(signature.getType(), signature.getName())
                    .addModifier(signature.getModifier());
            if (args.get("default").isStr())
                variable.setValue(args.get("default").strValue());
            javaField.setVariable(variable);
            return javaField;
        } catch (ClassNotFoundException e) {
            runtime.error(new JIJavaException(e));
            return Val();
        }
    }

}
