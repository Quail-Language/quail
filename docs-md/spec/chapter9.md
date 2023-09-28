# Quail Specification
[Back to index](index.md)

## Chapter 9: External libraries

<!-- TOC -->
* [Quail Specification](#quail-specification)
  * [Chapter 9: External libraries](#chapter-9-external-libraries)
    * [9.1 Definitions](#91-definitions)
    * [9.2 Library creation](#92-library-creation)
    * [9.3 Library loading](#93-library-loading)
      * [9.3.1 Syntax](#931-syntax)
      * [9.3.2 Default locations](#932-default-locations)
      * [9.3.3 Behaviour](#933-behaviour)
<!-- TOC -->

### 9.1 Definitions

**<u>Library</u>** is a Quail program that could be accessed from other Quail programs

### 9.2 Library creation

Any Quail program `(*.q)` can be used as a library if it returns a not-null value.

Example:

`library.q`:
```
function add(a, b) return a + b
return {
  "add" = add
}
```
`main.q`:
```
use "library.q" = lib
print(lib.add(3, 7))
```

### 9.3 Library loading

#### 9.3.1 Syntax

Library is loaded with following syntax:

```
use "library/path/to/file.q" = variableToPlaceLibraryIn
```

#### 9.3.2 Default locations

Quail searches for requested libraries in these locations by default:
- `$cwd$/?`
- `$script$/?`

Available wildcards:

| Wildcard   | Replacement                                           |
|------------|-------------------------------------------------------|
| `$cwd$`    | Replaced with ENV `user.dir`                          |
| `$script$` | Replaced with parent directory of file of main script |
| `?`        | Replaced with the path of requested library           |

#### 9.3.3 Behaviour

When you request a library, the following gets executed:
1. Library cache is being searched for already loaded library with that path.
   
   If found:
   
   1. Library requested from cache
   
   If not found:

   1. Quail searches for requested file, using wildcards (9.3.2)
   
      If not found - error raised, algorithm interrupted
   
   2. Quail ensures that this file is not currently loading from somewhere else
   in case of circular imports
   
      If circular import is detected - error raised, algorithm interrupted
   
   3. File is being loaded and executed in new, separate runtime
   
   4. The return value of the loaded library is placed into cache and returned to
   provided variable
