package me.tapeline.quail.qdk.libconverter;

public class DraftedMethod {

    private final String name;
    private final String methodClassName;
    //private ClassSourceGenerator generator;
    private final String code;

    public DraftedMethod(String name, String methodClassName, String code) {
        this.name = name;
        this.code = code;
        this.methodClassName = methodClassName;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getMethodClassName() {
        return methodClassName;
    }

}
