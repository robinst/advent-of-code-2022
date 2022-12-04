private extension ClosedRange {
    func contains(_ other: ClosedRange) -> Bool {
        return lowerBound <= other.lowerBound && upperBound >= other.upperBound
    }
}

enum Day04 {
    static func solve1(_ input: String) throws -> Int {
        let lines = input.components(separatedBy: "\n")
        return try lines
            .map { l in try score1(l) }
            .reduce(0, +)
    }

    static func score1(_ line: String) throws -> Int {
        let ranges = line.components(separatedBy: ",")
        let a = range(ranges[0])
        let b = range(ranges[1])
        return a.contains(b) || b.contains(a) ? 1 : 0
    }

    static func solve2(_ input: String) throws -> Int {
        let lines = input.components(separatedBy: "\n")
        return try lines
            .map { l in try score2(l) }
            .reduce(0, +)
    }

    static func score2(_ line: String) throws -> Int {
        let ranges = line.components(separatedBy: ",")
        let a = range(ranges[0])
        let b = range(ranges[1])
        return a.overlaps(b) ? 1 : 0
    }

    static func range(_ s: String) -> ClosedRange<Int> {
        let parts = s.components(separatedBy: "-")
        return Int(parts[0])! ... Int(parts[1])!
    }
}
