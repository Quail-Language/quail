package me.tapeline.quailj.runtime.std.ji;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.ji.javaclass.JavaClass;
import me.tapeline.quailj.runtime.std.ji.sketchedjavapackage.SketchedJavaPackage;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.JavaMemoryCompiler;
import org.burningwave.core.classes.UnitSourceGenerator;
import org.burningwave.core.concurrent.QueuedTaskExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.burningwave.core.assembler.StaticComponentContainer.ClassLoaders;

public class JIFuncDeployPackage extends QBuiltinFunc {


    public JIFuncDeployPackage(Runtime runtime, Memory closure) {
        super(
                "deployPackage",
                Arrays.asList(
                        new FuncArgument(
                                "package",
                                QObject.Val(),
                                new int[] {},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "classStorage",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "classes",
                                QObject.Val(),
                                new int[] {ModifierConstants.LIST},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                closure,
                false
        );
    }

    public JIFuncDeployPackage(Runtime runtime) {
        this(runtime, runtime.getMemory());
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        if (!(args.get("package") instanceof SketchedJavaPackage))
            runtime.error(new QUnsuitableTypeException("SketchedJavaPackage", args.get("package")));
        UnitSourceGenerator unit = ((SketchedJavaPackage) args.get("package")).getUnit();

        ComponentContainer componentContainer = ComponentContainer.getInstance();
        JavaMemoryCompiler javaMemoryCompiler = componentContainer.getJavaMemoryCompiler();
        QueuedTaskExecutor.ProducerTask<JavaMemoryCompiler.Compilation.Result> compilationTask =
                javaMemoryCompiler.compile(
                        JavaMemoryCompiler.Compilation.Config.forUnitSourceGenerator(unit)
                        .storeCompiledClassesTo(
                                runtime.getIo().file(args.get("classStorage").strValue()).getAbsolutePath()
                        )
                );

        JavaMemoryCompiler.Compilation.Result compilationResult = compilationTask.join();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassLoaders.addClassPaths(classLoader, compilationResult.getDependencies());
        ClassLoaders.addClassPath(classLoader, compilationResult.getClassPath().getAbsolutePath());

        List<QObject> classesLoaded = new ArrayList<>();
        for (QObject className : args.get("classes").listValue()) {
            if (!className.isStr()) runtime.error(new QUnsuitableTypeException("String", className));
            try {
                Class<?> loaded = classLoader.loadClass(className.strValue());
                classesLoaded.add(new JavaClass(loaded));
            } catch (ClassNotFoundException e) {
                runtime.error(new JIJavaException(e));
                return Val();
            }
        }

        return Val(classesLoaded);
    }

}
