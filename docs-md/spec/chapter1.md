# Quail Specification
[Back to index](index.md)

## Chapter 1: A Brief Introduction

<!-- TOC -->
* [Quail Specification](#quail-specification)
  * [Chapter 1: A Brief Introduction](#chapter-1-a-brief-introduction)
    * [1.1 Specification organisation](#11-specification-organisation-)
    * [1.2 Example programs](#12-example-programs)
    * [1.3 Contact info](#13-contact-info)
<!-- TOC -->

The Quail is a general-purpose, multiparadigmal, object-oriented, interpretable programming language driven by Java 8, developed by single developer and oriented to be simple and friendly for new developers as well as for experienced programmers moving from other language.

### 1.1 Specification organisation 

Specification is split into chapters:

1. A brief introduction
   Contains general information, organization of specification, couple of "Hello, World!" programs and contact info.

2. Grammar and execution flow
   Contains grammar definition arranged in block-schemas.
   Describes execution flow.

3. Preprocessing
   Describes the work of preprocessor.

4. Lexical analysis
   Describes the work of lexical analyser.

5. Parsing
   Describes type of nodes and the work of parser.

6. Typing and classes
   Describes Quail's type and OOP system

7. Memory system
   Describes how memory and scopes in Quail work.

8. Statement executions
   Describes how statements and expressions are executed in Quail.

9. External Libraries
    Describes how external libraries in Quail are imported.

10. Exceptions and errors
    Describes how exceptions and errors are raised and handled.

11. Standard library
    Describes Quail's standard library

12. Console operation
    Describes how you should operate Quail in terminal

### 1.2 Example programs

Hello, World!:

```lua
print("Hello, World!")
```

Looping factorial:

```lua
function fact(x) {
    n = 1
    through 1:+x as i
        n *= i
    return n
}

print(fact(5))
```

Recursive factorial:

```lua
function fact(x) {
    if x >= 1
        return x * fact(x - 1)
    else
        return 1
}

print(fact(5))
```

One-liner factorial:

```lua
use "lang/math" = math

function fact(x) math.product(1:+x)

print(fact(5))
```

### 1.3 Contact info

For reporting any bugs please use [Issues · Quail-Language/quail · GitHub](https://github.com/Quail-Language/quail/issues)

If you want to ask a question, write to our [Discord server](https://discord.gg/8smQAa8whM), or to email: `dev.quail.lang@gmail.com`
