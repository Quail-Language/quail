#?toc-entry JavaClass
#?toc-entry JavaMethod
#?toc-entry JavaObject
#?toc-entry SketchedJavaClass
#?toc-entry SketchedJavaConstructor
#?toc-entry SketchedJavaField
#?toc-entry SketchedJavaInheritance
#?toc-entry SketchedJavaMethod
#?toc-entry SketchedJavaPackage
#?toc-entry getClass
#?toc-entry deployPackage

#?html <h2><code>lang/ji</code></h2>
#?html <hr>

class JavaClass {
    #? Proxy for Java classes
    static CLASS = 0
    static ABSTRACT = 1
    static INTERFACE = 2

    constructor (this, args...) {
        #? Instantiate JavaObject of that class
        #?see #JavaObject
    }
}

class JavaMethod {
    #? Proxy for Java methods
}

class JavaObject {
    #? Proxy for Java objects

    static object pack(object obj) {
        #? Wraps Quail native value as a JavaObject
    }

    object getClass(this) {
        #? Get class (instance of JavaClass)
        #?see #JavaClass
    }
}

class SketchedJavaClass {
    #? Used for runtime creation of Java classes
    constructor (this, string signature, contents...) {
        #? Prepares a Java class.
        #? Signature is a sequence of Java keyword modifiers and name of the class:
        #? E.g. <code>public final ClassName</code>
        #? Contents is a sequence of SketchedJavaConstructors, SketchedJavaFields,
        #? SketchedJavaInheritances and SketchedJavaMethods
        #?see #SketchedJavaField
        #?see #SketchedJavaInheritance
        #?see #SketchedJavaMethod
        #?see #SketchedJavaConstructor
    }
}

class SketchedJavaConstructor {
    #? Used for runtime creation of Java class constructors
    constructor (this, string signature, list args, func body) {
        #? Prepares a Java class constructor.
        #? Signature is a sequence of Java keyword modifiers: public, private, protected, static, final
        #? E.g. <code>public</code>
        #? Args is a list of strings that contain argument signatures.
        #? E.g. <code>["double a", "String b"]</code>
        #? Body is the Quail function that will be executed when this constructor is invoked
    }
}

class SketchedJavaField {
    #? Used for runtime creation of Java class fields
    constructor (this, string signature, object default) {
        #? Prepares a Java class sequence.
        #? Signature is a sequence of Java keyword modifiers (public, private, protected, static, final),
        #? a type (double, String, boolean...) and a name. E.g. public double a
        #? Default is an instance of JavaObject. Resembles the default value of this field
    }
}

class SketchedJavaInheritance {
    #? Used for inheritance of runtime created Java classes
    constructor (this, object extends) {
        #? Receives a JavaClass instance of extension or implementation
        #?see #JavaClass
    }
}

class SketchedJavaMethod {
    #? Used for runtime creation of Java class methods
    constructor (this, string signature, list args, func body) {
        #? Prepares a Java class constructor.
        #? Signature is a sequence of Java keyword modifiers (public, private, protected, static, final),
        #? a type (double, String, boolean...) and a name. E.g. public double a
        #? Args is a list of strings that contain argument signatures.
        #? E.g. <code>["double a", "String b"]</code>
        #? Body is the Quail function that will be executed when this method is invoked
    }
}

class SketchedJavaPackage {
    #? Used for runtime creation of Java packages
    constructor (this, string name, classes...) {
        #? Classes is a sequence of SketchedJavaClasses
        #?see #SketchedJavaClass
    }
}

function getClass(string className) {
    #? Get defined Java class by qualified ID
    #?see #JavaClass
}

function deployPackage(object package) {
    #? Creates all Java classes specified in passed SketchedJavaPackage
    #?see #SketchedJavaPackage
}