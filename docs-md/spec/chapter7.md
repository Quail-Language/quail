# Quail Specification
[Back to index](index.md)

## Chapter 7: Memory and scopes

<!-- TOC -->
* [Quail Specification](#quail-specification)
  * [Chapter 7: Memory and scopes](#chapter-7-memory-and-scopes)
    * [7.1 Definitions](#71-definitions)
    * [7.2 Scope behaviour](#72-scope-behaviour)
<!-- TOC -->

### 7.1 Definitions

**<u>Scope</u>** is a table of variables and their values

**<u>Table</u>** is a (string) key => (Object) value structure

### 7.2 Scope behaviour

Scope is not just a wrapper around table. Scopes can nest.

When runtime is started, the root scope is created.

When other scopes are created, they are nested in current scope:

```
+-- root scope ------------+
|                          |
|  function, variables     |
|                          |
|  +-- function scope --+  |
|  | arguments          |  |
|  +-nearest------------+  |
|                          |
+-farthest-----------------+
```

When you try to access a variable, Quail searches from nearest to farthest scope:
```
accessing a:

root scope:
    a = 2    <- ignored
    inner scope 1:
        a = 4    <-------------------+
        inner scope 2:               |
            b = 4                    |
            accessing a from here ->-+
```
When Quail has ascended to the farthest scope, and still has not found variable, null is returned:
```
accessing c:

root scope:
    a = 2   
    nothing found -> return null <---+
    inner scope 1:                   |
        a = 4                        |
        inner scope 2:               |
            b = 4                    |
            accessing a from here ->-+
```
When you try to set a variable, nearest scope with its definition is affected. If variable is not defined anywhere, 
then nearest scope affected:
```
setting a to 5:

root scope:
    a = 2    <- ignored
    inner scope 1:
        a = 4    <-------------------+
        inner scope 2:               |
            b = 4                    |
            setting a from here   ->-+
```
```
setting c to 4:

root scope:                          +---------+ 
    a = 2                            ^         |
    nothing found -> return to nearest <---+   |
    inner scope 1:                         |   |
        a = 4                              |   |
        inner scope 2:                     |   |
            b = 4                          |   |
            setting a from here         ->-+   |
            c = 4   <--------------------------+
```

