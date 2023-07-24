package me.tapeline.quailj.docgen;

import com.sun.istack.internal.Nullable;

public class DocumentationContext {

    public @Nullable DocumentationContext parent;

    public String qualifiedPath = "";

    public DocumentationContext(DocumentationContext parent, String name) {
        this.parent = parent;
        if (parent != null)
            this.qualifiedPath = parent.qualifiedPath + "::" + name;
        else
            this.qualifiedPath = name;
    }

}