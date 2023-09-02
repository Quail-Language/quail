package me.tapeline.quail.qdk.templater;

public class TemplatedFile {

    private String code;
    private String name;

    public TemplatedFile(String name, String code) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
