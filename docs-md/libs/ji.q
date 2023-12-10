class JavaClass {
    string getName(this) {}
    string getQualifiedName(this) {}
    bool isInstance(this, object obj) {}
    bool isInterface(this) {}
    object<JavaClass> getSuperclass(this) {}
    list<object<JavaClass>> getImplementedInterfaces(this) {}
}

class JavaObject {
    static object<JavaObject> pack(obj) {}
    object<JavaClass> getClass(this) {}
}

class JavaMethod {}

class SketchedJavaMethod {
    constructor (this, string signature, list<list<string>> args, func body) {}
}

class SketchedJavaField {
    constructor (this, string signature, object<JavaObject> value) {}
}

class SketchedJavaConstructor {
    constructor (this, list<list<string>> args, func body) {}
}

class SketchedJavaClass {
    constructor (this, string signature, list fields, list methods) {}
}

class SketchedJavaPackage {
    constructor (this, string name, classes...) {}
}

#? Get defined Java class
function getClass(string className)

function deployPackage(object<SketchedJavaPackage> package) {}