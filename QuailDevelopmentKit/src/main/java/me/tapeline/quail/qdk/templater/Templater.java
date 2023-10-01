package me.tapeline.quail.qdk.templater;

import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.expression.VarAssignNode;
import me.tapeline.quailj.parsing.nodes.literals.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Templater {

    private final List<Node> nodes;
    private final String selectedPackage;
    private final String prefix;
    private final List<TemplatedFile> files;

    public Templater(List<Node> nodes, String selectedPackage, String prefix) {
        this.nodes = nodes;
        this.files = new ArrayList<>();
        this.selectedPackage = selectedPackage;
        this.prefix = prefix;
    }

    public List<TemplatedFile> getFiles() {
        return files;
    }

    public void templateAndWrite(String folder) throws IOException {
        if (new File(folder).exists())
            FileUtils.deleteDirectory(new File(folder));
        new File(folder).mkdirs();
        templateAll();
        for (TemplatedFile file : getFiles()) {
            FileUtils.writeStringToFile(new File(folder + "/" +
                            file.getFilePackage().replace('.', '/') + '/' + file.getName()),
                    file.getCode(),
                    "UTF-8");
        }
    }

    public void templateAll() {
        for (Node node : nodes) {
            TemplatedFile file = template(node, selectedPackage, null);
            if (file != null) {
                files.add(file);
                System.out.println("Templated " + file.getName());
            }
        }
    }

    public TemplatedFile template(Node node, String selectedPackage, String parentClassName) {
        if (node instanceof LiteralFunction) {
            String funcName = ((LiteralFunction) node).name;
            String funcClassName;
            if (parentClassName != null)
                funcClassName = parentClassName + StringUtils.capitalize(funcName);
            else
                funcClassName = prefix + StringUtils.capitalize(funcName);

            StringBuilder code = new StringBuilder();
            code.append("package ").append(selectedPackage).append(";\n\n");
            code.append("import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;\n" +
                    "import me.tapeline.quailj.runtime.Runtime;\n" +
                    "import me.tapeline.quailj.typing.modifiers.ModifierConstants;\n" +
                    "import me.tapeline.quailj.typing.classes.QObject;\n" +
                    "import me.tapeline.quailj.typing.utils.FuncArgument;\n" +
                    "import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;\n" +
                    "import me.tapeline.quailj.runtime.RuntimeStriker;\n" +
                    "\n" +
                    "import java.util.Arrays;\n" +
                    "import java.util.HashMap;\n" +
                    "import java.util.List;\n\n");
            code.append("public class ").append(funcClassName)
                    .append(" extends QBuiltinFunc {\n\n");
            code.append("    public ").append(funcClassName)
                    .append("(Runtime runtime) {\n");
            code.append("        super(\n");
            code.append("                \"").append(funcName).append("\",\n");
            code.append("                Arrays.asList(\n");
            for (LiteralFunction.Argument argument : ((LiteralFunction) node).args) {
                code.append("                        new FuncArgument(\n");
                code.append("                               \"").append(argument.name).append("\",\n");
                if (argument.defaultValue instanceof LiteralNull)
                    code.append("                                QObject.Val(),\n");
                else if (argument.defaultValue instanceof LiteralStr)
                    code.append("                                QObject.Val(\"")
                            .append(((LiteralStr) argument.defaultValue).value).append("\"),\n");
                else if (argument.defaultValue instanceof LiteralNum)
                    code.append("                                QObject.Val(")
                            .append(((LiteralNum) argument.defaultValue).value).append("),\n");
                else if (argument.defaultValue instanceof LiteralBool)
                    code.append("                                QObject.Val(")
                            .append(((LiteralBool) argument.defaultValue).value).append("),\n");
                else
                    code.append("                                QObject.Val(DEFINE_A_VALUE),\n");
                if (argument.modifiers.length == 0)
                    code.append("                                new int[0],\n");
                else
                    code.append("                                new int[] {")
                            .append(StringUtils.join(TemplateUtils.modifiersToStrings(argument.modifiers), ", "))
                            .append("},\n");
                if (argument.type == LiteralFunction.Argument.POSITIONAL)
                    code.append("                                LiteralFunction.Argument.POSITIONAL\n");
                else if (argument.type == LiteralFunction.Argument.POSITIONAL_CONSUMER)
                    code.append("                                LiteralFunction.Argument.POSITIONAL_CONSUMER\n");
                else if (argument.type == LiteralFunction.Argument.KEYWORD_CONSUMER)
                    code.append("                                LiteralFunction.Argument.KEYWORD_CONSUMER\n");
                code.append("                        ),\n");
            }
            if (code.toString().endsWith(",\n"))
                code.deleteCharAt(code.length() - 2);
            code.append(
                    "                ),\n" +
                    "                runtime,\n" +
                    "                runtime.getMemory(),\n" +
                    "                false\n" +
                    "        );\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public QObject action(Runtime runtime, HashMap<String, QObject> args, " +
                    "List<QObject> argList) throws RuntimeStriker {\n\n");
            code.append("    }\n\n")
                    .append("}\n");
            return new TemplatedFile(selectedPackage, funcClassName + ".java", code.toString());
        } else if (node instanceof LiteralClass) {
            String className = ((LiteralClass) node).name;

            StringBuilder tableCode = new StringBuilder("                    new Table(Dict.make(\n");
            for (VarAssignNode field : ((LiteralClass) node).contents) {
                tableCode.append("                            new Pair<>(\"")
                        .append(field.variable.name).append("\", FILL_THIS),\n");
            }
            StringBuilder methodImports = new StringBuilder();
            for (Map.Entry<String, LiteralFunction> method : ((LiteralClass) node).methods.entrySet()) {
                TemplatedFile methodFile = template(method.getValue(), selectedPackage + "."
                    + className.toLowerCase(Locale.ROOT), className);
                files.add(methodFile);
                methodImports.append("import ").append(methodFile.getFilePackage()).append("+")
                        .append(methodFile.getName()).append(";\n");
                tableCode.append("                            new Pair<>(\"")
                        .append(method.getKey()).append("\", new ")
                        .append(methodFile.getName(), 0, methodFile.getName().length() - 5)
                        .append("(this),\n");
            }
            if (tableCode.toString().endsWith(",\n"))
                tableCode.deleteCharAt(tableCode.lastIndexOf(",\n"));
            tableCode.append("                    ))");

            StringBuilder code = new StringBuilder();
            code.append("package ").append(selectedPackage).append(";\n\n");
            code.append("package ").append(selectedPackage).append(";\n\n");
            code.append("import me.tapeline.quailj.runtime.Runtime;\n" +
                    "import me.tapeline.quailj.runtime.RuntimeStriker;\n" +
                    "import me.tapeline.quailj.runtime.Table;\n" +
                    "import me.tapeline.quailj.typing.classes.QObject;\n" +
                    "import me.tapeline.quailj.utils.Dict;\n" +
                    "import me.tapeline.quailj.utils.Pair;\n")
                    .append(methodImports).append("\n")
                    .append("import java.util.Arrays;\n")
                    .append("import java.util.HashMap;\n")
                    .append("import java.util.List;\n\n");
            code.append("public class ")
                    .append(prefix)
                    .append(className)
                    .append(" extends ")
                    .append(((LiteralClass) node).like != null ? "FILL_THIS" : "QObject")
                    .append(" {\n\n");
            code.append("    public static ").append(prefix).append(className)
                    .append(" prototype = null;\n");
            code.append("    public static ").append(prefix).append(className)
                    .append(" prototype(Runtime runtime) {\n");
            code.append("        if (prototype == null)\n");
            code.append("            prototype = new ").append(prefix).append(className)
                    .append("(\n");
            code.append(tableCode).append(",\n");
            code.append("                    \"").append(className).append("\",\n");
            code.append("                    ")
                    .append(((LiteralClass) node).like != null ? "FILL_THIS" : "QObject.superObject").append(",\n");
            code.append("                    true\n");
            code.append("            );\n");
            code.append("        return prototype;\n");
            code.append("    }\n");
            code.append(("\n" +
                    "    public CLASS(Table table, String className, QObject parent, boolean isPrototype) {\n" +
                    "        super(table, className, parent, isPrototype);\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public QObject derive(Runtime runtime) throws RuntimeStriker {\n" +
                    "        if (!isPrototype)\n" +
                    "            runtime.error(\"Attempt to inherit from non-prototype value\");\n" +
                    "        return new CLASS(new Table(), className, this, false);\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public QObject extendAs(Runtime runtime, String className) throws RuntimeStriker {\n" +
                    "        if (!isPrototype)\n" +
                    "            runtime.error(\"Attempt to inherit from non-prototype value\");\n" +
                    "        return new CLASS(new Table(), className, this, true);\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public QObject copy() {\n" +
                    "        QObject copy = new CLASS(table, className, parent, isPrototype);\n" +
                    "        copy.setInheritableFlag(isInheritable);\n" +
                    "        return copy;\n" +
                    "    }\n" +
                    "\n" +
                    "}").replace("CLASS", prefix + className));
            return new TemplatedFile(selectedPackage, prefix + className + ".java", code.toString());
        }
        return null;
    }
}
