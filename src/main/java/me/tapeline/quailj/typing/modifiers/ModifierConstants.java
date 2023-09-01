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
        for (int i = 0; i < flags.length; i++)
            if (!matchesOnAssign(flags[i], value))
                return false;
        return true;
    }

    public static boolean matchesOnAssign(int flag, QObject value) {
        if ((flag & NUM) == NUM && !value.isNum()) return false;
        if ((flag & BOOL) == BOOL && !value.isBool()) return false;
        if ((flag & NULL) == NULL && !value.isNull()) return false;
        if ((flag & VOID) == VOID && !value.isNull()) return false;
        if ((flag & NOTNULL) == NOTNULL && value.isNull()) return false;
        if ((flag & DICT) == DICT && !value.isDict()) return false;
        if ((flag & LIST) == LIST && !value.isList()) return false;
        if ((flag & STR) == STR && !value.isStr()) return false;
        if ((flag & FUNC) == FUNC && !value.isFunc()) return false;
        if ((flag & FINAL_ASSIGNED) == FINAL_ASSIGNED) return false;
        return true;
    }

    public static boolean couldBeNull(int[] flags) {
        for (int flag : flags)
            if (IntFlags.check(flag, NOTNULL))
                return false;
            else if (IntFlags.check(flag, NULL) || IntFlags.check(flag, VOID))
                return true;
        return true;
    }

    public static void main(String[] args) {
        System.out.println(matchesOnAssign(NUM, QObject.Val("")));
    }

}
