enum Day09 {
    static func solve1(_ input: String) -> Int {
        return solve(input, knots: 2)
    }

    static func solve2(_ input: String) -> Int {
        return solve(input, knots: 10)
    }

    static func solve(_ input: String, knots: Int) -> Int {
        let lines = input.components(separatedBy: "\n")
        var visited = Set<Pos>()

        var knots = (0..<knots).map { _ in Pos(0, 0) }

        for line in lines {
            let direction = direction(line.components(separatedBy: " ")[0])
            let steps = numbers(line)[0]

            for _ in 0..<steps {
                knots[0] = knots[0] + direction

                for i in 1..<knots.count {
                    knots[i] = move(pos: knots[i], following: knots[i - 1])
                }
                visited.insert(knots[knots.count - 1])
            }
        }
        return visited.count
    }

    static func direction(_ s: String) -> Pos {
        switch s {
        case "U": return Pos(0, 1)
        case "R": return Pos(1, 0)
        case "D": return Pos(0, -1)
        case "L": return Pos(-1, 0)
        default: assertionFailure("Unknown direction \(s)")
        }
        return Pos(0, 0)
    }

    static func move(pos: Pos, following: Pos) -> Pos {
        var x = pos.x
        var y = pos.y

        if abs(x - following.x) <= 1, abs(y - following.y) <= 1 {
            return pos
        }

        if x < following.x {
            x += 1
        } else if x > following.x {
            x -= 1
        }
        if y < following.y {
            y += 1
        } else if y > following.y {
            y -= 1
        }
        return Pos(x, y)
    }
}
