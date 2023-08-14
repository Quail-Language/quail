package me.tapeline.quailj.io;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class DefaultIO implements IO {

    private String encoding = "UTF-8";

    private String absCwd = "";

    @Override
    public File file(String path) {
        if (new File(path).isAbsolute())
            return new File(path);
        return Paths.get(absCwd, path).toFile();
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String getEncoding() {
        return encoding;
    }

    @Override
    public void println(String str) {
        System.out.println(str);
    }

    @Override
    public void print(String str) {
        System.out.print(str);
    }

    @Override
    public String readLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    @Override
    public String readFile(String path) throws IOException {
        return FileUtils.readFileToString(file(path), encoding);
    }

    @Override
    public void writeFile(String path, String content) throws IOException {
        FileUtils.writeStringToFile(file(path), content, encoding);
    }

    @Override
    public boolean fileExists(String path) {
        return file(path).exists();
    }

    @Override
    public boolean makePath(String path) {
        return file(path).mkdirs();
    }

    @Override
    public void deleteFile(String path) throws IOException {
        FileUtils.delete(file(path));
    }

    @Override
    public void deleteDirectory(String path) throws IOException {
        FileUtils.deleteDirectory(file(path));
    }

    @Override
    public void move(String oldPath, String newPath) throws IOException {
        if (file(oldPath).isDirectory())
            FileUtils.moveDirectory(file(oldPath), file(newPath));
        else
            FileUtils.moveFile(file(oldPath), file(newPath));
    }

    @Override
    public void copy(String file, String copy) throws IOException {
        if (this.file(file).isDirectory())
            FileUtils.copyDirectory(this.file(file), this.file(copy));
        else
            FileUtils.copyFile(this.file(file), this.file(copy));
    }

    @Override
    public List<String> list(String directory) {
        String[] ls = file(directory).list();
        if (ls == null)
            return new ArrayList<>();
        else
            return new ArrayList<>(Arrays.asList(ls));
    }

    @Override
    public String absPath(String path) {
        return file(path).getAbsolutePath();
    }

    @Override
    public void setCwd(String cwd) {
        this.absCwd = file(cwd).getAbsolutePath();
    }

    @Override
    public void resetCwd() {
        absCwd = new File("").getAbsolutePath();
    }

    @Override
    public String getCwd() {
        return absCwd;
    }

    @Override
    public String[] splitPath(String path) {
        return path.split(Pattern.quote(File.separator));
    }

    @Override
    public String buildPath(String[] components) {
        return Paths.get("", components).toString();
    }

}
