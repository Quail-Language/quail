package me.tapeline.quail.qdk.templater;

public class TemplatedFile {

    private final String code;
    private final String name;
    private final String filePackage;

    public TemplatedFile(String filePackage, String name, String code) {
        this.filePackage = filePackage;
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getFilePackage() {
        return filePackage;
    }

}
