package me.tapeline.quailj.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IO {

    void setEncoding(String encoding);
    String getEncoding();

    void println(String str);
    void print(String str);

    String readLine() throws IOException;

    File file(String path);
    String readFile(String path) throws IOException;
    void writeFile(String path, String content) throws IOException;
    boolean fileExists(String path);
    boolean makePath(String path) throws IOException;
    void deleteFile(String path) throws IOException;
    void deleteDirectory(String path) throws IOException;
    void move(String oldPath, String newPath) throws IOException;
    void copy(String file, String copy) throws IOException;
    List<String> list(String directory);
    String absPath(String path);
    void setCwd(String cwd);
    String getCwd();
    void resetCwd();
    String[] splitPath(String path);
    String buildPath(String[] components);

}
