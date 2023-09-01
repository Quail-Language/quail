package me.tapeline.quailj.runtime.std.ji.sketchedjavapackage;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.ji.JIJavaException;
import me.tapeline.quailj.runtime.std.ji.StringifiedSignature;
import me.tapeline.quailj.runtime.std.ji.sketchedjavaclass.SketchedJavaClass;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.burningwave.core.classes.UnitSourceGenerator;
import org.burningwave.core.classes.VariableSourceGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SketchedJavaPackageConstructor extends QBuiltinFunc {


    public SketchedJavaPackageConstructor(Runtime runtime) {
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
                                "name",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "classes",
                                QObject.Val(),
                                new int[] {},
                                LiteralFunction.Argument.POSITIONAL_CONSUMER
                        )
                ),
                runtime,
                runtime.getMemory(),
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        if (!(args.get("this") instanceof SketchedJavaPackage))
            runtime.error(new QUnsuitableTypeException(args.get("this"),
                    "SketchedJavaPackage"));
        SketchedJavaPackage javaPackage = ((SketchedJavaPackage) args.get("this"));

        UnitSourceGenerator generator = UnitSourceGenerator.create(args.get("name").strValue());

        for (QObject clazz : args.get("classes").listValue()) {
            if (clazz instanceof SketchedJavaClass) {
                generator.addClass(((SketchedJavaClass) clazz).getClazz());
            } else {
                runtime.error(new QUnsuitableTypeException("SketchedJavaClass", clazz));
            }
        }

        javaPackage.setUnit(generator);
        return javaPackage;
    }

}
