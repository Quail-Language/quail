# Quail Specification
[Back to index](index.md)

<!-- TOC -->
* [Quail Specification](#quail-specification)
  * [Chapter 13: Documentation](#chapter-13-documentation)
    * [13.1 Definitions](#131-definitions)
    * [13.2 Documentation syntax](#132-documentation-syntax)
      * [13.2.1 TOC Entry](#1321-toc-entry)
      * [13.2.2 Author](#1322-author)
      * [13.2.3 Badge](#1323-badge)
      * [13.2.4 HTML](#1324-html)
      * [13.2.5 Since](#1325-since)
      * [13.2.6 DocString](#1326-docstring)
    * [13.3 Classes and functions representation](#133-classes-and-functions-representation)
    * [13.3 Generating documentation](#133-generating-documentation)
<!-- TOC -->

## Chapter 13: Documentation

### 13.1 Definitions

**<u>TOC</u>** stands for Table of Contents. It lists all documentation entries

### 13.2 Documentation syntax

#### 13.2.1 TOC Entry

Syntax: `#?toc-entry <entryName>`

Generates a TOC entry with provided name that links to HTML `#<entryName>` id

Example: `#?toc-entry MyClass`

#### 13.2.2 Author

Syntax: `#?author <author>`

Adds author description. Multiple authors allowed

Example: `#?author Tapeline`

#### 13.2.3 Badge

Syntax: `#?badge <badge-title>` or `#?badge-<badge-color> <badge-title>`

Adds a badge with chosen color. 

Available colors:
- default
- red
- yellow
- green

If color is not provided, it defaults.

Examples:
```
#?badge For testing purposes
#?badge-yellow Deprecated
```

#### 13.2.4 HTML

Syntax: `#?html <html>`

Adds raw html

Example: `#?html <h1>Overview</h1>`

#### 13.2.5 Since

Syntax: `#?since <since>`

Add since badge. Only one per context allowed.

Example: `#?since 0.1-alpha.2`

#### 13.2.6 DocString

Syntax `#? <text>`

Adds a textual description

Example: `#? Checks if given key is present`

### 13.3 Classes and functions representation

Classes and functions are automatically documented.

Each class and function documentation entry is labeled with HTML `id=`.

Example:
```
class A {}         # Will be id=#A
class B {          # Will be id=#B
  function c() {}  # Will be id=#A::c
}
function d() {}    # Will be id=#d
```

### 13.3 Generating documentation

You can generate documentation using `gendoc` mode:

```
java -jar quail.jar gendoc fileToDocument.q > outputFile.html
```

Additional flags for mode available:
- `documentationHeadline` Headline in TOC. Example:
  `java -jar quail.jar -sF.documentationHeadline="My Documentation" gendoc fileToDocument.q > outputFile.html`