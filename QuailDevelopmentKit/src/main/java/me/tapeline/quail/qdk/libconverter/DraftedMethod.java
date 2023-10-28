package me.tapeline.quail.qdk.libconverter;

import org.burningwave.core.classes.ClassSourceGenerator;

public class DraftedMethod {

    private String name;
    private String methodClassName;
    //private ClassSourceGenerator generator;
    private String code;

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
