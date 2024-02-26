# Quail Specification

[Back to index](index.md)

## Chapter 2: Grammar and execution flow

<!-- TOC -->
* [Quail Specification](#quail-specification)
  * [Chapter 2: Grammar and execution flow](#chapter-2-grammar-and-execution-flow)
    * [2.1 Definitions](#21-definitions)
    * [2.2 Execution flow](#22-execution-flow)
    * [2.3 Syntax](#23-syntax)
      * [2.3.1 Comments](#231-comments)
      * [2.3.2 Operators](#232-operators)
        * [2.3.2.1 Table of operators](#2321-table-of-operators)
        * [2.3.2.2 Operator shortens](#2322-operator-shortens)
      * [2.3.3 Control flow constructions](#233-control-flow-constructions)
        * [2.3.3.1 `if-else if-else`](#2331-if-else-if-else)
        * [2.3.3.2 `through ... as ...`](#2332-through--as-)
        * [2.3.3.3 `while`](#2333-while)
        * [2.3.3.4 `loop-stop when`](#2334-loop-stop-when)
        * [2.3.3.5 `for ... in ...`](#2335-for--in-)
        * [2.3.3.6 `try-catch`](#2336-try-catch)
      * [2.3.4 Instructions](#234-instructions)
        * [2.3.4.1 `break`](#2341-break)
      * [2.3.5 Effects](#235-effects)
        * [2.3.5.1 `assert`](#2351-assert)
        * [2.3.5.2 `strike`](#2352-strike)
        * [2.3.5.3 `import`](#2353-import)
        * [2.3.5.4 `use`](#2354-use)
        * [2.3.5.6 `return`](#2356-return)
        * [2.3.5.7 `throw`](#2357-throw)
        * [2.3.5.8 `async`](#2358-async)
      * [2.3.6 Variable modifiers](#236-variable-modifiers)
        * [2.3.6.1 List of modifiers](#2361-list-of-modifiers)
        * [2.3.6.2 Type conversion / Typecast](#2362-type-conversion--typecast)
        * [2.3.6.2 Variable modifying](#2362-variable-modifying)
        * [2.3.6.3 Argument modifying](#2363-argument-modifying)
        * [2.3.6.4 Defining functions](#2364-defining-functions)
      * [2.3.7 Data constructions](#237-data-constructions)
        * [2.3.7.1 Functions](#2371-functions)
        * [2.3.7.2 Special functions](#2372-special-functions)
        * [2.3.7.3 Classes](#2373-classes)
      * [2.3.8 Values](#238-values)
        * [2.3.8.1 Number](#2381-number)
        * [2.3.8.2 Boolean](#2382-boolean)
        * [2.3.8.3 String](#2383-string)
        * [2.3.8.4 Null](#2384-null)
        * [2.3.8.5 List](#2385-list)
        * [2.3.8.6 Dict](#2386-dict)
        * [2.3.8.6 Lambda (anonymous functions)](#2386-lambda-anonymous-functions)
      * [2.3.9 Generators](#239-generators)
        * [2.3.9.1 List generators](#2391-list-generators)
        * [2.3.9.2 Dict generators](#2392-dict-generators)
      * [2.3.10 Expressions](#2310-expressions)
        * [2.3.10.1 Call](#23101-call)
        * [2.3.10.2 Field call](#23102-field-call)
        * [2.3.10.3 Field reference](#23103-field-reference)
        * [2.3.10.4 Field assign](#23104-field-assign)
        * [2.3.10.5 Indexing](#23105-indexing)
        * [2.3.10.6 Indexing assign](#23106-indexing-assign)
        * [2.3.10.7 Range](#23107-range)
        * [2.3.10.8 Subscript](#23108-subscript)
      * [2.3.11 Sections (blocks)](#2311-sections-blocks)
<!-- TOC -->

### 2.1 Definitions

This chapter only describes syntax. For semantics go to Chapter 8.

expr - stands for expression

In this chapter `<...>` is a placeholder for necessary value and `<[...]>` 
for unnecessary.

Things in `[]` are usually not required

`<name:type>` is just a named group like `<...>`

`stmt` stands for Statement.

```
{
Code
}
```

is a single statement

### 2.2 Execution flow

When you run Quail program, interpreter follows the following execution flow:

1. Preprocessing [#Chapter 3](chapter3.md)

2. Lexical analysis [#Chapter 4](chapter4.md)

3. Parsing [#Part 2.3](#23-syntax) [#Chapter 5](chapter5.md)

4. Running [#Chapter 6](chapter6.md) [#Chapter 7](chapter7.md) [#Chapter 8](chapter8.md)

### 2.3 Syntax

#### 2.3.1 Comments

`#` denotes a comment

---

`#:` denotes a preprocessor directive

Examples: (see [Chapter 3](chapter3.md))

- `#:include "file.q"`
- `#:alias "HELLO" print("Hello")`

---

`#?` denotes a doc

Examples: (see [Chapter 13](chapter13.md))

- `#? This is a description`
- `#?author Tapeline`

#### 2.3.2 Operators

##### 2.3.2.1 Table of operators

| Operator     | Meaning                                                 | Example                |
| ------------ | ------------------------------------------------------- | ---------------------- |
| `+`          | Addition                                                | `1 + 2`                |
| `-`          | Subtraction                                             | `1 - 2`                |
| `*`          | Multiplication                                          | `1 * 2`                |
| `/`          | Division                                                | `1 / 2`                |
| `//`         | Integer division (without remainder and floating point) | `1 // 2`               |
| `%`          | Remainder of division                                   | `1 % 2`                |
| `^`          | Power                                                   | `3 ^ 2`                |
| `=`          | Assignment                                              | `a = 2`                |
| `==`         | Equals                                                  | `1 == 2`               |
| `!=`         | Not equals                                              | `1 != 2`               |
| `!` , `not`  | Logical bool                                            | `!true`, `not true`    |
| `&&`         | Locigal and                                             | `true && true`         |
| `\||`        | Logical or                                              | `true \|| false`       |
| `<`          | Is less than                                            | `1 < 2`                |
| `>`          | Is greater than                                         | `1 > 2`                |
| `<=`         | Is less or equal                                        | `1 <= 2`               |
| `>=`         | Is greater or equal                                     | `1 >= 2`               |
| `:`          | Range operator                                          | `1:5`, `1:10:2`        |
| `:+`         | Range-include operator                                  | `1:+5`, `1+:10:2`      |
| `<<`         | Shift left                                              | `[1, 2, 3] << 1`       |
| `>>`         | Shift right                                             | `[1, 2, 3] >> 1`       |
| `in`         | Is element in                                           | `1 in [1, 2, 3]`       |
| `not in`     | is element absent                                       | `1 not in [1, 2, 3]`   |
| `instanceof` | Check if object is instace of class                     | `a instanceof MyClass` |

##### 2.3.2.2 Operator shortens

Operators `+`, `-`, `*`, `/`, `//`, `%` and `^` support shortens like this:

```
a += b
```

With a general form of

```
<expr1> operator= <expr2>
```

which is basically a syntax sugar for

```
<expr1> = <expr1> operator <expr2>
```

#### 2.3.3 Control flow constructions

##### 2.3.3.1 `if-else if-else`

Runs code basing on conditions

```
if <condition>  <stmt>
```

```
if <condition>  <stmt>
else  <stmt>
```

```
if <condition>  <stmt>
else if <condition>  <stmt> ...
```

```
if <condition>  <stmt>
else if <condition>  <stmt> 
else  <stmt>
```

Examples:

```
if a == b f()
```

```
if a == b {
  f()
}
```

```
if a == b f()
else f1()
```

```
if a == b {
  f()
} else {
  f1()
}
```

```
if a == b {
  f()
} else if a == c {
  f1()
}
```

```
if a == b {
  f()
} else if a == c {
  f1()
} else {
  f2()
}
```

##### 2.3.3.2 `through ... as ...`

Iterates over a number range

```
through <range> as <variable>  <stmt>
```

Supports all range variations.

Examples:

```
through 1:10 as i print(i)
```

```
through 1:+10 as i print(i)
```

```
through 1:10:2 as i print(i)
```

```
through 10:1 as i print(i)
```

##### 2.3.3.3 `while`

Iterates while condition is true

```
while <condition>  <stmt>
```

Examples:

```
while i > 2 {
  i /= 3
}
```

##### 2.3.3.4 `loop-stop when`

Iterates while condition is false.

Checks condition after iteration (like do-while)

```
loop <stmt> stop when <condition>
```

Examples:

```
loop {
  cmd = input()
} stop when cmd == "exit"
```

##### 2.3.3.5 `for ... in ...`

Iterates over a collection (list, string, dict...)

```
for <variable> in <collection>  <stmt>
```

```
every <variable> in <collection>  <stmt>
```

Example:

```
for student in students {
  print(student.name)
}
```

##### 2.3.3.6 `try-catch`

Tries to execute code. If an exception is thrown, suppresses it and proceeds
to corresponding catch block. If there is no applicable catch block, rethrows
the exception. If there are no catch blocks at all, the exception is always
suppressed

```
try  <stmt>
```

```
try  <stmt>
catch as <variable>  <stmt>
```

(Catches all)

```
try  <stmt>
catch <expr: exception class> as <variable>  <stmt>
```

(Catches exceptions that are instanceof given class)

Examples:

```
try {
  f()
}
```

```
try {
  a[i] = b
} catch as e {
  print(e)
}
```

```
try {
  a[i] = b
} catch IndexOutOfBoundsException as e {
  print(e)
}  
```

#### 2.3.4 Instructions

Instructions are one-word control words.

##### 2.3.4.1 `break`

Quits current loop

```
break
```

Example

```
while true {
    cmd = input()
    if cmd == "quit" 
        break
}
```



#### 2.3.5 Effects

Effects are one-line constructions with general form: `<effect> <value>`

##### 2.3.5.1 `assert`

Throws assertion exception when value is not true

```
assert <expr>
```

Example

```
assert value != null
```

##### 2.3.5.2 `strike`

Quits multiple nested loops (like break)

```
strike <expr>
```

Example

```
through 0:+10 as x {
    through 0:+10 as y {
        through 0:+10 as z {
            strike 2
        }    
    }
}
```

##### 2.3.5.3 `import`

Get code from specified file and execute it in this runtime

```
import <expr>
```

Example

```
import "common.q"
```

##### 2.3.5.4 `use`

Import a library and place it in some variable

```
use <expr> = <variable>
```

Example

```
use "lang/reflect" = reflect
```

##### 2.3.5.6 `return`

Return value from function or a file

```
return
```

```
return <expr>
```

Examples:

```
function f(x) {
    if x == null return
    return x.a
}
```

##### 2.3.5.7 `throw`

Throw an exception

```
throw <expr>
```

Examples:

```
throw "Error!"
```

```
throw Exception() % {"message"="Error!"}
```

##### 2.3.5.8 `async`

Run node in parallel

``` 
async <anything>
```

Examples:

```
async while true {
  print("Hi!")
}
```

#### 2.3.6 Variable modifiers

##### 2.3.6.1 List of modifiers

Available modifiers are:

```
func
string
bool
num 
dict
list
class
void

required
local
final
static
```

First group is called type modifiers, the second - behaviour modifiers

##### 2.3.6.2 Type conversion / Typecast

Types `string`, `num`, `bool` support conversion.

```
<modifier>(<expr>)
```

Examples:

```
a = num(input())
b = string([1, 2, 3])
c = bool(1)
```

##### 2.3.6.2 Variable modifying

You can modify variable on assignment using following syntax instead of just `<variable>`:

```
<modifier group> <variable>
```

```
<modifier group 1> | <modifier group 2> | ... <variable>
```

Modifier group is one or more modifiers split by space.

Notice! Behaviour modifiers should always go only into first modifier group!

Examples:

```
string name = ""
num | void result = null
local final num MY_CONSTANT = 3.1415
```

##### 2.3.6.3 Argument modifying

Same rules as in 2.3.6.2 apply

##### 2.3.6.4 Defining functions

Functions can be defined the same way as written in 2.3.7.1, but instead of `function` and `method` keywords you can write a type moidifier:

```
num sqrt(num x) {
    return x ^ 0.5
}
```

This syntax will not affect anything, but it is helpful for developing.

#### 2.3.7 Data constructions

##### 2.3.7.1 Functions

```
function <variable: name>([<variable: argument>, ...])  <stmt>
```

```
method <variable: name>([<variable: argument>, ...])  <stmt>
```

Examples:

```
function f() {}
```

##### 2.3.7.2 Special functions

```
constructor ([<variable: argument>, ...])  <stmt>
```
Syntax sugar for `function _constructor ...`

```
gets <variable>(this)  <stmt>
```
Syntax sugar for `function _get_<variable> ...`

```
sets <variable>(this, value)  <stmt>
```
Syntax sugar for `function _set_<variable> ...`

```
override <operator>([<variable: argument>, ...])  <stmt>
```
Syntax sugar for `function operator ...`, where operator is defined
using following table:

| Operator char | Function name |
|---------------|---------------|
| `+`           | `_add`        |
| `-`           | `_sub`        |
| `*`           | `_mul`        |
| `/`           | `_div`        |
| `//`          | `_intdiv`     |
| `%`           | `_mod`        |
| `^`           | `_pow`        |
| `==`          | `_eq`         |
| `!=`          | `_neq`        |
| `<`           | `_cmpl`       |
| `>`           | `_cmpg`       |
| `<=`          | `_cmple`      |
| `>=`          | `_cmpge`      |
| `!`           | `_not`        |
| `<<`          | `_shl`        |
| `>>`          | `_shr`        |

```
override <type>(this)  <stmt>
```
Syntax sugar for `function type ...`, where type is defined
using following table:

| Type modifier | Function name |
|---------------|---------------|
| `string`      | `_tostring`   |
| `bool`        | `_tobool`     |
| `num`         | `_tonumber`   |


Examples:

```
constructor (this, name) {
    this.name = name
}
```

```
override +(this, other) {
    return this.val + other.val
}
```

```
override string(this) {
    return string(this.val)
}
```

```
gets myProperty(this) {
    return this.calculateProperty()
}
```

##### 2.3.7.3 Classes

```
class <variable: name> [like <expr>]  <stmt>
```

Examples:

```
class Student {
    string name

    constructor (this, name) {
        this.name = name
    }
}
```

```
class MyException like Exception {
    constructor (this, message) {
        this.message = message
    }
}
```

#### 2.3.8 Values

Values are considered expressions

##### 2.3.8.1 Number

```
<digits>[.<digits>]
```

Examples:

```
132
```

```
123.3231
```

##### 2.3.8.2 Boolean

```
true
```

```
false
```

##### 2.3.8.3 String

```
"<content>"
```

Escape sequences supported: `\\`, `\n`, `\r`, `\t`

##### 2.3.8.4 Null

```
null
```

##### 2.3.8.5 List

```
[<expr>, <expr>...]
```

Examples:

```
[]
```

```
[234]
```

```
[231, 3123, 4343]
```

##### 2.3.8.6 Dict

```
{<expr>=<expr>, ...}
```

Examples:

```
{}
```

```
{"a" = 1}
```

```
{"a" = 1, "b" = 2}
```

##### 2.3.8.6 Lambda (anonymous functions)

```
function (<variable: argument>, ...)  <stmt>
```

```
(<variable: argument>, ...) -> <stmt>
```

Examples:

```
f = function (a, b) return a + b
```

```
f = (a, b) -> return a + b
```

#### 2.3.9 Generators

Generators are also considered expressions

##### 2.3.9.1 List generators

```
[<expr> for <variable> in <expr>]
```

```
[<expr> for <variable> in <expr> if <condition>]
```

```
[<expr> every <variable> in <expr>]
```

```
[<expr> every <variable> in <expr> if <condition>]
```

```
[<expr> through <range> as <variable>]
```

```
[<expr> through <range> as <variable> if <condition>]
```

Examples:

```
[x^2 for x in 1:+10]
```

``` 
[x^2 for x in 1:+10 if x % 2 == 0]
```

```
[x^2 through 1:+10 as x if x % 2 == 0]
```

##### 2.3.9.2 Dict generators

```
{<key: expr> = <value: expr> for <variable> in <expr>}
```

```
{<key: expr> = <value: expr> for <variable> in <expr> if <condition>}
```

```
{<key: expr> = <value: expr> every <variable> in <expr>}
```

```
{<key: expr> = <value: expr> every <variable> in <expr> if <condition>}
```

```
{<key: expr> = <value: expr> through <range> as <variable>}
```

```
{<key: expr> = <value: expr> through <range> as <variable> if <condition>}
```

Examples:

```
{x = x^2 for x in 1:+10}
```

``` 
{x = x^2 for x in 1:+10 if x % 2 == 0}
```

```
{x = x^2 through 1:+10 as x if x % 2 == 0}
```

#### 2.3.10 Expressions

##### 2.3.10.1 Call

```
<expr>()
```

```
<expr>(<arg1>, <arg2>, ...)
```

``` 
<expr>(<arg1>, <arg2>, <kwarg name: variable>=<expr>, ...)
```

Example

```
print()
```

```
print("Hello!")
```

```
input(prompt="input a message> ")
```

##### 2.3.10.2 Field call

```
<expr>.<name: variable>()
```

```
<expr>.<name: variable>(<arg1>, <arg2>, ...)
```

``` 
<expr>.<name: variable>(<arg1>, <arg2>, <kwarg name: variable>=<expr>, ...)
```

Examples:

```
thread.result()
```

##### 2.3.10.3 Field reference

```
<expr>.<name: variable>
```

Example:

```
math.pi
```

##### 2.3.10.4 Field assign

```
<expr>.<name: variable>
```

Example:

```
myObj.field = value
```

##### 2.3.10.5 Indexing

```
<expr>[<index: expr>]
```

Example:

```
a[0]
```

##### 2.3.10.6 Indexing assign

```
<expr>[<index: expr>] = <expr>
```

Example:

```
a[0] = 4
```

##### 2.3.10.7 Range

``` 
<start:expr> : <end:expr>
```

``` 
<start:expr> :+ <end:expr>
```

``` 
<start:expr> : <end:expr> : <step:expr>
```

``` 
<start:expr> :+ <end:expr> : <step:expr>
```

Examples:

```
1:10
```

```
1:+10
```

```
1:+10:2
```

##### 2.3.10.8 Subscript

```
<expr>[<range*>]
```

range* - is a usual range, but start and end can be omitted like this:
`a[:10]`, `a[::-1]`, `a[1:]`, `a[1::4]`

Examples:

```
a[1:10]
```

```
a[::-1]
```

#### 2.3.11 Sections (blocks)

Section (or code block) is a statement that contains multiple statements that
are executed one-by-one.

Section can be defined in 3 different ways:

1. Using `{` - `}`:
   ```
   if a == b {
      foo()
      bar()
   }
   ```
2. Using `do|does|has|with|then` - `end`:
   ```
   if a == b then
      foo()
      bar()
   end
   
   function foo() do
      ...
   end
   
   function bar() does
      ...
   end
   
   class MyClass has
      string field
      
      override string(this) with
          return this.field
      end
   end
   ```
   `do|does|has|with|then` are interchangeable
3. Using `:`:
   ```
   if a == b:
      foo()
      bar()
   ```
   In this case indentation matters!

All 3 different styles can be combined (but this is not recommended due to bad
readability):
```
while true {
    for i in 1:10 do
        if i % 2 == 0:
            print(i)
    end
}
```
