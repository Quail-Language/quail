package me.tapeline.quailj.runtime.std.ji.sketchedjavaclass;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.ji.StringifiedSignature;
import me.tapeline.quailj.runtime.std.ji.sketchedjavaconstructor.SketchedJavaConstructor;
import me.tapeline.quailj.runtime.std.ji.sketchedjavafield.SketchedJavaField;
import me.tapeline.quailj.runtime.std.ji.sketchedjavainheritance.SketchedJavaInheritance;
import me.tapeline.quailj.runtime.std.ji.sketchedjavamethod.SketchedJavaMethod;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.burningwave.core.classes.ClassSourceGenerator;
import org.burningwave.core.classes.TypeDeclarationSourceGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SketchedJavaClassConstructor extends QBuiltinFunc {


    public SketchedJavaClassConstructor(Runtime runtime) {
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
                                "contents",
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
        if (!(args.get("this") instanceof SketchedJavaClass))
            runtime.error(new QUnsuitableTypeException(args.get("this"),
                    "SketchedJavaClass"));
        SketchedJavaClass javaClass = ((SketchedJavaClass) args.get("this"));

        StringifiedSignature signature = StringifiedSignature.parseJavaClass(args.get("signature").strValue());

        ClassSourceGenerator generator = ClassSourceGenerator.create(
                TypeDeclarationSourceGenerator.create(signature.getName()));

        for (QObject content : args.get("contents").listValue()) {
            if (content instanceof SketchedJavaConstructor) {
                generator.addConstructor(((SketchedJavaConstructor) content).getFunction());
            } else if (content instanceof SketchedJavaField) {
                generator.addField(((SketchedJavaField) content).getVariable());
            } else if (content instanceof SketchedJavaMethod) {
                generator.addMethod(((SketchedJavaMethod) content).getFunction());
            } else if (content instanceof SketchedJavaInheritance) {
                generator.addConcretizedType(((SketchedJavaInheritance) content).getExtending());
                //generator.expands();
            }
        }

        generator.addModifier(signature.getModifier());
        javaClass.setClass(generator);
        return javaClass;
    }

}
