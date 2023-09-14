# TODO feature-request: python like lambdas:   x ->: num(x)


function arrays() {
    a = map((x) -> return num(x), input().split(" "))
    b = map((x) -> return num(x), input().split(" "))
    print("Arrays:", a, b)

    #s = []
    #for i in 0:a.size():
    #    s.add(a[i] + b[i])

    #s = [a[i] + b[i] for i in 0:a.size()]

    s = a [+] b

    print(s)
}


c = [
    [0, 1],
    [2, 3]
]
d = [
    [4, 5],
    [6, 7]
]

#m = []
#for y in 0:c.size():
#    row = []
#    for x in 0:c[y].size():
#        row.add(c[y][x] + d[y][x])
#    m.add(row)

#m = [[c[y][x] + d[y][x] for x in 0:c[y].size()] for y in 0:c.size()]

m = c {+} d

print(m)
