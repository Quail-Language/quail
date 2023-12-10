package me.tapeline.quail.qdk.libconverter;

import me.tapeline.quail.qdk.templater.TemplateUtils;
import me.tapeline.quail.qdk.templater.TemplatedFile;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

public class AdapterDraft {

    private final Class<?> adaptingClass;
    private final String name;
    private String[] usedDrafts;
    private final List<DraftedMethod> draftedMethods = new ArrayList<>();
    private final List<TemplatedFile> resultingFiles = new ArrayList<>();
    private String draftedClass;

    public AdapterDraft(Class<?> adaptingClass, String name, String[] usedDrafts) {
        this.adaptingClass = adaptingClass;
        this.name = name;
        this.usedDrafts = usedDrafts;
    }

    public Class<?> getAdaptingClass() {
        return adaptingClass;
    }

    public String getName() {
        return name;
    }

    public String[] getUsedDrafts() {
        return usedDrafts;
    }

    public void setUsedDrafts(String[] usedDrafts) {
        this.usedDrafts = usedDrafts;
    }

    private String getNameForMethod(String methodName) {
        return name + "Func" + StringUtils.capitalize(methodName);
    }

    public List<TemplatedFile> getResultingFiles() {
        return resultingFiles;
    }

    private DraftedMethod draftMethod(Method method) {
        StringBuilder arguments = new StringBuilder();
        if (!Modifier.isStatic(method.getModifiers()))
            arguments.append("new FuncArgument(\n")
                    .append("                                 \"this\",\n")
                    .append("                                 QObject.Val(),\n")
                    .append("                                 new int[0],\n")
                    .append("                                 LiteralFunction.Argument.POSITIONAL\n")
                    .append("                        ),\n                        ");
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isImplicit()) {
                arguments.append("new FuncArgument(\n")
                        .append("        \"this\",\n")
                        .append("        QObject.Val(),\n")
                        .append("        new int[0],\n")
                        .append("        LiteralFunction.Argument.POSITIONAL\n")
                        .append("),\n");
            } else {
                arguments.append("new FuncArgument(\n").append("                                 \"").append(parameter.getName()).append("\",\n")
                        .append("                                 QObject.Val(),\n");

                arguments.append("                                 new int[] {")
                        .append(StringUtils.join(TemplateUtils.modifiersToStrings(
                                new int[]{Utils.javaClassToModifier(parameter.getType())}
                        ), ", "))
                        .append("},\n");
                if (parameter.isVarArgs())
                    arguments.append("                                 LiteralFunction.Argument.POSITIONAL_CONSUMER\n");
                else
                    arguments.append("                                 LiteralFunction.Argument.POSITIONAL\n");
                arguments.append("                        ),\n                         ");
            }
        }
        if (arguments.toString().endsWith(",\n                         "))
            arguments.deleteCharAt(arguments.length() - 27);

        StringBuilder code = new StringBuilder();
        code.append("import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;\n" + "import me.tapeline.quailj.runtime.Runtime;\n" + "import me.tapeline.quailj.runtime.RuntimeStriker;\n" + "import me.tapeline.quailj.typing.classes.QObject;\n" + "import me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException;\n" + "import me.tapeline.quailj.typing.classes.errors.QNotInitializedException;\n" + "import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;\n" + "import me.tapeline.quailj.typing.utils.FuncArgument;\n" + "\n" + "import java.util.Arrays;\n" + "import java.util.HashMap;\n" + "import java.util.List;\n" + "\n" + "public class ").append(getNameForMethod(method.getName())).append(" extends QBuiltinFunc {\n").append("\n").append("\n").append("    public ").append(getNameForMethod(method.getName())).append("(Runtime runtime, Memory closure) {\n");
        code.append("        super(\n").append("                \"").append(method.getName()).append("\",\n")
                .append("                Arrays.asList(\n").append("                        ").append(arguments).append("\n")
                .append("                ),\n")
                .append("                runtime,\n")
                .append("                closure,\n")
                .append("                false\n")
                .append("        );\n");
        code.append("    }\n\n").append("    public ").append(getNameForMethod(method.getName())).append("(Runtime runtime) {\n")
                .append("        this(runtime, runtime.getMemory());\n")
                .append("    }\n\n");
        code.append("    @Override\n" +
                "    public QObject action(Runtime runtime, HashMap<String, QObject> args, " +
                "List<QObject> argList) throws RuntimeStriker {\n");
        code.append("        if (!(args.get(\"this\") instanceof ").append(name).append("))\n").append("           runtime.error(new QUnsuitableTypeException(\"").append(name).append("\", args.get(\"this\")));\n").append("        ").append(name).append(" thisObject = ((").append(name).append(") args.get(\"this\"));\n").append("        if (!thisObject.isInitialized())\n").append("           runtime.error(new QNotInitializedException(\"").append(name).append("\"));\n");
        for (Parameter parameter : method.getParameters()) {
            code.append("        ").append(parameter.getType().getCanonicalName()).append(" arg").append(StringUtils.capitalize(parameter.getName())).append(";\n");
            ValueRepresenter representer = ValueRepresenter.getRepresenterForType(
                    parameter.getParameterizedType());
            if (representer == null) {
                code.append("        arg").append(StringUtils.capitalize(parameter.getName())).append(" = DEFINE_A_VALUE;\n");
            } else {
                ValueRepresenter.Result result = representer.convertToJava(
                        parameter.getParameterizedType(),
                        "arg" + StringUtils.capitalize(parameter.getName()),
                        parameter.getName()
                );
                for (Class<?> cls : result.getImports())
                    code.insert(0, "import " + cls.getCanonicalName() + ";\n");
                code.append("        ").append(result.getResult()).append("\n");
            }
        }
        if (method.getReturnType().equals(Void.class) || method.getReturnType().equals(void.class)) {
            code.append("        thisObject.").append(method.getName()).append("(").append(StringUtils.join(
                    Arrays.stream(method.getParameters())
                            .map(p -> "arg" + StringUtils.capitalize(p.getName()))
                            .toArray(),
                    ", "
            )).append(");\n");
            code.append("        return QObject.Val();\n");
        } else {
            code.append("        Object returnValue = thisObject.").append(method.getName()).append("(").append(StringUtils.join(
                    Arrays.stream(method.getParameters())
                            .map(p -> "arg" + StringUtils.capitalize(p.getName()))
                            .toArray(),
                    ", "
            )).append(");\n");
            code.append("        QObject returnQValue;\n");
            ValueRepresenter representer = ValueRepresenter.getRepresenterForType(method.getGenericReturnType());
            if (representer != null) {
                ValueRepresenter.Result result = representer.convertFromJava(method.getGenericReturnType(),
                        "returnQValue", "returnValue");
                for (Class<?> cls : result.getImports())
                    code.insert(0, "import " + cls.getCanonicalName() + ";\n");
                code.append("        ").append(result.getResult()).append("\n");
            }
            code.append("        return returnQValue;\n");
        }

        code.append("    }\n\n").append("}\n");

        return new DraftedMethod(method.getName(), getNameForMethod(method.getName()), code.toString());
    }

    private void draftClass() {
        List<String> methodInitializers = new ArrayList<>();
        for (DraftedMethod method : draftedMethods)
            methodInitializers.add("new Pair<>(\"" + method.getName() + "\", new " +
                    method.getMethodClassName() + "(runtime))");
        String prototypeTable = StringUtils.join(methodInitializers, ",\n                            ");

        StringBuilder prototypeBuilder = new StringBuilder();
        prototypeBuilder.append("    public static ").append(getName()).append(" prototype(Runtime runtime) {\n");
        prototypeBuilder.append("        if (prototype == null)\n");
        prototypeBuilder.append("            prototype = new Event(\n");
        prototypeBuilder.append("                    new Table(Dict.make(\n");
        prototypeBuilder.append("                            ").append(prototypeTable).append("\n");
        prototypeBuilder.append("                    )),\n");
        prototypeBuilder.append("                    \"").append(getName()).append("\",\n");
        prototypeBuilder.append("                    FILL_THIS_IN,\n");
        prototypeBuilder.append("                    true\n");
        prototypeBuilder.append("            );\n");
        prototypeBuilder.append("        return prototype;\n");
        prototypeBuilder.append("    }\n\n");

        String constructor =
                "    public " + getName() +
                        "(Table table, String className, QObject parent, boolean isPrototype) {\n" +
                "        super(table, className, parent, isPrototype);\n" +
                "    }\n\n";

        StringBuilder classCode = new StringBuilder();
        classCode.append("import me.tapeline.quailj.runtime.Runtime;\n" +
                "import me.tapeline.quailj.runtime.RuntimeStriker;\n" +
                "import me.tapeline.quailj.runtime.Table;\n" +
                "import me.tapeline.quailj.typing.classes.QObject;\n" +
                "import me.tapeline.quailj.utils.Dict;\n" +
                "import me.tapeline.quailj.utils.Pair;\n\n");
        classCode.append("public class ").append(getName()).append(" extends QObject {\n\n");
        classCode.append("    public static ").append(getName()).append(" prototype = null;\n");
        classCode.append(prototypeBuilder);
        classCode.append(constructor);
        classCode.append(("    @Override\n" +
                        "    public QObject derive(Runtime runtime) throws RuntimeStriker {\n" +
                        "        if (!isPrototype)\n" +
                        "            runtime.error(\"Attempt to inherit from non-prototype value\");\n" +
                        "        return new $$$(new Table(), className, this, false);\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {\n" +
                        "        if (!isPrototype)\n" +
                        "            runtime.error(\"Attempt to inherit from non-prototype value\");\n" +
                        "        return new $$$(new Table(), className, this, true);\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    public QObject copy() {\n" +
                        "        QObject copy = new $$$(table, className, parent, isPrototype);\n" +
                        "        copy.setInheritableFlag(isInheritable);\n" +
                        "        return copy;\n" +
                        "    }\n\n" +
                "}\n").replaceAll("\\$\\$\\$", getName()
        ));
        draftedClass = classCode.toString();
    }

    public void generate(String selectedPackage) {
        selectedPackage += "." + adaptingClass.getSimpleName().toLowerCase();

        for (Method method : adaptingClass.getDeclaredMethods())
            draftedMethods.add(draftMethod(method));
        draftClass();

        for (DraftedMethod draftedMethod : draftedMethods) {
            TemplatedFile file = new TemplatedFile(
                    selectedPackage,
                    draftedMethod.getMethodClassName(),
                    "package " + selectedPackage + ";\n\n" +
                    draftedMethod.getCode()
            );
            resultingFiles.add(file);
        }
        TemplatedFile classFile = new TemplatedFile(
                selectedPackage,
                getName(),
                "package " + selectedPackage + ";\n\n" + draftedClass
        );
        resultingFiles.add(classFile);
    }

}
