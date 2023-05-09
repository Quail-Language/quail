package me.tapeline.quailj.utils;

import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.parsing.nodes.sections.BlockNode;
import me.tapeline.quailj.parsing.nodes.sections.CatchClause;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextUtils {

    public static String catchClauseListToStringRepr(List<CatchClause> clauses) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < clauses.size(); i++) {
            sb.append(clauses.get(i).stringRepr());
            if (i + 1 < clauses.size())
                sb.append(" ");
        }
        return sb.toString() + "]";
    }

    public static String blockListToStringRepr(List<BlockNode> nodes) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nodes.size(); i++) {
            sb.append(nodes.get(i).stringRepr());
            if (i + 1 < nodes.size())
                sb.append(" ");
        }
        return sb.toString() + "]";
    }

    public static String nodeListToStringRepr(List<Node> nodes) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nodes.size(); i++) {
            sb.append(nodes.get(i).stringRepr());
            if (i + 1 < nodes.size())
                sb.append(" ");
        }
        return sb.toString() + "]";
    }

    public static String nodeMapToStringRepr(Map<String, Node> nodes) {
        StringBuilder sb = new StringBuilder("{");
        List<String> keys = new ArrayList<>(nodes.keySet());
        keys.sort(String::compareTo);
        for (int i = 0; i < keys.size(); i++) {
            sb.append(nodes.get(keys.get(i)).stringRepr());
            if (i + 1 < keys.size())
                sb.append(" ");
        }
        return sb.toString() + "}";
    }

    public static String methodMapToStringRepr(Map<String, LiteralFunction> nodes) {
        StringBuilder sb = new StringBuilder("{");
        List<String> keys = new ArrayList<>(nodes.keySet());
        keys.sort(String::compareTo);
        for (int i = 0; i < keys.size(); i++) {
            sb.append(nodes.get(keys.get(i)).stringRepr());
            if (i + 1 < keys.size())
                sb.append(" ");
        }
        return sb.toString() + "}";
    }

    public static String iteratorsToStringRepr(List<VariableNode> nodes) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nodes.size(); i++) {
            sb.append(nodes.get(i).stringRepr());
            if (i + 1 < nodes.size())
                sb.append(" ");
        }
        return sb.toString() + "]";
    }

    public static String argumentListToStringRepr(List<LiteralFunction.Argument> arguments) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arguments.size(); i++) {
            sb.append(arguments.get(i).name).append("=")
                    .append(arguments.get(i).defaultValue.stringRepr());
            if (i + 1 < arguments.size())
                sb.append(" ");
        }
        return sb.toString() + "]";
    }

}
