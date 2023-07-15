# Quail Specification
[Back to index](index.md)

## Chapter 4: Lexical analysis

### 4.1 Definition

Lexical analysis is a process which splits textual code into a stream of tokens.

Token is a unit that represents a single keyword, value, symbol, etc.

#### 4.2 Defined tokens

Quail lexer defines following token types:

- Control Flow:
  
  - CONTROL_THROUGH
  
  - CONTROL_IF
  
  - CONTROL_ELSEIF
  
  - CONTROL_ELSE
  
  - CONTROL_TRY
  
  - CONTROL_CATCH
  
  - CONTROL_WHILE
  
  - CONTROL_LOOP
  
  - CONTROL_STOP_WHEN
  
  - CONTROL_EVERY
  
  - CONTROL_FOR

- Types:
  
  - TYPE_FUNC
  
  - TYPE_STRING
  
  - TYPE_BOOL
  
  - TYPE_NUM
  
  - TYPE_DICT
  
  - TYPE_LIST
  
  - TYPE_OBJECT
  
  - TYPE_FUNCTION
  
  - TYPE_METHOD
  
  - TYPE_CLASS
  
  - TYPE_VOID

- Modifiers:
  
  - MOD_REQUIRE
  
  - MOD_ANYOF
  
  - MOD_LOCAL
  
  - MOD_FINAL
  
  - MOD_STATIC

- Effects and instructions:
  
  - EFFECT_ASSERT
  
  - EFFECT_USE
  
  - EFFECT_THROW
  
  - EFFECT_IMPORT
  
  - EFFECT_STRIKE
  
  - EFFECT_RETURN
  
  - INSTRUCTION_BREAK
  
  - INSTRUCTION_CONTINUE

- Operators:
  
  - PLUS
  
  - MINUS
  
  - MULTIPLY
  
  - DIVIDE
  
  - MODULO
  
  - INTDIV
  
  - POWER
  
  - ASSIGN
  
  - EQUALS
  
  - NOT_EQUALS
  
  - GREATER
  
  - LESS
  
  - GREATER_EQUAL
  
  - LESS_EQUAL
  
  - AND
  
  - OR
  
  - RANGE
  
  - RANGE_INCLUDE
  
  - SHORT_PLUS
  
  - SHORT_MINUS
  
  - SHORT_MULTIPLY
  
  - SHORT_DIVIDE
  
  - SHORT_MODULO
  
  - SHORT_INTDIV
  
  - SHORT_POWER
  
  - NOT
  
  - IN
  
  - INSTANCEOF

- Literals:
  
  - LITERAL_NULL
  
  - LITERAL_STR
  
  - LITERAL_NUM
  
  - LITERAL_FALSE
  
  - LITERAL_TRUE

- Misc Keywords:
  
  - CONSTRUCTOR
  
  - OVERRIDE
  
  - SETS
  
  - GETS
  
  - AS
  
  - ASYNC
  
  - LIKE

- Symbols:
  
  - LPAR
  
  - RPAR
  
  - LSPAR
  
  - RSPAR
  
  - LCPAR
  
  - RCPAR
  
  - CONSUMER
  
  - KWARG_CONSUMER
  
  - LAMBDA_ARROW
  
  - PILLAR
  
  - COMMA
  
  - DOT

- Non-printable symbols:
  
  - EOL
  
  - EOF
  
  - TAB

- For variables:
  
  - ID

And following token modifiers:

- SINGULAR_MOD

- ARRAY_MOD

- MATRIX_MOD

#### 4.3 Textual representation of tokens

##### 4.3.1 Keywords

`through` CONTROL_THROUGH

`if` CONTROL_IF

`elseif` CONTROL_ELSEIF

`else` CONTROL_ELSE

`try` CONTROL_TRY

`catch` CONTROL_CATCH

`while` CONTROL_WHILE

`loop` CONTROL_LOOP

`stop when` CONTROL_STOP_WHEN

`every` CONTROL_EVERY

`for` CONTROL_FOR

`func` TYPE_FUNC

`string` TYPE_STRING

`bool` TYPE_BOOL

`num` TYPE_NUM

`dict` TYPE_DICT

`list` TYPE_LIST

`object` TYPE_OBJECT

`function` TYPE_FUNCTION

`method` TYPE_METHOD

`class` TYPE_CLASS

`void` TYPE_VOID

`notnull` MOD_REQUIRE

`anyof` MOD_ANYOF

`local` MOD_LOCAL

`final` MOD_FINAL

`static` MOD_STATIC

`assert` EFFECT_ASSERT

`use` EFFECT_USE

`throw` EFFECT_THROW

`import` EFFECT_IMPORT

`strike` EFFECT_STRIKE

`return` EFFECT_RETURN

`break` INSTRUCTION_BREAK

`continue` INSTRUCTION_CONTINUE

`null` LITERAL_NULL

`true` LITERAL_TRUE

`false` LITERAL_FALSE

`constructor` CONSTRUCTOR

`override` OVERRIDE

`sets` SETS

`gets` GETS

`as` AS

`async` ASYNC

`like` LIKE

`in` IN

`instanceof` INSTANCEOF

`not` NOT

`and` AND

`or` OR

`has` `does` `with` `do` `then` LCPAR

`end` RCPAR

##### 4.3.2 Symbols

| Symbol  | Token type     |
| ------- | -------------- |
| `+`     | PLUS           |
| `-`     | MINUS          |
| `*`     | MULTIPLY       |
| `/`     | DIVIDE         |
| `//`    | INTDIV         |
| `%`     | MODULO         |
| `^`     | POWER          |
| `=`     | ASSIGN         |
| `==`    | EQUALS         |
| `!=`    | NOT_EQUAL      |
| `>`     | GREATER        |
| `<`     | LESS           |
| `>=`    | GREATER_EQUAL  |
| `<=`    | LESS_EQUAL     |
| `&&`    | AND            |
| `\|     | `              |
| `\|`    | PILLAR         |
| `:`     | RANGE          |
| `:+`    | RANGE_INCLUDE  |
| `+=`    | SHORT_PLUS     |
| `-=`    | SHORT_MINUS    |
| `*=`    | SHORT_MULTIPLY |
| `/=`    | SHORT_DIVIDE   |
| `%=`    | SHORT_MODULO   |
| `//=`   | SHORT_INTDIV   |
| `^=`    | SHORT_POWER    |
| `!`     | NOT            |
| `...`   | CONSUMER       |
| `{...}` | KWARG_CONSUMER |
| `->`    | LAMBDA_ARROW   |
| `,`     | COMMA          |
| `.`     | DOT            |
| `(`     | LPAR           |
| `)`     | RPAR           |
| `[`     | LSPAR          |
| `]`     | RSPAR          |
| `{`     | LCPAR          |
| `}`     | RCPAR          |

#### 4.4 Lexing process

This part contains pseudocode that represents the lexing process

> `@` - means that it is a global variable

Class Token has:

- modifier

- token type

- string lexeme

- int length

- int character

- int line

Token modifiers:

- SINGULAR_MOD

- ARRAY_MOD

- MATRIX_MOD

Method `advance`

```
increase @pos by 1
```

Method `next`

```
return current char and advance
```

Method `match(char expected)`

```
if reached end
    return false
if current char != expected
    return false
else
    advance
    return true
```

Method `match(String expected)`

```
if reached end
    return false
if substring of @sourceCode from @pos starts with expected
    increase pos by length of expected
    return true
else
    return false
```

Method `peek`

```
if reached end
    return null
else
    return current char
```

Method `peek next`

```
if one more available
    return next char
else
    return null
```

Method `add binary op(op)`

```
if match "="
    add short operator op
else
    add token op
```

Method `scan`

```
while not reached end
    @start = @current
    scan token
return @tokens
```

Method `scan token`

```
ce = next
if c is ...
    "(" then
        add token LPAR

    ")" then
        add token RPAR

    "{" then
        if match "...}" then
            add token KWARG_CONSUMER
        else
            scan curly brace

    "}" then
        add token RCPAR

    "[" then
        scan bracket

    "]" then
        add token RSPAR

    "," then
        add token COMMA

    "." then
        if match ".." then
            add token CONSUMER
        else
            add token DOT

    "-" then
        if match ">" then
            add token LAMBDA_ARROW
        else
            add token MINUS
    
    "+" then
        add token PLUS

    "/" then
        if match "/" then
            add token INTDIV
        else
            add token DIVIDE
    
    "*" then
        add token MULTIPLY
    
    "%" then
        add token MODULO
    
    "^" then
        add token POWER
    
    ">" then
        if match "=" then
            add token GREATER_EQUAL
        else
            add token GREATER
    
    "<" then
        if match "=" then
            add token LESS_EQUAL
        else
            add token LESS
    
    "=" then
        if match "=" then
            add token EQUALS
        else
            add token ASSIGN
    
    "!" then
        if match "=" then
            add token NOT_EQUALS
        else
            add token NOT
    
    "&" then
        if match "&" then
            add token AND
        else
            error
    
    "|" then
        if match "|" then
            add token OR
        else
            add token PILLAR
    
    ":" then
        if match "+" then
            add token RANGE_INCLUDE
        else
            add token RANGE
    
    "#" then
        scan comment

    "\n" (next line symbol) then
        add token EOL
        increase @line by 1
        @startOfCurrentLine = @pos

    " then
        scan string
    
    " " then
        if match "   "
            add token TAB
    
    "\r" then ignore
    
    "\t" (tab symbol) then
        add token TAB
else
    if c is digit then
        scan number
    else if c is alphabetical then
        scan id
    else
        error  
```
