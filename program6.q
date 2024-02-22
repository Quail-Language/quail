use "lang/data" = data; Bytes = data.Bytes; Set = data.Set; Queue = data.Queue; Deque = data.Deque;

q = Deque()
q.addBack("a")
q.addBack("b")
q.addFront("d")

for e in q:
    print(e)

