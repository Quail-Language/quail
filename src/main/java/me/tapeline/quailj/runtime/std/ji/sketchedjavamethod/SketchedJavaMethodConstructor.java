package me.tapeline.quailj.runtime.std.ji.sketchedjavamethod;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.runtime.std.ji.Arg;
import me.tapeline.quailj.runtime.std.ji.JIJavaException;
import me.tapeline.quailj.runtime.std.ji.JIMethodRegistry;
import me.tapeline.quailj.runtime.std.ji.javaclass.JavaClass;
import me.tapeline.quailj.runtime.std.ji.javamethod.JavaMethod;
import me.tapeline.quailj.runtime.std.ji.javaobject.JavaObject;
import me.tapeline.quailj.typing.classes.QFunc;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;
import me.tapeline.quailj.typing.classes.errors.QUnsuitableValueException;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
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

        String[] signature = args.get("signature").strValue().split(" ");
        String name = signature[signature.length - 1];
        String returnValue = signature[signature.length - 2];
        boolean isPublic = false,
                isStatic = false,
                isFinal = false,
                isProtected = false,
                isPrivate = false;
        for (int i = 0; i < signature.length - 2; i++) {
            if (signature[i].equals("public")) isPublic = true;
            if (signature[i].equals("private")) isPrivate = true;
            if (signature[i].equals("protected")) isProtected = true;
            if (signature[i].equals("final")) isFinal = true;
            if (signature[i].equals("static")) isStatic = true;
        }

        Class<?>[] argClasses = new Class<?>[args.get("args").listValue().size()];
        String[] argNames = new String[args.get("args").listValue().size()];
        List<QObject> argsList = args.get("args").listValue();
        assert argsList != null;
        for (int i = 0; i < argsList.size(); i++) {
            if (!argsList.get(i).isList())
                runtime.error(new QUnsuitableTypeException("List", argsList.get(i)));
            if (argsList.get(i).listValue().size() != 2)
                runtime.error(new QUnsuitableValueException("List should be size 2",
                        argsList.get(i)));
            QObject classString = argsList.get(i).listValue().get(0);
            QObject argNameString = argsList.get(i).listValue().get(1);
            if (!classString.isStr() || !argNameString.isStr())
                runtime.error(new QUnsuitableValueException(
                        "List should be [[str, str], ...]", argsList.get(i)));

            try {
                argClasses[i] = Class.forName(classString.strValue());
            } catch (ClassNotFoundException e) {
                runtime.error(new JIJavaException(e));
                return Val();
            }
            argNames[i] = argNameString.strValue();
        }

        long id = JIMethodRegistry.add(((QFunc) args.get("func")), runtime);
        FunctionSourceGenerator generator =
                FunctionSourceGenerator.create(name)
                .useType(JIMethodRegistry.class,
                        QFunc.class,
                        QObject.class,
                        Arg.class);
        for (int i = 0; i < argClasses.length; i++) {
            generator.addParameter(
                    VariableSourceGenerator.create(argClasses[i], argNames[i])
            );
        }
        generator.setReturnType(returnValue);
        if (isPublic) generator.addModifier(Modifier.PUBLIC);
        if (isPrivate) generator.addModifier(Modifier.PRIVATE);
        if (isProtected) generator.addModifier(Modifier.PROTECTED);
        if (isFinal) generator.addModifier(Modifier.FINAL);
        if (isStatic) generator.addModifier(Modifier.STATIC);
        generator.addBodyCode(
                "QFunc func = JIMethodRegistry.get(" + id + ");",
                "QObject result = func.call(JIMethodRegistry.getRuntime(" + id + "),",
                "       Arg.transformArgsBack(" +
                        StringUtils.join(argNames, ", ") +
                        "), new HashMap<>());",
                "return Arg.transform(result);"
        );

        javaMethod.setFunction(generator);

        return javaMethod;
    }

}
