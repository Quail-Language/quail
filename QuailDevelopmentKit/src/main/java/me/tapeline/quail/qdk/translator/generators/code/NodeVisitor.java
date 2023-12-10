package me.tapeline.quail.qdk.translator.generators.code;

import me.tapeline.quail.qdk.translator.generators.code.expression.AssignGenerator;
import me.tapeline.quail.qdk.translator.generators.code.literals.NumGenerator;
import me.tapeline.quail.qdk.translator.generators.code.sections.BlockGenerator;
import me.tapeline.quail.qdk.translator.generators.code.variable.VariableGenerator;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.expression.AssignNode;
import me.tapeline.quailj.parsing.nodes.literals.LiteralNum;
import me.tapeline.quailj.parsing.nodes.sections.BlockNode;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;

import java.util.ArrayList;
import java.util.List;

public class NodeVisitor {

    public CodeGenerator visit(Node node) throws UnsupportedNodeException {
        if (node instanceof AssignNode) {
            AssignNode thisNode = (AssignNode) node;
            return new AssignGenerator(
                    node.getToken(),
                    (VariableGenerator) visit(thisNode.variable),
                    visit(thisNode.value)
            );
        } else if (node instanceof VariableNode) {
            return new VariableGenerator(
                    node.getToken(),
                    ((VariableNode) node).name,
                    ((VariableNode) node).modifiers
            );
        } else if (node instanceof BlockNode) {
            List<CodeGenerator> generators = new ArrayList<>();
            for (Node child : ((BlockNode) node).nodes)
                generators.add(visit(child));
            return new BlockGenerator(node.getToken(), generators);
        } else if (node instanceof LiteralNum) {
            return new NumGenerator(node.getToken(), ((LiteralNum) node).value);
        }
        throw new UnsupportedNodeException(node.getClass());
    }

}
