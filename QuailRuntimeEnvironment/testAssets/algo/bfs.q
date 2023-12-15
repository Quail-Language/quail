#? BFS algorithm to determine chess knight path from A to B

INF = 1000000
n = -1
visited = null
dist = null
possibleMoves = [
        [-1, -2],
        [1, 2],
        [1, -2],
        [-1, 2],
        [2, 1],
        [2, -1],
        [-2, 1],
        [-2, -1]
]

bool shouldMoveTo(x, y) {
    if x >= 0 && x < n && y >= 0 && y < n
        return not visited[x][y]
    return false
}

list bfs(s, e) {
    dist[s[0]][s[1]] = 0
    linked_path = {}
    q = []
    q.add(s)
    while (q.size() > 0) {
        v = q[0]
        q.removeElementAt(0)
        for move in possibleMoves {
            if shouldMoveTo(v[0] + move[0], v[1] + move[1]) {
                q.add([v[0] + move[0], v[1] + move[1]])
                visited[v[0] + move[0]][v[1] + move[1]] = true
                dist[v[0] + move[0]][v[1] + move[1]] = dist[v[0]][v[1]] + 1
                linked_path[string([v[0] + move[0], v[1] + move[1]])] = v
            }
        }
    }
    if dist[e[0]][e[1]] == INF {
        return null
    } else {
        path = []
        while linked_path.containsKey(string(e)) {
            path.add(e)
            if (e[0] == s[0] && e[1] == s[1]) break
            e = linked_path[string(e)]
        }
        path.reversed()
        return path
    }
}

function main() {
    n = 5 #num(input())
    visited = [[false] * n] * n
    dist = [[INF] * n] * n
    #c1 = map((x) -> return num(x), input().split(" "))
    #c2 = map((y) -> return num(y), input().split(" "))
    c1 = map((x) -> return num(x), "3 3".split(" "))
    c2 = map((y) -> return num(y), "5 1".split(" "))
    x1 = c1[0]
    y1 = c1[1]
    x2 = c2[0]
    y2 = c2[1]
    path = bfs([x1 - 1, y1 - 1], [x2 - 1, y2 - 1]);
    if path == null {
        print(-1)
    } else {
        print(path.size() - 1)
        for i in path {
            print(i[0] + 1, i[1] + 1)
        }
    }
}

main()
