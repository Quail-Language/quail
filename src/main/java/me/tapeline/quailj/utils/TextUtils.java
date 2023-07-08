package me.tapeline.quailj.utils;

import com.sun.org.apache.xpath.internal.operations.Mod;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.parsing.nodes.sections.BlockNode;
import me.tapeline.quailj.parsing.nodes.sections.CatchClause;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;

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
            sb.append(keys.get(i)).append("=").append(nodes.get(keys.get(i)).stringRepr());
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
            sb.append(keys.get(i)).append("=").append(nodes.get(keys.get(i)).stringRepr());
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

    public static String modifiersToStringRepr(int[] mods) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mods.length; i++) {
            if (IntFlags.check(mods[i], ModifierConstants.NULL)) sb.append("void ");
            if (IntFlags.check(mods[i], ModifierConstants.NUM)) sb.append("num ");
            if (IntFlags.check(mods[i], ModifierConstants.STR)) sb.append("string ");
            if (IntFlags.check(mods[i], ModifierConstants.NOTNULL)) sb.append("required ");
            if (IntFlags.check(mods[i], ModifierConstants.DICT)) sb.append("dict ");
            if (IntFlags.check(mods[i], ModifierConstants.LIST)) sb.append("list ");
            if (IntFlags.check(mods[i], ModifierConstants.BOOL)) sb.append("bool ");
            if (IntFlags.check(mods[i], ModifierConstants.FINAL)) sb.append("final ");
            if (IntFlags.check(mods[i], ModifierConstants.FINAL_ASSIGNED)) sb.append("final(finalized) ");
            if (IntFlags.check(mods[i], ModifierConstants.FUNC)) sb.append("func ");
            if (IntFlags.check(mods[i], ModifierConstants.LOCAL)) sb.append("local ");
            if (IntFlags.check(mods[i], ModifierConstants.OBJ)) sb.append("object ");
            if (IntFlags.check(mods[i], ModifierConstants.STATIC)) sb.append("static ");
            if (IntFlags.check(mods[i], ModifierConstants.VOID)) sb.append("void ");
            if (i + 1 < mods.length)
                sb.append("| ");
        }
        return sb.toString();
    }

}
