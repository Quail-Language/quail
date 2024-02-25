class MyException like Exception {}
class MyException2 like Exception {}

try {
    throw MyException()
} catch MyException2 as e {
    print(e)
}