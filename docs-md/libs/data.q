class Queue {
    #? Implementation of a single-ended queue.
    #? Supports <code>in</code> operator and iteration

    num size(this) {}
    void add(this, object obj) {}
    object pop(this) {}
    void clear(this) {}
    object peek(this) {}
}

class Deque {
    #? Implementation of a double-ended queue.
    #? Supports <code>in</code> operator and iteration

    num size(this) {}
    void addFront(this, object obj) {}
    void addBack(this, object obj) {}
    object popFront(this) {}
    object popBack(this) {}
    void clear(this) {}
    object peekFront(this) {}
    object peekBack(this) {}
}

class Set {
    #? Implementation of a set.
    #? Can contain only booleans, strings and numbers
    #? Supports <code>in</code> operator, equality check and iteration

    constructor (this, list | void values=null) {}

    num size(this) {}
    void add(this, bool | string | num obj) {}
    void remove(this, bool | string | num obj) {}
    void clear(this) {}

    static object union(a, b) {}
    static object intersection(a, b) {}
}

class Bytes {
    #? Implementation of a byte array.
    #? Supports equality check, iteration, indexing and subscripts

    static object fromBase64(string b64) {}
    static object fromBits(string bits) {}
    static object fromSignedInt(num uint) {}
    static string asBase64(string|object bytes) {}
    static num asUnsignedInt(string|object bytes) {}
    static num asSignedInt(string|object bytes) {}
    static num asBits(string|object bytes) {}

    static object or(string|object a, string|object b) {}
    static object and(string|object a, string|object b) {}
    static object xor(string|object a, string|object b) {}
    static object implies(string|object a, string|object b) {}
    static object nor(string|object a, string|object b) {}
    static object nand(string|object a, string|object b) {}
    static object xnor(string|object a, string|object b) {}

    bool bitAt(this, num pos) {}
    void setBitAt(this, num pos, bool bit) {}
}

