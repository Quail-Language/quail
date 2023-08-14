package me.tapeline.quailj.utils;

public class IntFlags {

    public static boolean check(int flags, int flag) {
        return (flags & flag) == flag;
    }

}
