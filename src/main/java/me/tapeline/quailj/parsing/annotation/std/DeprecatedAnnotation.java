package me.tapeline.quailj.parsing.annotation.std;

import me.tapeline.quailj.parsing.annotation.Annotation;
import me.tapeline.quailj.parsing.nodes.Node;

public class DeprecatedAnnotation implements Annotation {

    @Override
    public String name() {
        return "Deprecated";
    }

    @Override
    public Node apply(Node target, Object... args) {
        return target;
    }

}
