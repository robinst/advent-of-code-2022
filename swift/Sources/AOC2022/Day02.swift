private enum Hand {
    case rock
    case paper
    case scissors
}

extension Hand {
    static let beatsMap: [Hand: Hand] = [.rock: .scissors, .paper: .rock, .scissors: .paper]

    static let losesMap: [Hand: Hand] = [.scissors: .rock, .rock: .paper, .paper: .scissors]

    init(_ symbol: String) throws {
        switch symbol {
        case "A", "X": self = .rock
        case "B", "Y": self = .paper
        case "C", "Z": self = .scissors
        default: throw InputError.parseError("Can't parse \(symbol)")
        }
    }

    func beats() -> Hand {
        return Hand.beatsMap[self]!
    }

    func loses() -> Hand {
        return Hand.losesMap[self]!
    }

    func value() -> Int {
        switch self {
        case .rock:
            return 1
        case .paper:
            return 2
        case .scissors:
            return 3
        }
    }
}

enum Day02 {
    static func solve1(_ input: String) throws -> Int {
        let lines = input.components(separatedBy: "\n")
        return try lines
            .map { l in try score1(l) }
            .reduce(0, +)
    }

    static func score1(_ line: String) throws -> Int {
        let parts = line.components(separatedBy: " ")
        let theirs = try Hand(parts[0])
        let mine = try Hand(parts[1])

        if mine == theirs {
            return mine.value() + 3
        } else if mine.beats() == theirs {
            return mine.value() + 6
        } else {
            return mine.value()
        }
    }

    static func solve2(_ input: String) throws -> Int {
        let lines = input.components(separatedBy: "\n")
        return try lines
            .map { l in try score2(l) }
            .reduce(0, +)
    }

    static func score2(_ line: String) throws -> Int {
        let parts = line.components(separatedBy: " ")
        let theirs = try Hand(parts[0])

        // X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win
        let symbol = parts[1]
        switch symbol {
        case "X":
            return theirs.beats().value()
        case "Y":
            return theirs.value() + 3
        case "Z":
            return theirs.loses().value() + 6
        default: throw InputError.parseError("Can't parse \(symbol)")
        }
    }
}
