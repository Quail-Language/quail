package me.tapeline.quail.qdk.libconverter;

import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

import java.util.*;

public class Utils {

    public static boolean notPresentedInQuail(Class<?> cls) {
        return !(modifiersForClasses.containsKey(cls));
    }

    private static final HashMap<Class<?>, Integer> modifiersForClasses = Dict.make(
            new Pair<>(Number.class, ModifierConstants.NUM),
            new Pair<>(int.class, ModifierConstants.NUM),
            new Pair<>(long.class, ModifierConstants.NUM),
            new Pair<>(float.class, ModifierConstants.NUM),
            new Pair<>(double.class, ModifierConstants.NUM),
            new Pair<>(boolean.class, ModifierConstants.BOOL),
            new Pair<>(Boolean.class, ModifierConstants.BOOL),
            new Pair<>(void.class, ModifierConstants.VOID),
            new Pair<>(Void.class, ModifierConstants.VOID),
            new Pair<>(Map.class, ModifierConstants.DICT),
            new Pair<>(HashMap.class, ModifierConstants.DICT),
            new Pair<>(List.class, ModifierConstants.LIST),
            new Pair<>(ArrayList.class, ModifierConstants.LIST),
            new Pair<>(Set.class, ModifierConstants.LIST),
            new Pair<>(String.class, ModifierConstants.STR)
    );

    public static int javaClassToModifier(Class<?> cls) {
        if (cls.isArray()) return ModifierConstants.LIST;
        if (modifiersForClasses.containsKey(cls)) return modifiersForClasses.get(cls);
        return ModifierConstants.OBJ;
    }

}
