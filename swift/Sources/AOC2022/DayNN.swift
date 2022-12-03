enum DayNN {
    static func solve1(_ input: String) throws -> Int {
        let lines = input.components(separatedBy: "\n")
        return try lines
            .map { l in try score1(l) }
            .reduce(0, +)
    }

    static func score1(_ line: String) throws -> Int {
        return 0
    }

    static func solve2(_ input: String) throws -> Int {
        let lines = input.components(separatedBy: "\n")
        return try lines
            .map { l in try score2(l) }
            .reduce(0, +)
    }

    static func score2(_ line: String) throws -> Int {
        return 0
    }
}
