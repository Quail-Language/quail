package me.tapeline.quail.qdk.templater;

import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.utils.IntFlags;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TemplateUtils {

    public static String[] modifiersToStrings(int[] modifiers) {
        String[] modifierSequences = new String[modifiers.length];
        for (int i = 0; i < modifiers.length; i++) {
            List<String> sequence = new ArrayList<>();
            if (IntFlags.check(modifiers[i], ModifierConstants.NULL)) sequence.add("ModifierConstants.NULL");
            if (IntFlags.check(modifiers[i], ModifierConstants.NUM)) sequence.add("ModifierConstants.NUM");
            if (IntFlags.check(modifiers[i], ModifierConstants.STR)) sequence.add("ModifierConstants.STR");
            if (IntFlags.check(modifiers[i], ModifierConstants.NOTNULL)) sequence.add("ModifierConstants.NOTNULL");
            if (IntFlags.check(modifiers[i], ModifierConstants.DICT)) sequence.add("ModifierConstants.DICT");
            if (IntFlags.check(modifiers[i], ModifierConstants.LIST)) sequence.add("ModifierConstants.LIST");
            if (IntFlags.check(modifiers[i], ModifierConstants.BOOL)) sequence.add("ModifierConstants.BOOL");
            if (IntFlags.check(modifiers[i], ModifierConstants.FINAL)) sequence.add("ModifierConstants.FINAL");
            if (IntFlags.check(modifiers[i], ModifierConstants.FINAL_ASSIGNED)) sequence.add("ModifierConstants.FINAL_ASSIGNED");
            if (IntFlags.check(modifiers[i], ModifierConstants.FUNC)) sequence.add("ModifierConstants.FUNC");
            if (IntFlags.check(modifiers[i], ModifierConstants.LOCAL)) sequence.add("ModifierConstants.LOCAL");
            if (IntFlags.check(modifiers[i], ModifierConstants.OBJ)) sequence.add("ModifierConstants.OBJ");
            if (IntFlags.check(modifiers[i], ModifierConstants.STATIC)) sequence.add("ModifierConstants.STATIC");
            if (IntFlags.check(modifiers[i], ModifierConstants.VOID)) sequence.add("ModifierConstants.VOID");
            modifierSequences[i] = StringUtils.join(sequence, " | ");
        }
        return modifierSequences;
    }

}
