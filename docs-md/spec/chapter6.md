# Quail Specification
[Back to index](index.md)

## Chapter 6: Typing and object system

<!-- TOC -->
* [Quail Specification](#quail-specification)
  * [Chapter 6: Typing and object system](#chapter-6-typing-and-object-system)
    * [6.1 Definitions](#61-definitions)
      * [6.1.1 Quail implements a prototype-based programming style.](#611-quail-implements-a-prototype-based-programming-style)
      * [6.1.2 Everything in Quail is an object.](#612-everything-in-quail-is-an-object)
      * [6.1.3 Nothing in Quail is also an object.](#613-nothing-in-quail-is-also-an-object)
    * [6.2 Objects, fields and methods](#62-objects-fields-and-methods)
    * [6.3 Prototype (class) creation and extension](#63-prototype-class-creation-and-extension)
    * [6.4 Objects with values](#64-objects-with-values)
    * [6.5 Method calls](#65-method-calls)
    * [6.6 Constructors](#66-constructors)
    * [6.7 Additional notes on extension and java-defined Quail classes](#67-additional-notes-on-extension-and-java-defined-quail-classes)
<!-- TOC -->

### 6.1 Definitions

#### 6.1.1 Quail implements a prototype-based programming style.

#### 6.1.2 Everything in Quail is an object.

#### 6.1.3 Nothing in Quail is also an object.

**<u>Object</u>** is the minimum unit of data in Quail. Object always contains a table, 
    metadata and could contain some value.

**<u>Table</u>** is a (string) key => (Object) value structure

**<u>Object metadata</u>** is a collection of different values that determinate Object's behaviour. These include:
* Object's class name
* Object's parent prototype
* Object's super-prototype
* Prototype flag - defines whether given object is a prototype
* Inheritability flag - defines whether given prototype could be extended

**<u>Prototype</u>** is an object that replaces classes. Defines fields and methods. Can also be referred as _class_.

**<u>Parent prototype</u>** is a prototype from which the object was derived

**<u>Derivation</u>** is an operation of making a new object in the likeness of prototype. 
Corresponds to instantiation in class-based OOP.

**<u>Super prototype</u>** is a prototype from which the given prototype was extended

**<u>Prototype extension / inheritance</u>** is an operation of making a new prototype in the likeness 
of super prototype. Corresponds to extension in class-based OOP.

**<u>Constructor</u>** is a special function that is called right after derivation and initialises the object

### 6.2 Objects, fields and methods
Fields and methods of a given object are stored in its table.

They can be accessed with dot notation: `myObject.myMethod(myObject.myField)`

New fields can be added, and existing modified dynamically:
```
myObject.myExistingField = 2
# No need to somehow explicitly create new field
myObject.myNewField = "Hello, this is a new value"
```

> A quite important note:
> Dictionaries in Quail override this dot notation, so you can access theirs values via it too:
> ```
> d = {"a" = 1, "b" = 2}
> print(d.a)
> ```

Methods are just fields that contain functions. Thus, dynamic creation and modification applies to them:
```
myObject.myExistingMethod = (this, args...) -> {}
myObject.myNewMethod = (this, args...) -> {}
```

Same rules apply to prototypes. And these changes affect extended prototypes and theis derivatives:
```
# assuming B extends from A:
a = A()
b = B()
A.someNewMethod = (this, args...) -> {}
# Even after creation, new methods and fields could be accessed
b.someNewMethod(arg1, arg2)
```

All objects have shadow fields that are not in the table and cannot be modified, but can be accessed:
```
myObject._className
myObject._superClass
myObject._objectPrototype  # object's prototype
myObject._isPrototype
myObject._isInheritable
```

### 6.3 Prototype (class) creation and extension
Class creation
```
class A {
    field1 = "some value"
    
    constructor(this, args...) {}

    method method1(this, args...) {}
    static method method2(this, args...) {}
}
```
Class extension
```
class B like A {
    # some additional functionality goes here
    # ...
}
```

### 6.4 Objects with values
As was written in 6.1 objects can have their own values. `Number` is an example of this:
```java
public class QNumber extends QObject {
    // ...
    protected double value; // This is the value
    // ...
    public QNumber(double value) {
        this(new Table(), prototype.className, prototype, false);
        this.value = value;
    }
    // ...
}
```
Thus, any `Number` can be treated in two different ways at once: as an object and as a number:
```
number = 3.14
number.myField = "some value"
print(number + 1)
```
And using dynamic method addition, we can extend default values with additional functionality:
```
Number.square = (this) -> {
    return this ^ 2
}
print(4.square())
```
When extending class with values, these values are not lost: (see 6.Appendix I)
```
class MyNumber like Number {
    constructor(this, value) {
        # more info in Standard library Chapter
        reflect.setNumberValue(this, value)
    }
    
    method square(this) {
        return this ^ 2
    }
}
a = 3
b = MyNumber(2)
print(3 + b.square())
# will print 7
```

### 6.5 Method calls

When a function is called via field access (`obj.f()`) it is treated as a method call. Only exceptions are:
* `obj` is a prototype
* `obj` is a dictionary and it contains `f` as a key
* `f` is a static function
If at least one of these conditions match, then it is treated as a function call.

The only difference between function and method call is that in method call, 
reference to the object is put into arguments:
```
# this is how you see a method call
obj.method1(arg1, arg2)
# this is how interpreter sees it
obj.method(obj, arg1, arg2)
```

So if method to be extracted from object and called separately, it will be treated as a function call and 
in order to make it function correctly, you should manually pass the reference:
```
method1 = obj.method1
method1(obj, arg1, arg2)
```

### 6.6 Constructors

Constructor in objects is always named `_constructor`.

When new object is being initialised, it is firstly derived from prototype, then initialised with constructor:
```java
public class QObject {
    // ...
    public QObject newObject(Runtime runtime, List<QObject> args, HashMap<String, QObject> kwargs)
            throws RuntimeStriker {
        QObject blank = derive(runtime);
        blank = blank.callFromThis(runtime, "_constructor", args, kwargs);
        return blank;
    }
    // ...
}
```
Constructor receives reference to initialising object as first argument, then all the other args:
```
class A {
    constructor(this, args...) {}
}
```
Value which constructor returns is ignored.

### 6.7 Additional notes on extension and java-defined Quail classes
When Quail class is defined in Java as a Java class also (e.g. as `QNumber` in example above), prototypes that
extend from it remain to be this particular Java class:
```
class MyNumber like Number {
    constructor(this, value) {
        # more info in Standard library Chapter
        reflect.setNumberValue(this, value)
    }
    
    method square(this) {
        return this ^ 2
    }
}
a = 3
b = MyNumber(2)
print(3 + b.square())
# will print 7
```
From Java perspective, `MyNumber` prototype is an instance of `QNumber` Java class.
