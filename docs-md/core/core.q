#?toc-html <h4>Standard types</h4>
#?toc-entry Object
#?toc-entry Number
#?toc-entry Bool
#?toc-entry List
#?toc-entry Dict
#?toc-entry Func
#?toc-entry Null
#?toc-html <h4><br>Standard exceptions</h4>
#?toc-entry Exception
#?toc-entry AssertionException
#?toc-entry CircularDependencyException
#?toc-entry DerivationException
#?toc-entry IOException
#?toc-entry IterationNotStartedException
#?toc-entry IterationStopException
#?toc-entry UnsuitableTypeException
#?toc-entry UnsuitableValueException
#?toc-entry UnsupportedConversionException
#?toc-entry UnsupportedIterationException
#?toc-entry UnsupportedOperationException
#?toc-entry UnsupportedStepSubscriptException
#?toc-entry UnsupportedSubscriptException
#?toc-entry UnsupportedUnaryOperationException
#?toc-html <h4><br>Standard functions</h4>
#?toc-entry all
#?toc-entry any
#?toc-entry clone
#?toc-entry copy
#?toc-entry enumerate
#?toc-entry map
#?toc-entry millis
#?toc-entry input
#?toc-entry print
#?toc-entry put
#?toc-entry abs
#?toc-entry acos
#?toc-entry asin
#?toc-entry atan
#?toc-entry atan2
#?toc-entry cos
#?toc-entry cosh
#?toc-entry max
#?toc-entry min
#?toc-entry sin
#?toc-entry sinh
#?toc-entry tan
#?toc-entry tanh
#?toc-entry bin
#?toc-entry dec
#?toc-entry hex
#?toc-entry oct

#?html <h1 id="overview">Overview</h1>
#?html <hr>
#?html <h1 id="standard-classes">Standard classes</h1>

class Object {
    #? Ancestor class for all other classes

    dict table(this) {
        #? Get all key=value pairs in form of a dict
    }

    object _get(this, string key) {
        #?badge-yellow May be null
        #?badge-red Not present, but can be defined in child class by user
        #? Called when field in that object is accessed through object.field or Object.get.
        #? When field is accessed through object["field"] call to this method will be omitted.
    }

    object _get_FIELDNAME(this) {
        #?badge-yellow May be null
        #?badge-red Not present, but can be defined in child class by user
        #? Called when field FIELDNAME in that object is accessed through object.FIELDNAME or Object.get.
        #? When field is accessed through object["FIELDNAME"] call to this method will be omitted.
    }

    object _set(this, string key, object value) {
        #?badge-yellow May be null
        #?badge-red Not present, but can be defined in child class by user
        #? Called when field in that object is set through object.field = value or Object.set.
        #? When field is set through object["field"] = value call to this method will be omitted.
    }

    object _set_FIELDNAME(this, object value) {
        #?badge-yellow May be null
        #?badge-red Not present, but can be defined in child class by user
        #? Called when field FIELDNAME in that object is set through object.FIELDNAME = value or Object.set.
        #? When field is accessed through object["FIELDNAME"] = value call to this method will be omitted.
    }
}

class Number like Object {
    #? Represents a number
}

class Bool like Object {
    #? Represents a boolean value
}

class String like Object {
    #? Represents a piece of text

    string capitalized(this) {
        #? Makes first letter in string capital and all other letters - small
    }

    string centered(this, num size) {
        #? Centers string in row of defined size
    }

    num count(this, string needle) {
        #? Counts all occurrences of given <code>needle</code> in this string
    }

    num find(this, string needle) {
        #? Returns index of first occurrence of <code>needle</code> in this string
        #? If <code>needle</code> was not found, <code>-1</code> is returned
    }

    num findFromRight(this, string needle) {
        #? Returns index of first occurrence of <code>needle</code> in this string, counting from rightmost char
        #? If <code>needle</code> was not found, <code>-1</code> is returned
    }

    string encoded64(this) {
        #? Returns base64 encoded version of that string
    }

    string decoded64(this) {
        #? Returns base64 decoded version of that string (assuming that string is base64-encoded)
    }

    bool startsWith(this, string prefix) {
        #? Checks if this string starts with given prefix
    }

    bool endsWith(this, string suffix) {
        #? Checks if this string ends with given suffix
    }

    string format(this, list values) {
        #? Format this string with given values by <code>%number</code> pattern.
        #? E.g. <code>"%0, %1".format(["Hello", 3]) -> "Hello, 3"</code>
    }

    string format(this, dict values) {
        #? Format this string with given values by <code>%key</code> pattern.
        #? E.g. <code>"Hello, %name".format({"name"="John"}) -> "Hello, John"</code>
    }

    bool isAlphabetical(this) {
        #? Check if string contains only alphabetical characters
        #? Space is included
    }

    bool isNumeric(this) {
        #? Check if string contains only numeric characters
    }

    bool isAlphaNumeric(this) {
        #? Check if string contains only numeric and alphabetical characters
        #? Space is included
    }

    bool isUpper(this) {
        #? Check if string contains only uppercase letters
    }

    bool isLower(this) {
        #? Check if string contains only lowercase letters
    }

    string inBetweenOf(this, list values) {
        #? Place this string in between given values.
        #? E.g. <code>", ".inBetween(["a", "b", "c"]) -> "a, b, c"</code>
    }

    string lowerCased(this) {
        #? Returns same string, but with all characters being in lowercase
    }

    string upperCased(this) {
        #? Returns same string, but with all characters being in uppercase
    }

    string replaceAll(this, string target, string replacement) {
        #? Replaces all occurrences of <code>target</code> with <code>replacement</code>
        #?badge Does not mutate
    }

    string replaceFirst(this, string target, string replacement) {
        #? Replaces first occurrence of <code>target</code> with <code>replacement</code>
        #?badge Does not mutate
    }

    list split(this, string delimiter) {
        #? Splits this string by given separator and returns that list
    }

    string swappedCase(this) {
        #? Makes all uppercase letters lower and vice versa
    }

    num size(this) {
        #? Returns length of this string
    }

    string reversed(this) {
        #? Returns reversed string
    }

    list chars(this) {
        #? Returns list of characters of that string
    }

}


class List like Object {
    #? An ordered dynamic collection of various elements

    void add(this, value) {
        #? Adds given value to the end
    }

    void addAll(this, list values) {
        #? Adds all given values to the end
    }

    void insert(this, num index, value) {
        #? Inserts given value at given index
        #? After operation, <code>value</code>'s located at <code>index</code>. All further elements are shifted by +1
    }

    void clear(this) {
        #? Clears the list
    }

    num count(this, needle) {
        #? Counts all occurrences of given <code>needle</code> in this list
    }

    num find(this, needle) {
        #? Returns index of first occurrence of <code>needle</code> in this list
        #? If <code>needle</code> was not found, <code>-1</code> is returned
    }

    num findFromRight(this, needle) {
        #? Returns index of first occurrence of <code>needle</code> in this list, counting from rightmost element
        #? If <code>needle</code> was not found, <code>-1</code> is returned
    }

    object get(this, num index) {
        #? Returns element at given index. If index is unrepresented, null is returned
    }

    object pop(this) {
        #? Removes last element from list and returns the removed element
    }

    object remove(this, element) {
        #? Removes given element from list. Returns removed element
    }

    object removeElementAt(this, num index) {
        #? Removes element at given index from list. Returns removed element
    }

    void reverse(this) {
        #? Reverses this list
    }

    list reversed(this) {
        #? Returns reversed list
        #?badge Does not mutate
    }

    object set(this, num index, value) {
        #? Sets element at given index.
        #? If list's size < index, then list is expanded with null-s up to given index
    }

    num size(this) {
        #? Returns size of this list
    }

    void sort(this, func comparator) {
        #? Sorts the list with given comparator.
        #? Comparator is a function which returns:
        #?html <ul>
        #?html <li>-1 if a &lt; b</li>
        #?html <li>0 if a = b</li>
        #?html <li>1 if a &gt; b</li>
        #?html </ul>
    }

    list sorted(this, func comparator) {
        #? Acts like <a href="#List::sort">List::sort</a>, but returns sorted list
        #?badge Does not mutate
    }

    string joined(this, string separator) {
        #? Same as <a href="#String::inBetweenOf">String::inBetweenOf</a>
    }

}


class Dict like Object {
    #? A string key -> object value data structure

    list keys(this) {
        #? List all keys in this dict
    }

    list values(this) {
        #? List all values in this dict
    }

    list pairs(this) {
        #? List all <code>[key, value]</code> pairs
    }

    bool containsKey(this, string key) {
        #? Check if this dict contains given key
    }

    num size(this) {
        #? Returns size of this dict
    }

    void set(this, string key, value) {
        #? Set given key to given value in this dict
    }

    object get(this, string key) {
        #? Get value by given key in this dict
    }

    static dict assemblePairs(list pairs) {
        #? Assemble dict from given list of <code>[key, value]</code> pairs
    }

}

class Func {
    #? Resembles functions and methods
}

class Null {
    #? Resembles the <code>null</code> value
}


#?html <hr>
#?html <h1 id="standard-exceptions">Standard exceptions</h1>

class Exception {
    #? Base class for all exceptions in Quail

    string message
}

class AssertionException like Exception {
    #? Thrown when <code>assert</code> results in <code>false</code>
}

class CircularDependencyException like Exception {
    #? Thrown when one file is used in other file, that the first one trying to use
}

class DerivationException like Exception {
    #? Thrown on attempt to derive or extend from non-prototype object

    object target
}

class IOException like Exception {
    #? Resembles Java's IOException
}

class IterationNotStartedException like Exception {
    #? Thrown when <code>_next()</code> is called, but iteration was not started
}
class IterationStopException like Exception {
    #? Should be thrown when iteration reaches its end
}

class UnsuitableTypeException like Exception {
    #? Thrown when object is not suitable for operation because of its type

    object value
}

class UnsuitableValueException like Exception {
    #? Thrown when object is not suitable for operation because of its value

    object value
}

class UnsupportedConversionException like Exception {
    #? Thrown when object is not suitable for conversion

    object operand
    string targetType
}

class UnsupportedIterationException like Exception {
    #? Thrown when object is not suitable for iteration

    object operand
}

class UnsupportedOperationException like Exception {
    #? Thrown when an operation is not supported by object

    object left
    string operator
    object right
}

class UnsupportedStepSubscriptException like Exception {
    #? Thrown when object is not suitable for stepped subscript

    object operand
}

class UnsupportedSubscriptException like Exception {
    #? Thrown when object is not suitable for subscript

    object operand
}

class UnsupportedUnaryOperationException like Exception {
    #? Thrown when an operation is not supported by object

    string operator
    object operand
}

#?html <hr>
#?html <h1 id="standard-functions">Standard functions</h1>

bool all(list values) {
    #? Check if all given values are true
}

bool any(list values) {
    #? Check if any of given values is true
}

object clone(obj) {
    #? Clones given object
}

object copy(obj) {
    #? Copies given object
}

list enumerate(list collection) {
    #? Numerates given collection
    #? E.g. <code>enumerate(["a", "b", "c"]) -> [[0, "a"], [1, "b"], [2, "c"]]</code>
}

list zip(list left, list right) {
    #? Zips 2 lists into one
    #? E.g. <code>zip([0, 1, 2], ["a", "b", "c"]) -> [[0, "a"], [1, "b"], [2, "c"]]</code>
}

list map(func callback, list collection) {
    #? Applies given function to every item in collection and returns results of function in the order of application
}

num millis() {
    #? Get current time in milliseconds
}

string input(string prompt = "") {
    #? Put given prompt to console, wait for user input and return it
}

void print(values...) {
    #? Print all values separated by space and put \n at the end
}

void put(values...) {
    #? Print all values separated by space, but do not put \n at the end
}

num abs(num n) {
    #? Absolute value of given number
}

num acos(num n) {
    #? Arc cosine of given number
}

num asin(num n) {
    #? Arc sine of given number
}

num atan(num n) {
    #? Arc tangent of given number
}

num atan2(num x, num y) {
    #? Arc tangent2 of given x;y
}

num cos(num n) {
    #? Cosine of given number
}

num cosh(num n) {
    #? Hyperbolic cosine of given number
}

num max(list values) {
    #? Returns maximal value from given collection
}

num min(list values) {
    #? Returns minimal value from given collection
}

num sin(num n) {
    #? Sine of given number
}

num sinh(num n) {
    #? Hyperbolic sine of given number
}

num tan(num n) {
    #? Tangent of given number
}

num tanh(num n) {
    #? Hyperbolic tangent of given number
}

string bin(num n) {
    #? Convert given number to base-2 (integer only)
}

num dec(string n, num base) {
    #? Converts given number representation to in given base to base-10 (integer only)
}

string hex(num n) {
    #? Convert given number to base-16 (integer only)
}

string oct() {
    #? Convert given number to base-8 (integer only)
}
