package me.tapeline.quailj.parsing.annotation;

import me.tapeline.quailj.parsing.nodes.Node;

public interface Annotation {

    String name();
    Node apply(Node target, Object... args);

}
