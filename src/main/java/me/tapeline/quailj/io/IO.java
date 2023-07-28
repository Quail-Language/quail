package me.tapeline.quailj.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Input/Output interface.
 * Default implementation - {@link me.tapeline.quailj.io.DefaultIO}
 * @author Tapeline
 */
public interface IO {

    /**
     * Sets the current encoding
     * @param encoding new encoding
     */
    void setEncoding(String encoding);

    /**
     * @return current encoding
     */
    String getEncoding();

    /**
     * Print line in console
     * @param str line
     */
    void println(String str);

    /**
     * Print string in console
     * @param str string
     */
    void print(String str);

    /**
     * Read line from console
     * @return line
     * @throws IOException if something is wrong
     */
    String readLine() throws IOException;

    /**
     * Gets the file for given path
     * @param path file path
     * @return target file
     */
    File file(String path);

    /**
     * Reads file at given path
     * @param path file path
     * @return target file contents
     * @throws IOException if something goes wrong
     */
    String readFile(String path) throws IOException;

    /**
     * Writes file at given path. Content is replaced, not appended
     * @param path file path
     * @param content new content
     * @throws IOException if something goes wrong
     */
    void writeFile(String path, String content) throws IOException;

    /**
     * @param path target file
     * @return whether the file exists
     */
    boolean fileExists(String path);

    /**
     * Constructs missing directories by on given path
     * @param path target path
     * @return whether the action was successful
     */
    boolean makePath(String path);

    /**
     * Deletes file at given path
     * @param path target file
     * @throws IOException if something goes wrong
     */
    void deleteFile(String path) throws IOException;

    /**
     * Deletes directory with all files and
     * subdirectories recursively at given path
     * @param path target directories
     * @throws IOException if something goes wrong
     */
    void deleteDirectory(String path) throws IOException;

    /**
     * Moves given file or directory to a new path
     * @param oldPath target file or directory
     * @param newPath destination file or directory.
     *                Should be new file/directory name,
     *                not the path for the folder where
     *                target file/directory will be placed
     * @throws IOException if something goes wrong
     */
    void move(String oldPath, String newPath) throws IOException;

    /**
     * Copies given file or directory to a new path
     * @param file target file or directory
     * @param copy destination file or directory.
     *             Should be new file/directory name,
     *             not the path for the folder where
     *             copied file/directory will be placed
     * @throws IOException if something goes wrong
     */
    void copy(String file, String copy) throws IOException;

    /**
     * Get list of files in given directory
     * @param directory target directory
     * @return list of files
     */
    List<String> list(String directory);

    /**
     * Gets the absolute path for file
     * @param path relative path
     * @return absolute path
     */
    String absPath(String path);

    /**
     * Sets current working directory
     * @param cwd new cwd
     */
    void setCwd(String cwd);

    /**
     * @return current working directory
     */
    String getCwd();

    /**
     * Resets current working directory to initial
     */
    void resetCwd();

    /**
     * Splits path into components
     * @param path target path
     * @return path components
     */
    String[] splitPath(String path);

    /**
     * Joins given path components into path. Reverse for {@link #splitPath(String)}
     * @param components path components
     * @return built path
     */
    String buildPath(String[] components);

}
