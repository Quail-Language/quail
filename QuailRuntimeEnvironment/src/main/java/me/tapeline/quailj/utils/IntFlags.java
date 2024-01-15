package me.tapeline.quailj.utils;

public class IntFlags {

    /**
     * Checks if specified bit flag is set
     */
    public static boolean check(int flags, int flag) {
        return (flags & flag) == flag;
    }

}
