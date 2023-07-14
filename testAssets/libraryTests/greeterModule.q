use "main.q" = main

function greet(string target) {
    print("Hello, " + target + "!")
}

return {
    "greet" = greet
}