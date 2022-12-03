enum Day03 {
    static func solve1(_ input: String) throws -> Int {
        let lines = input.components(separatedBy: "\n")
        return lines
            .map { l in score1(l) }
            .reduce(0, +)
    }

    static func score1(_ line: String) -> Int {
        let length = line.count / 2
        let a = line.prefix(length)
        let b = line.suffix(length)
        let setA = chars(a)
        let setB = chars(b)
        let common = setA.intersection(setB)
        return priority(common.first!)
    }

    static func solve2(_ input: String) throws -> Int {
        let lines = input.components(separatedBy: "\n")
        return stride(from: 0, to: lines.count, by: 3)
            .map { i in score2(lines[i], lines[i + 1], lines[i + 2]) }
            .reduce(0, +)
    }

    static func score2(_ a: String, _ b: String, _ c: String) -> Int {
        let common = chars(a).intersection(chars(b)).intersection(chars(c))
        return priority(common.first!)
    }

    static func chars(_ s: any StringProtocol) -> Set<UInt8> {
        return Set(s.map { $0.asciiValue! })
    }

    static func priority(_ c: UInt8) -> Int {
        if c >= "a".first!.asciiValue!, c <= "z".first!.asciiValue! {
            return Int(c - "a".first!.asciiValue! + 1)
        } else {
            return Int(c - "A".first!.asciiValue! + 27)
        }
    }
}
