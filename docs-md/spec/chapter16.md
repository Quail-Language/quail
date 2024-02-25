# Quail Specification
[Back to index](index.md)

## Chapter 16: Addons

### 16.1 Addon definition
Addons are .jar-packaged modules for Quail that can modify Quail behaviour.

They can add:
- Preprocessor directives
- Parser annotations
- Builtin libraries
- and they can modify runtime when it is created

### 16.2 Loading addons

Addons can be attached with a global flag:

```
quail -G.addons=path_to_addon_jar
```

Multiple addons specified as follows

```
quail -G.addons=path_to_addon1;path_to_addon2
```

### 16.3 Creating addons

You can find information about how to create addons on Quail Developer Tutorials