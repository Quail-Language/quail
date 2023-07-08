package me.tapeline.quailj.typing.modifiers;

import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.utils.IntFlags;

public class ModifierConstants {

    public static final int UNCLARIFIED = 0;
    public static final int NUM         = 1;
    public static final int BOOL        = 2;
    public static final int NULL        = 4;
    public static final int NOTNULL     = 8;
    public static final int LOCAL       = 16;
    public static final int FINAL       = 32;
    public static final int STATIC      = 64;
    public static final int DICT        = 128;
    public static final int LIST        = 256;
    public static final int STR         = 512;
    public static final int FUNC        = 1024;
    public static final int OBJ         = 2048;
    public static final int VOID        = 8192;
    public static final int FINAL_ASSIGNED = 16384;

    public static boolean matchesOnAssign(int[] flags, QObject value) {
        return false; // TODO
    }

    public static boolean matchesOnAssign(int flag, QObject value) {
        return false; // TODO
    }

    public static boolean couldBeNull(int[] flags) {
        for (int flag : flags)
            if (!IntFlags.check(flag, NOTNULL) || IntFlags.check(flag, NULL))
                return true;
        return false;
    }

}
