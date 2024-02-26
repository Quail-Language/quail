# Quail Specification
[Back to index](index.md)

## Chapter 8: Statement executions

<!-- TOC -->
* [Quail Specification](#quail-specification)
  * [Chapter 8: Statement executions](#chapter-8-statement-executions)
    * [8.1 Common definitions](#81-common-definitions)
    * [8.2 Runtime execution](#82-runtime-execution)
      * [8.2.1 Effects](#821-effects)
          * [8.2.1.1 AsyncNode (`async` effect)](#8211-asyncnode-async-effect)
          * [8.2.1.2 EffectNode (any of the effects, except `return` and `use`)](#8212-effectnode-any-of-the-effects-except-return-and-use)
          * [8.2.1.3 InstructionNode (any instruction)](#8213-instructionnode-any-instruction)
          * [8.2.1.4 ReturnNode (`return` effect)](#8214-returnnode-return-effect)
          * [8.2.1.5 UseNode (`use` effect)](#8215-usenode-use-effect)
      * [8.2.2 Expression](#822-expression)
          * [8.2.2.1 AssignNode (assignment operator)](#8221-assignnode-assignment-operator)
          * [8.2.2.2 BinaryOperatorNode (any of `a <operator> b` operators)](#8222-binaryoperatornode-any-of-a-operator-b-operators)
          * [8.2.2.3 CallNode (any call)](#8223-callnode-any-call)
          * [8.2.2.4 FieldReferenceNode (`object.field`)](#8224-fieldreferencenode-objectfield)
          * [8.2.2.5 FieldSetNode (`object.field = value`)](#8225-fieldsetnode-objectfield--value)
          * [8.2.2.6 IndexingNode (`object[index]`)](#8226-indexingnode-objectindex)
          * [8.2.2.7 IndexSetNode (`object[index] = value`)](#8227-indexsetnode-objectindex--value)
          * [8.2.2.8 SubscriptNode (any subscript)](#8228-subscriptnode-any-subscript)
          * [8.2.2.9 TypecastNode (any typecast)](#8229-typecastnode-any-typecast)
          * [8.2.2.10 UnaryOperatorNode (any `<operator> a` operator)](#82210-unaryoperatornode-any-operator-a-operator)
      * [8.2.3 Generators](#823-generators)
          * [8.2.3.1 Pattern](#8231-pattern)
          * [8.2.3.2 DictForGeneratorNode](#8232-dictforgeneratornode)
          * [8.2.3.3 DictThroughGeneratorNode](#8233-dictthroughgeneratornode)
          * [8.2.3.4 ListForGeneratorNode](#8234-listforgeneratornode)
          * [8.2.3.5 ListThroughGeneratorNode](#8235-listthroughgeneratornode)
          * [8.2.3.6 RangeNode](#8236-rangenode)
      * [8.2.4 Literals (values)](#824-literals-values)
          * [8.2.4.1 LiteralBool](#8241-literalbool)
          * [8.2.4.2 LiteralClass](#8242-literalclass)
          * [8.2.4.3 LiteralDict](#8243-literaldict)
          * [8.2.4.4 LiteralFunction](#8244-literalfunction)
          * [8.2.4.5 LiteralLambda](#8245-literallambda)
          * [8.2.4.6 LiteralList](#8246-literallist)
          * [8.2.4.7 LiteralNull](#8247-literalnull)
          * [8.2.4.8 LiteralNum](#8248-literalnum)
          * [8.2.4.9 LiteralStr](#8249-literalstr)
      * [8.2.5 Sections](#825-sections)
          * [8.2.5.1 BlockNode (sections / code blocks)](#8251-blocknode-sections--code-blocks)
          * [8.2.5.2 ForNode (`for` or `every` loops)](#8252-fornode-for-or-every-loops)
          * [8.2.5.3 IfNode](#8253-ifnode)
          * [8.2.5.4 LoopStopNode (`loop-stop when` loop)](#8254-loopstopnode-loop-stop-when-loop)
          * [8.2.5.5 ThroughNode (`through` loop)](#8255-throughnode-through-loop)
          * [8.2.5.6 TryNode](#8256-trynode)
          * [8.2.5.7 WhileNode](#8257-whilenode)
      * [8.2.6 Variable](#826-variable)
          * [8.2.6.1 VariableNode (any variable reference)](#8261-variablenode-any-variable-reference)
<!-- TOC -->

### 8.1 Common definitions
**<u>Throw</u>** - throw a Java exception

**<u>Raise</u>** - throw a Quail exception

**<u>RuntimeStriker</u>** - universal program flow
interrupter. Used for `break`, `continue`, `return`,
`strike`, `throw`

**<u>Overridable get/set</u>** - get/set from/to object
that allows user to override get/set behaviour with
`_get`/`_set`/`_get_?`/`_set_?` methods.

Every heading here is specified in form of 
`NodeName (which syntax is applicable)`. Learn more about syntax in Chapter 2.

### 8.2 Runtime execution

#### 8.2.1 Effects

###### 8.2.1.1 AsyncNode (`async` effect)
1. Async runtime worker created and started

###### 8.2.1.2 EffectNode (any of the effects, except `return` and `use`)
- If `assert`: Raise QAssertionException if value is false
- If `throw`: Raise provided exception
- If `strike`: Throw runtime striker with provided 
  strike power, or raise UnsuitableTypeException if 
  value is not a number or < -1
- If `import`:
  1. Read file at provided path, if IOException thrown,
     raise IOException
  2. Run code from collected string, catching exception.
     
     If exception is caught, an Exception is raised

###### 8.2.1.3 InstructionNode (any instruction)
- If `break`: Throw runtime striker (break)
- If `continue`: Throw runtime striker (continue)

###### 8.2.1.4 ReturnNode (`return` effect)
Return provided value

###### 8.2.1.5 UseNode (`use` effect)
Load library with provided identifier (see Chapter 9)
and place it into specified variable

#### 8.2.2 Expression
Most of these operations are defined by and in type
classes. They can raise various exceptions.

###### 8.2.2.1 AssignNode (assignment operator)
- If variable already defined in scope
  
  Try to reassign it. Exception raise possible
- Else
  
  Assign variable and write its modifiers. Exception
  cannot happen.

###### 8.2.2.2 BinaryOperatorNode (any of `a <operator> b` operators)
1. Evaluate left and right parts
2. - If singular operator: perform bin op
   - If array operator: unwrap lists, apply op to
     each pair. Raises UnsuitableTypeException and
     UnsuitableValueException if left or right 
     operand is not a list, or if they sizes do not
     match. Returns resulting list.
   - If matrix operator: same as array, but does double
     unwrap. Returns resulting matrix

###### 8.2.2.3 CallNode (any call)
1. Determine callee and parent:
   - If is field call: parent and callee obtained
   - Else: only callee obtained
2. Evaluate all args
3. Evaluate all kwargs
4. Call
   - If callee is func and parent is obtained and is
     field call:
     - If parent is dict and it contains callee in
       its pairs: perform usual call
     - Else if parent is not a prototype:
       Perform method call (pass `this`)
     - Else:
       Perform usual call
   - Else:
     Perform usual call

###### 8.2.2.4 FieldReferenceNode (`object.field`)
1. Obtain parent
2. Get (overridable) provided field from parent

###### 8.2.2.5 FieldSetNode (`object.field = value`)
1. Obtain parent
2. Set (overridable) provided field in parent

###### 8.2.2.6 IndexingNode (`object[index]`)
1. Obtain parent
2. Get at index from parent

###### 8.2.2.7 IndexSetNode (`object[index] = value`)
1. Obtain parent
2. Set at index in parent

###### 8.2.2.8 SubscriptNode (any subscript)
1. Obtain parent and all not-null subscript parts
   (start, end, step)
2. Perform subscript or stepped subscript on parent
   (based on whether step was provided or not)

###### 8.2.2.9 TypecastNode (any typecast)
1. Obtain conversion subject
2. If conversion to:
   - num
     try to convert to num
   - string
     try to convert to string
   - bool
     try to convert to bool
   
   Else: Raise UnsupportedConversionException

###### 8.2.2.10 UnaryOperatorNode (any `<operator> a` operator)
Same as 8.2.2.2 (BinaryOperatorNode), but with only
one operand (which removes size equality check)


#### 8.2.3 Generators
###### 8.2.3.1 Pattern
All generators (8.2.3.2-8.2.3.5) fall for the same
pattern:
1. A new scope is created
2. Generated dict/list created (empty for now)
3. Generated dict/list is put in new scope under name `_this`
4. Iterable is evaluated
5. Iteration: (see also 8.2.5.2 and 8.2.5.5)
   - If condition is present and met or condition not
     present:
     1. Evaluate value or key-value pair
     2. Add value or key-value pair
6. Return generated, destroy new scope

###### 8.2.3.2 DictForGeneratorNode
See 8.2.3.1

###### 8.2.3.3 DictThroughGeneratorNode
See 8.2.3.1

###### 8.2.3.4 ListForGeneratorNode
See 8.2.3.1

###### 8.2.3.5 ListThroughGeneratorNode
See 8.2.3.1

###### 8.2.3.6 RangeNode
1. Start, end and step object are evaluated.
2. If any of there 3 are not numbers, 
   UnsuitableTypeException raised.
3. If step not manually provided:
   - If start <= end: step = 1
   - Else: step = -1
4. Number iterator initialized with start value
5. Iteration until: 

   If range is including, < and > replaced with <= and >=
   
   If range is increasing (start <= end), condition is
   iterator </<= end, else iterator >/>= end.
   - Create new QNumber with current iterator value
   - Increment/decrement iterator
6. Return generated range


#### 8.2.4 Literals (values)
###### 8.2.4.1 LiteralBool
Bool is evaluated

###### 8.2.4.2 LiteralClass
1. New scope is created
2. Parent object is obtained (if not provided - Object)
3. Every method in class is evaluated in the new scope
4. Every field is put in the new scope
5. Every other statement is executed in the new scope
6. Inheritance is registered
7. Object table is set to the new scope contents
8. Place class into outer scope

###### 8.2.4.3 LiteralDict
Every key-value pair evaluated and put into dict.
Quail will try to convert every key to string (8.2.2.9)

###### 8.2.4.4 LiteralFunction
1. Arguments are initialized
2. If current scope already contains a function with
   same name, evaluated function is treated as an
   alternative call to existing function
3. Else - function is constructed and placed into scope

If function name is 

###### 8.2.4.5 LiteralLambda
Function is created and returned

###### 8.2.4.6 LiteralList
Every value is evaluated and added to list. List is returned

###### 8.2.4.7 LiteralNull
Null is returned

###### 8.2.4.8 LiteralNum
Number is returned

###### 8.2.4.9 LiteralStr
String is returned


#### 8.2.5 Sections
###### 8.2.5.1 BlockNode (sections / code blocks)
Every node in block is evaluated one-by-one sequentially

###### 8.2.5.2 ForNode (`for` or `every` loops)
1. Iterable is evaluated
2. Start iteration called on iterable
3. While there is more to iterate: call next iteration on iterable

###### 8.2.5.3 IfNode
1. Condition is evaluated
2. If condition is true, appropriate statement executed; then IfNode ends
3. If else if-s are present:
   
   For each else-if:
   1. Condition is evaluated
   2. If condition is true, appropriate statement executed; then IfNode ends
4. If else is present - execute else

###### 8.2.5.4 LoopStopNode (`loop-stop when` loop)
1. Statement inside loop is executed
2. Condition is evaluated
3. If condition is true - quit cycle
4. Go to 1

###### 8.2.5.5 ThroughNode (`through` loop)
1. Start, end, and step of loop are evaluated
2. While iterator < end:
   1. Statement inside loop is executed
   2. Iterator is incremented by step

###### 8.2.5.6 TryNode
1. Statement inside `try` is being run
2. If no exceptions were thrown, TryNode ends
3. If there were exceptions, but there are no catch blocks - 
   exceptions are suppressed, TryNode ends
4. If catch blocks are present - do following for each block from top to bottom:
   1. If no class specified for catch block - execute it
   2. If class is specified - execute if exception is an instance of specified class
5. If there were no suitable catch blocks, but they are present,
   exception is re-thrown

###### 8.2.5.7 WhileNode
1. Condition is evaluated
2. If condition is false - quit cycle
3. Statement inside loop is executed
4. Go to 1


#### 8.2.6 Variable

###### 8.2.6.1 VariableNode (any variable reference)
If variable exists, its value is returned.

If not and variable node has modifiers, variable
is being initialized according to the first modifier
to an 'empty' synonym for that type:
(e.g. num v -> initialize v to 0)