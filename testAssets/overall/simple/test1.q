function main() {
    list retValue = ""

    print("Hello, World!")

    print("Now begins list of squares from 1 to 10")
    through 1:+10 as i do
        print(i^2)
        retValue += string(i)
    end

    print("Now the cubes")
    for i in 1:+10 do
        print(i^3)
        retValue += string(i)
    end

    print("Bye for now")

    return retValue
}


return main()
