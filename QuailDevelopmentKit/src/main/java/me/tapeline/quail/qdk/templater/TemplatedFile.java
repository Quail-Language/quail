package me.tapeline.quail.qdk.templater;

public class TemplatedFile {

    private final String code;
    private final String name;

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
