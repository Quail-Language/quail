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

}
