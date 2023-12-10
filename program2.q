function OuterWrapper(f) {
    return function() {
        print("Here is your outer wrapper")
        f()
    }
}


function TestAnnotatiodsn(f, message) {
    return function() {
        print("Begin execution")
        print("Here goes the custom message: " + message)
        f()
        print("End execution")
    }
}


@Deprecated
@OuterWrapper
@TestAnnotation("Hellodfsdf!")
function hello()
    print("Hello")

hello()
