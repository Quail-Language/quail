# Quail Specification
[Back to index](index.md)

## Chapter 8: Statement executions

<!-- TOC -->
* [Quail Specification](#quail-specification)
  * [Chapter 8: Statement executions](#chapter-8-statement-executions)
    * [8.1 Common definitions](#81-common-definitions)
    * [8.2 Runtime execution](#82-runtime-execution)
      * [8.2.1 Effects](#821-effects)
          * [8.2.1.1 AsyncNode](#8211-asyncnode)
          * [8.2.1.2 EffectNode](#8212-effectnode)
          * [8.2.1.3 InstructionNode](#8213-instructionnode)
          * [8.2.1.4 ReturnNode](#8214-returnnode)
          * [8.2.1.5 UseNode](#8215-usenode)
      * [8.2.2 Expression](#822-expression)
          * [8.2.2.1 AssignNode](#8221-assignnode)
          * [8.2.2.2 BinaryOperatorNode](#8222-binaryoperatornode)
          * [8.2.2.3 CallNode](#8223-callnode)
          * [8.2.2.4 FieldReferenceNode](#8224-fieldreferencenode)
          * [8.2.2.5 FieldSetNode](#8225-fieldsetnode)
          * [8.2.2.6 IndexingNode](#8226-indexingnode)
          * [8.2.2.7 IndexSetNode](#8227-indexsetnode)
          * [8.2.2.8 SubscriptNode](#8228-subscriptnode)
          * [8.2.2.9 TypecastNode](#8229-typecastnode)
          * [8.2.2.10 UnaryOperatorNode](#82210-unaryoperatornode)
      * [8.2.3 Generators](#823-generators)
          * [8.2.3.1 Pattern](#8231-pattern)
          * [8.2.3.2 DictForGeneratorNode](#8232-dictforgeneratornode)
          * [8.2.3.3 DictThroughGeneratorNode](#8233-dictthroughgeneratornode)
          * [8.2.3.4 ListForGeneratorNode](#8234-listforgeneratornode)
          * [8.2.3.5 ListThroughGeneratorNode](#8235-listthroughgeneratornode)
          * [8.2.3.6 RangeNode](#8236-rangenode)
      * [8.2.4 Literals](#824-literals)
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
          * [8.2.5.1 BlockNode](#8251-blocknode)
          * [8.2.5.2 ForNode](#8252-fornode)
          * [8.2.5.3 IfNode](#8253-ifnode)
          * [8.2.5.4 LoopStopNode](#8254-loopstopnode)
          * [8.2.5.5 ThroughNode](#8255-throughnode)
          * [8.2.5.6 TryNode](#8256-trynode)
          * [8.2.5.7 WhileNode](#8257-whilenode)
      * [8.2.6 Variable](#826-variable)
          * [8.2.6.1 VariableNode](#8261-variablenode)
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

### 8.2 Runtime execution

#### 8.2.1 Effects

###### 8.2.1.1 AsyncNode
1. Async runtime worker created and started

###### 8.2.1.2 EffectNode
- If assert: Raise QAssertionException if value is false
- If throw: Raise provided exception
- If strike: Throw runtime striker with provided 
  strike power, or raise UnsuitableTypeException if 
  value is not a number or < -1
- If import:
  1. Read file at provided path, if IOException thrown,
     raise IOException
  2. Run code from collected string, catching exception.
     
     If exception is caught, an Exception is raised

###### 8.2.1.3 InstructionNode
- If break: Throw runtime striker (break)
- If continue: Throw runtime striker (continue)

###### 8.2.1.4 ReturnNode
Return provided value

###### 8.2.1.5 UseNode
Load library with provided id (see $9)

#### 8.2.2 Expression
Most of these operations are defined by and in type
classes. They can raise various exceptions.

###### 8.2.2.1 AssignNode
- If variable already defined in scope
  
  Try to reassign it. Exception raise possible
- Else
  
  Assign variable and write its modifiers. Exception
  cannot happen.

###### 8.2.2.2 BinaryOperatorNode
1. Evaluate left and right parts
2. - If singular operator: perform bin op
   - If array operator: unwrap lists, apply op to
     each pair. Raises UnsuitableTypeException and
     UnsuitableValueException if left or right 
     operand is not a list, or if they sizes do not
     match. Returns resulting list.
   - If matrix operator: same as array, but does double
     unwrap. Returns resulting matrix

###### 8.2.2.3 CallNode
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

###### 8.2.2.4 FieldReferenceNode
1. Obtain parent
2. Get (overridable) provided field from parent

###### 8.2.2.5 FieldSetNode
1. Obtain parent
2. Set (overridable) provided field in parent

###### 8.2.2.6 IndexingNode
1. Obtain parent
2. Get at index from parent

###### 8.2.2.7 IndexSetNode
1. Obtain parent
2. Set at index in parent

###### 8.2.2.8 SubscriptNode
1. Obtain parent and all not-null subscript parts
   (start, end, step)
2. Perform subscript or stepped subscript on parent
   (based on whether step was provided or not)

###### 8.2.2.9 TypecastNode
1. Obtain conversion subject
2. If conversion to:
   - num
     try to convert to num
   - string
     try to convert to string
   - bool
     try to convert to bool
   
   Else: Raise UnsupportedConversionException

###### 8.2.2.10 UnaryOperatorNode
Same as 8.2.2.2 (BinaryOperatorNode), but with only
one operand (which removes size equality check)


#### 8.2.3 Generators
###### 8.2.3.1 Pattern
All generators (8.2.3.2-8.2.3.5) fall for the same
pattern:
1. A new scope is created
2. Generated dict/list created (empty for now)
3. Generated dict/list put in new scope under name `_this`
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


#### 8.2.4 Literals
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
###### 8.2.5.1 BlockNode
Every node in block is evaluated one-by-one

###### 8.2.5.2 ForNode
###### 8.2.5.3 IfNode
###### 8.2.5.4 LoopStopNode
###### 8.2.5.5 ThroughNode
###### 8.2.5.6 TryNode
###### 8.2.5.7 WhileNode


#### 8.2.6 Variable

###### 8.2.6.1 VariableNode
If variable exists, its value is returned.

If not and variable node has modifiers, variable
is being initialized according to the first modifier
to an 'empty' synonym for that type:
(e.g. num v -> initialize v to 0)