package me.tapeline.quailj.utils;

import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.parsing.nodes.sections.BlockNode;
import me.tapeline.quailj.parsing.nodes.sections.CatchClause;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;

import java.util.ArrayList;
import java.util.Collection;
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
        return sb + "]";
    }

    public static String blockListToStringRepr(List<BlockNode> nodes) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nodes.size(); i++) {
            sb.append(nodes.get(i).stringRepr());
            if (i + 1 < nodes.size())
                sb.append(" ");
        }
        return sb + "]";
    }

    public static String nodeListToStringRepr(List<? extends Node> nodes) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nodes.size(); i++) {
            sb.append(nodes.get(i).stringRepr());
            if (i + 1 < nodes.size())
                sb.append(" ");
        }
        return sb + "]";
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
        return sb + "}";
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
        return sb + "}";
    }

    public static String iteratorsToStringRepr(List<VariableNode> nodes) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < nodes.size(); i++) {
            sb.append(nodes.get(i).stringRepr());
            if (i + 1 < nodes.size())
                sb.append(" ");
        }
        return sb + "]";
    }

    public static String argumentListToStringRepr(List<LiteralFunction.Argument> arguments) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arguments.size(); i++) {
            sb.append(arguments.get(i).name).append("=")
                    .append(arguments.get(i).defaultValue.stringRepr());
            if (i + 1 < arguments.size())
                sb.append(" ");
        }
        return sb + "]";
    }

    public static String modifiersToStringRepr(int[] mods) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mods.length; i++) {
            sb.append(modifierToStringRepr(mods[i]));
            if (i + 1 < mods.length)
                sb.append("| ");
        }
        return sb.toString();
    }

    public static String modifierToStringRepr(int mod) {
        StringBuilder sb = new StringBuilder();
        if (IntFlags.check(mod, ModifierConstants.NULL)) sb.append("void ");
        if (IntFlags.check(mod, ModifierConstants.NUM)) sb.append("num ");
        if (IntFlags.check(mod, ModifierConstants.STR)) sb.append("string ");
        if (IntFlags.check(mod, ModifierConstants.NOTNULL)) sb.append("required ");
        if (IntFlags.check(mod, ModifierConstants.DICT)) sb.append("dict ");
        if (IntFlags.check(mod, ModifierConstants.LIST)) sb.append("list ");
        if (IntFlags.check(mod, ModifierConstants.BOOL)) sb.append("bool ");
        if (IntFlags.check(mod, ModifierConstants.FINAL)) sb.append("final ");
        if (IntFlags.check(mod, ModifierConstants.FINAL_ASSIGNED)) sb.append("final(finalized) ");
        if (IntFlags.check(mod, ModifierConstants.FUNC)) sb.append("func ");
        if (IntFlags.check(mod, ModifierConstants.LOCAL)) sb.append("local ");
        if (IntFlags.check(mod, ModifierConstants.OBJ)) sb.append("object ");
        if (IntFlags.check(mod, ModifierConstants.STATIC)) sb.append("static ");
        if (IntFlags.check(mod, ModifierConstants.VOID)) sb.append("void ");
        return sb.toString();
    }

    public static String collectionToString(Collection<?> collection, String separator) {
        StringBuilder sb = new StringBuilder();
        for (Object element : collection)
            sb.append(element.toString()).append(separator);
        if (sb.toString().endsWith(separator))
            sb.delete(sb.length() - separator.length(), sb.length());
        return sb.toString();
    }

}
