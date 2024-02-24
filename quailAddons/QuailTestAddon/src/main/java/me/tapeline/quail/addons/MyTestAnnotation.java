package me.tapeline.quail.addons;

import me.tapeline.quailj.parsing.annotation.Annotation;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.Arrays;

public class MyTestAnnotation implements Annotation {
    @Override
    public String name() {
        return "MyTestAnnotation";
    }

    @Override
    public Node apply(Node target, Object... args) {
        System.out.println("MyTestAnnotation called with args:");
        System.out.println(Arrays.toString(args));
        return target;
    }
}
