package me.tapeline.quailj.runtime.std.ji.sketchedjavainheritance;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.ji.JIJavaException;
import me.tapeline.quailj.runtime.std.ji.StringifiedSignature;
import me.tapeline.quailj.runtime.std.ji.javaclass.JavaClass;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.burningwave.core.classes.VariableSourceGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SketchedJavaInheritanceConstructor extends QBuiltinFunc {


    public SketchedJavaInheritanceConstructor(Runtime runtime) {
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
                                "extends",
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
        if (!(args.get("this") instanceof SketchedJavaInheritance))
            runtime.error(new QUnsuitableTypeException(args.get("this"),
                    "SketchedJavaInheritance"));
        SketchedJavaInheritance javaInheritance = ((SketchedJavaInheritance) args.get("this"));
        if (!(args.get("extends") instanceof JavaClass))
            runtime.error(new QUnsuitableTypeException(args.get("extends"),
                    "JavaClass"));
        JavaClass javaClass = ((JavaClass) args.get("extends"));

        javaInheritance.setExtending(javaClass.getClazz());

        return javaInheritance;
    }

}
