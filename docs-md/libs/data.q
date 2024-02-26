#?toc-entry Queue
#?toc-entry Deque
#?toc-entry Set
#?toc-entry Bytes

#?html <h2><code>lang/data</code></h2>
#?html <hr>
#? This library some useful data structures

class Queue {
    #? Implementation of a single-ended queue.
    #? Supports <code>in</code> operator and iteration

    num size(this) {}
    void add(this, object obj) {
        #? Add object to back of queue
    }
    object pop(this) {
        #? Pop object from front.
        #? If queue is empty, null is returned
    }
    void clear(this) {}
    object peek(this) {
        #? Get object from front without popping it.
        #? If queue is empty, null is returned
    }
}

class Deque {
    #? Implementation of a double-ended queue.
    #? Supports <code>in</code> operator and iteration

    num size(this) {}
    void addFront(this, object obj) {
        #? Add object to front of queue
    }
    void addBack(this, object obj) {
        #? Add object to back of queue
    }
    object popFront(this) {
        #? Pop object from front.
        #? If queue is empty, null is returned
    }
    object popBack(this) {
        #? Pop object from back.
        #? If queue is empty, null is returned
    }
    void clear(this) {}
    object peekFront(this) {
        #? Get object from front without popping it.
        #? If queue is empty, null is returned
    }
    object peekBack(this) {
        #? Get object from front without popping it.
        #? If queue is empty, null is returned
    }
}

class Set {
    #? Implementation of a set.
    #? Can contain only booleans, strings and numbers
    #? Supports <code>in</code> operator, equality check and iteration

    constructor (this, list | void values=null) {
        #? Constructs an empty set and adds all values (if specified)
    }

    num size(this) {}
    void add(this, bool | string | num obj) {
        #? Add object to set. If object is already in set - nothing happens
    }
    void remove(this, bool | string | num obj) {
        #? Remove object from set. If object is not in set - nothing happens
    }
    void clear(this) {}

    static object union(a, b) {
        #? Union of 2 sets
    }
    static object intersection(a, b) {
        #? Intersection of 2 sets
    }
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

