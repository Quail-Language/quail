# Quail Developer Tutorials

## How to create a preprocessor directive

To create a preprocessor directive you need to create a class that extends
from `me.tapeline.quailj.preprocessing.directives.AbstractDirective`:

```java 
public class MyTestDirective extends AbstractDirective {

    @Override
    public String prefix() {
        return "testdirective";
    }

    @Override
    public List<DirectiveArgument> arguments() {
        return new ArrayList<>();
    }

    @Override
    public String applyDirective(String code, File scriptHome, StringBoundariesMap boundaries, Object... arguments) {
        System.out.println("Test directive applied");
        return code;
    }

}
```

Implement methods:
- `prefix` should return name of the directive. Prefix="testdirective" will
  correspond to this syntax: `#:testdirective`
- `arguments` should return a list of directive arguments. More info about
  this can be found in Quail Specification Chapter 3
- `applyDirective` should return the source code after applying this directive
  with given `arguments`, minding the fact that content in strings generally
  should not be touched. Locations of strings are given in `boundaries`