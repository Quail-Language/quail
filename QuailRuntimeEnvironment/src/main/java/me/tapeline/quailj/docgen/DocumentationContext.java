package me.tapeline.quailj.docgen;

import org.jetbrains.annotations.Nullable;

public class DocumentationContext {

    public final @Nullable DocumentationContext parent;

    public String qualifiedPath = "";

    public DocumentationContext(@Nullable DocumentationContext parent, String name) {
        this.parent = parent;
        if (parent != null)
            this.qualifiedPath = parent.qualifiedPath + "::" + name;
        else
            this.qualifiedPath = name;
    }

    public String getParentQualifiedPath() {
        return parent != null? parent.qualifiedPath : "";
    }

}
