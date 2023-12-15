package me.tapeline.quailj;

/**
 * Contains global interpreter flags
 * Global flags can be set with arguments:
 * java -jar quail.jar -G.encoding="cp1251" run program.q
 * @author Tapeline
 */
public class GlobalFlags {

    /**
     * Default encoding for reading and writing all files
     */
    public static String encoding = "UTF-8";

    /**
     * Whether to parse Quail docs or ignore them
     */
    public static boolean ignoreDocs = false;

    public static boolean displayReturnValue = false;

    public static final int jiFieldCacheSize = 10;
    public static final int jiMethodCacheSize = 10;

    public static short debugPort = 4004;

}
