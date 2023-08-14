# Quail Specification
[Back to index](index.md)

## Chapter 3: Preprocessing

<!-- TOC -->
* [Quail Specification](#quail-specification)
  * [Chapter 3: Preprocessing](#chapter-3-preprocessing)
    * [3.1 Definitions](#31-definitions)
    * [3.2 Directives](#32-directives)
      * [3.2.1 Alias](#321-alias)
      * [3.2.3 Include](#323-include)
    * [3.2 Preprocessor overview](#32-preprocessor-overview)
<!-- TOC -->

### 3.1 Definitions

**<u>Preprocessor</u>** is a program that transforms textual representation of program code by given rules. Preprocessing comes before lexical analysing.

**<u>Preprocessor directive</u>** is a textual representation of preprocessor rule.

**<u>Directive argument</u>** is a parameter that modifies the directive's behaviour.

### 3.2 Directives

Directive consists of its name and arguments. One directive takes only one line. If you wish to use wrap your directive to next line, you should place `\` backslash between lines as shown:

```
#:alias "HELLO" \
print("Hello")

#:alias "OTHERHELLO" \
print("Hello") \
print("Hello again")
```

General form of a directive look like this:

```
#:directive_name argument1 argument2 argument3 ... argumentN
```

Directives can accept following types of arguments:

- string `"this is a quoted string" this_an_unquoted_string_i_cannot_use_whitespaces_here`

- integer `3` `4000`

- boolean `true` `false` `0` `1` `YES` `NO` `ENABLE` `DISABLE` (case-insensetive)

- code (it's like a string, but it can contain whitespaces without necessity to quote. But `code` argument can be only the last argument and there cannot be more than one of them in one directive)

Quail preprocessor defines grammar for next directives:

#### 3.2.1 Alias

Name: `alias`, `define`

Arguments: `string regex` `code replacement`

Aliasing directive. Replaces all regex occurrences with given replacement. Regex capture groups also work. Does not replace content of strings.

#### 3.2.3 Include

Name: `include`

Arguments: `string path`

Includes all contents of given file as a plain text at the top of the file

### 3.2 Preprocessor overview

Preprocessing consists of several steps:

1. Resolving
   
   - Filter directive strings
   
   - Resolving string boundaries
   
   - Parsing preprocessor directives

2. Processing
   
   - Executing directives

This process is repeated until resolving gives 0 resolved directives. Then the preprocessor gives out the result.
