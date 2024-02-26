# Quail Developer Tutorials

## How to create an addon

### Step 1. Create a project
First of all, create a **Java 8** project. Add QRE (`qre.jar`) as a dependency.

Also make sure, that when you compile your project into a .jar, it compiles
**without dependencies**.

### Step 2. Create an addon class
Create a class that extends from `me.tapeline.quailj.addons.QuailAddon`

You are going to need to implement methods. For example:

```java
package me.tapeline.quail.addons;

import me.tapeline.quailj.addons.QuailAddon;
import me.tapeline.quailj.parsing.annotation.Annotation;
import me.tapeline.quailj.preprocessing.directives.AbstractDirective;
import me.tapeline.quailj.runtime.librarymanagement.BuiltinLibrary;

import java.util.Arrays;
import java.util.List;

public class QuailTestAddon extends QuailAddon {

    @Override
    public String getName() {
        return "QuailTestAddon";
    }

    @Override
    public List<AbstractDirective> providedDirectives() {
        return Arrays.asList(
            new MyTestDirective()
        );
    }

    @Override
    public List<Annotation> providedAnnotations() {
        return Arrays.asList(
                new MyTestAnnotation()
        );
    }

    @Override
    public List<BuiltinLibrary> providedLibraries() {
        return Arrays.asList(
                new MyTestLibrary()
        );
    }

}
```

Here, `getName` method should return the name of your addon, other 3 methods
should return a list of things addon provides. 

Notice: return values of them
under any circumstances should not be `null`. If you don't provide, for example,
any directives, just return an empty list:

```java 
public List<AbstractDirective> providedDirectives() {
    return new ArrayList<>();
}
```

### Step 3. Define main class for addon
Now head to your project resources and create a file called `addonMainClass`
without any extension.

You should place a fully qualified ID of your newly created QuailAddon class
in there. 

Notice: this file should end with `\n`.

Example:
``` 
me.tapeline.quail.addons.QuailTestAddon

```

### Step 4. Create some content for your addon

Now you can create directives, annotations, libraries, etc. for your
addon, just don't forget to register them in your main class.

### Step 5. Packaging

Package your addon into a .jar. 

**Remember to include `addonMainClass` resource and exclude dependencies!**

### Step 6. Using addon

To attach an addon use global flag `-G.addons`

More info on this topic in Quail Specification Chapter 16.
