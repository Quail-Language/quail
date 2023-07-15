# Quail Specification
[Back to index](index.md)

## Chapter 12: Console operation

<!-- TOC -->
* [Quail Specification](#quail-specification)
  * [Chapter 12: Console operation](#chapter-12-console-operation)
    * [12.1 Running Quail from console (simple)](#121-running-quail-from-console-simple)
    * [12.2 Global flags](#122-global-flags)
      * [12.2.1 Global flags list](#1221-global-flags-list)
      * [12.2.2 Global flags syntax](#1222-global-flags-syntax)
    * [12.3 Custom flags](#123-custom-flags)
      * [12.3.1 Custom flags values types](#1231-custom-flags-values-types)
      * [12.3.2 Custom flags syntax](#1232-custom-flags-syntax)
    * [12.4 General form of Quail start command](#124-general-form-of-quail-start-command)
<!-- TOC -->

### 12.1 Running Quail from console (simple)
To launch Quail script, simply run following command:
```
java -jar yourQuailJarName.jar run yourQuailProgram.q
```
E.g.:
```
project
|-- quail.jar
|-- main.q

java -jar quail.jar run main.q
```

### 12.2 Global flags

Global flags can modify behaviour of Quail

#### 12.2.1 Global flags list
* `encoding` (default: `UTF-8`) - default encoding for files

#### 12.2.2 Global flags syntax
Global flags are passed via following syntax:
`-G.globalFlagName=value`

E.g.: `java -jar quail.jar -G.encoding="cp1251" run main.q`

### 12.3 Custom flags

#### 12.3.1 Custom flags values types
* i - integer
* b - boolean
* s - string
* d - double

#### 12.3.2 Custom flags syntax
Custom flags are passed via following syntax:
`-tF.customFlagName=value`, where `t` is value type

E.g.: `java -jar quail.jar -bF.ignoreModifiers=true run main.q`

### 12.4 General form of Quail start command
```
java -jar <path to quail jar package> [global and custom flags] run <path to target script>
```

E.g.: `java -jar quail.jar -G.encoding="cp1251" -bF.ignoreModifiers=true run main.q`

E.g.: `java -jar quail.jar run main.q`