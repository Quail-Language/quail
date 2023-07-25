#?toc-html <h4>Standard types</h4>
#?toc-entry Object
#?toc-entry Number
#?toc-entry Bool
#?toc-entry List
#?toc-entry Dict
#?toc-entry Func
#?toc-entry Null
#?toc-html <h4>Standard exceptions</h4>
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
        #? Called when field in that object is accessed through object.field or Object.get.
        #? When field is accessed through Object.getStrict or object["field"] call to this method will be omitted.
    }

    object _get_FIELDNAME(this) {
        #?badge-yellow May be null
        #? Called when field FIELDNAME in that object is accessed through object.FIELDNAME or Object.get.
        #? When field is accessed through Object.getStrict or object["FIELDNAME"] call to this method will be omitted.
    }

    object _set(this, string key, object value) {
        #?badge-yellow May be null
        #? Called when field in that object is set through object.field = value or Object.set.
        #? When field is set through Object.setStrict or object["field"] = value call to this method will be omitted.
    }

    object _set_FIELDNAME(this, object value) {
        #?badge-yellow May be null
        #? Called when field FIELDNAME in that object is set through object.FIELDNAME = value or Object.set.
        #? When field is accessed through Object.setStrict or object["FIELDNAME"] = value call to this method will be omitted.
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
    #? <b>Unlike in any other language, it's mutable</b>

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
    }

    bool isNumeric(this) {
        #? Check if string contains only numeric characters
    }

    bool isAlphaNumeric(this) {
        #? Check if string contains only numeric and alphabetical characters
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

    list split(this, string separator) {
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
    #? A key=value data structure

    #?
}