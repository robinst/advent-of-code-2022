import Foundation

enum Day01 {
    static func solve1(_ input: String) -> Int {
        let elves = input.components(separatedBy: "\n\n")
        return elves.map { elf in
            elf.components(separatedBy: "\n")
                .compactMap { Int($0) }
                .reduce(0, +)
        }.max()!
    }

    static func solve2(_ input: String) -> Int {
        let elves = input.components(separatedBy: "\n\n")
        return elves.map { elf in
            elf.components(separatedBy: "\n")
                .compactMap { Int($0) }
                .reduce(0, +)
        }
        .sorted()
        .suffix(3)
        .reduce(0, +)
    }
}
