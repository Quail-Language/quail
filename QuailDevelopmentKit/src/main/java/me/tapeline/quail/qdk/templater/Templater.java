package me.tapeline.quail.qdk.templater;

import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.literals.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            FileUtils.writeStringToFile(new File(folder + "/" + file.getName()), file.getCode(),
                    "UTF-8");
        }
    }

    public void templateAll() {
        for (Node node : nodes) {
            TemplatedFile file = template(node);
            if (file != null) {
                files.add(file);
                System.out.println("Templated " + file.getName());
            }
        }
    }

    public TemplatedFile template(Node node) {
        if (node instanceof LiteralFunction) {
            String funcName = ((LiteralFunction) node).name;

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
            code.append("public class ").append(prefix).append(StringUtils.capitalize(funcName))
                    .append(" extends QBuiltinFunc {\n\n");
            code.append("    public ").append(prefix).append(StringUtils.capitalize(funcName))
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

            }
            code.append("                        )\n" +
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
            return new TemplatedFile(prefix + StringUtils.capitalize(funcName) + ".java", code.toString());
        }
        return null;
    }
}
