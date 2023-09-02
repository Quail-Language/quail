package me.tapeline.quailj.runtime.std.ji.sketchedjavamethod;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.ji.*;
import me.tapeline.quailj.runtime.std.ji.javaclass.JavaClass;
import me.tapeline.quailj.runtime.std.ji.javamethod.JavaMethod;
import me.tapeline.quailj.runtime.std.ji.javaobject.JavaObject;
import me.tapeline.quailj.typing.classes.QFunc;
import me.tapeline.quailj.typing.classes.QList;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableValueException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.utils.QListUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.burningwave.core.classes.FunctionSourceGenerator;
import org.burningwave.core.classes.VariableSourceGenerator;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SketchedJavaMethodConstructor extends QBuiltinFunc {


    public SketchedJavaMethodConstructor(Runtime runtime) {
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
                                "args",
                                QObject.Val(),
                                new int[] {ModifierConstants.LIST},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                                "func",
                                QObject.Val(),
                                new int[] {ModifierConstants.FUNC},
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
        if (!(args.get("this") instanceof SketchedJavaMethod))
            runtime.error(new QUnsuitableTypeException(args.get("this"),
                    "SketchedJavaMethod"));
        SketchedJavaMethod javaMethod = ((SketchedJavaMethod) args.get("this"));

        StringifiedSignature signature;
        try {
            signature = StringifiedSignature.parse(args.get("signature").strValue());
        } catch (ClassNotFoundException e) {
            runtime.error(new JIJavaException(e));
            return Val();
        }

        List<StringifiedSignature> argSignatures;
        try {
            argSignatures = StringifiedSignature.parseJavaArgs(
                    QListUtils.qStringListToStrings(((QList) args.get("args"))));
        } catch (ClassNotFoundException e) {
            runtime.error(new JIJavaException(e));
            return Val();
        }

        long id = JIMethodRegistry.add(((QFunc) args.get("func")), runtime);
        FunctionSourceGenerator generator =
                FunctionSourceGenerator.create(signature.getName())
                .useType(JIMethodRegistry.class,
                        QFunc.class,
                        QObject.class,
                        Arg.class,
                        HashMap.class,
                        ValueCaster.class);
        String[] argNames = new String[argSignatures.size() + 1];
        argNames[0] = "this";
        for (int i = 0; i < argSignatures.size(); i++) {
            generator.addParameter(
                    VariableSourceGenerator.create(argSignatures.get(i).getType(), argSignatures.get(i).getName())
                            .addModifier(argSignatures.get(i).getModifier())
            );
            argNames[i + 1] = argSignatures.get(i).getName();
        }
        generator.setReturnType(signature.getType());
        generator.addModifier(signature.getModifier());
        Class<?> wrappedTarget = ClassUtils.primitiveToWrapper(signature.getType());
        String bodyCode = "QFunc func = JIMethodRegistry.get(" + id + ");\n" +
                          "try {\n" +
                          "   QObject result = func.call(JIMethodRegistry.getRuntime(" + id + "),\n" +
                          "           Arg.transformArgsBack(" +
                          StringUtils.join(argNames, ", ") +
                          "), new HashMap<>());\n";
                          //"   return ((" + signature.getType().getSimpleName() + ") Arg.transform(result));\n"
        if (signature.getType() != void.class)
            bodyCode += "   return new ValueCaster<" + wrappedTarget.getSimpleName()
                        + ">(Arg.transform(result)).cast(" + wrappedTarget.getSimpleName() + ".class);\n";
        bodyCode += "} catch (Exception e___________) {\n" +
                    "   throw new RuntimeException(e___________);\n" +
                    "}";
        generator.addBodyCode(bodyCode);

        javaMethod.setFunction(generator);
        return javaMethod;
    }

}
