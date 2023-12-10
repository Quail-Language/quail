# Quail Specification
[Back to index](index.md)

## Chapter 10: Exceptions and errors

<!-- TOC -->
* [Quail Specification](#quail-specification)
  * [Chapter 10: Exceptions and errors](#chapter-10-exceptions-and-errors)
    * [10.1 Definitions](#101-definitions)
    * [10.2 Using exceptions](#102-using-exceptions)
      * [10.2.1 Built-in exceptions](#1021-built-in-exceptions)
      * [10.2.2 Creating exceptions](#1022-creating-exceptions)
      * [10.2.3 Throwing exceptions](#1023-throwing-exceptions)
      * [10.2.4 Catching exceptions](#1024-catching-exceptions)
      * [10.2.5 Behaviour](#1025-behaviour)
<!-- TOC -->

### 10.1 Definitions

**<u>Exception</u>** is a Quail object that represents an error. 
Exception can be thrown interrupting execution and caught when thrown to resume
execution.

### 10.2 Using exceptions

#### 10.2.1 Built-in exceptions

| Exception class                      | Meaning                                                               |
|--------------------------------------|-----------------------------------------------------------------------|
| `AssertionException`                 | Thrown when `assert` fails                                            |
| `CircularDependencyException`        | Thrown when circular import is detected (see $9.3.3)                  |
| `DerivationException`                | Thrown when derivation is impossible                                  |
| `Exception`                          | Base class for all exceptions                                         |
| `IndexOutOfBoundsException`          | Thrown when you try to access invalid index                           |
| `IOException`                        | Thrown when Java's `java.lang.IOException` is thrown                  |
| `IterationNotStartedException`       | Thrown when you try to access next element, but not started iterating |
| `IterationStopException`             | Thrown when iteration reached end                                     |
| `UnsuitableTypeException`            | Thrown when value is of unsuitable type for this operation            |
| `UnsuitableValueException`           | Thrown when value is unsuitable for this operation                    |
| `UnsupportedConversionException`     | Thrown when requesting conversion is not supported                    |
| `UnsupportedIterationException`      | Thrown when iteration is requested, but not supported by this object  |
| `UnsupportedOperationException`      | Thrown when requested operation is not supported                      |
| `UnsupportedStepSubscriptException`  | Thrown when subscript with step is not supported                      |
| `UnsupportedSubscriptException`      | Thrown when subscript is not supported                                |
| `UnsupportedUnaryOperationException` | Thrown when requested operation is not supported                      |

#### 10.2.2 Creating exceptions

You can inherit exception classes to create your own exceptions

```
class MyException like Exception {
    constructor (this, string message) {
        this.message = "MyException says: " + message
    }
}
```

#### 10.2.3 Throwing exceptions

To throw an exception use following syntax:

```
throw Exception() % { "message" = "This is an exception" }

throw MyException("Hello!")
```

#### 10.2.4 Catching exceptions

To catch an exception use following syntax:

```
try {
    # erroneous code
} catch as e {
    # exception placed into e
}

try {
    # erroneous code
} catch IOException as e {
    # only catches IOException, all other are left untouched
}

try {
    # erroneous code
} catch IOException as e {
    # only catches IOException, all other are left untouched
} catch as e {
    # fallback
}
```

#### 10.2.5 Behaviour

When exception is thrown, a RuntimeStriker (inherits `java.lang.Exception`) is issued. Try-catch blocks
neutralize RuntimeStrikers which are carrying exceptions.