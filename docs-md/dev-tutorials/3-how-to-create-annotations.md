# Quail Developer Tutorials

## How to create a parser annotation

To create a parser annotation you need to create a class that implements
`me.tapeline.quailj.parsing.annotation.Annotation`:

```java 
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
```

Implement methods:
- `name` should return name of the annotation. Name="MyTestAnnotation" will
  correspond to this syntax: `@MyTestAnnotation`
- `apply` should return the AST node after applying this annotation
  with given `args`
