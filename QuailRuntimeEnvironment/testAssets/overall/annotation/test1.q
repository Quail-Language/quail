function OuterWrapper(f) {
    return function() {
        print("Here is your outer wrapper")
        f()
    }
}


function TestAnnotation(f, message) {
    return function() {
        print("Begin execution")
        print("Here goes the custom message: " + message)
        f()
        print("End execution")
    }
}

@OuterWrapper
@TestAnnotation("Hello!")
function hello()
    print("Hello")

hello()
