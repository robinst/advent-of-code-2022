@testable import AOC2022
import XCTest

final class Day03Tests: XCTestCase {
    func testExample() throws {
        let example = """
        vJrwpWtwJgWrhcsFMMfFFhFp
        jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
        PmmdzqPrVvPwwTWBwg
        wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
        ttgJtRGJQctTZtZT
        CrZsJsPPZsGzwwsLwLmpwMDw
        """
        XCTAssertEqual(try Day03.solve1(example), 157)
        XCTAssertEqual(try Day03.solve2(example), 70)
    }

    func testInput() throws {
        let input = try Input.read("day03")
        XCTAssertEqual(try Day03.solve1(input), 7674)
        XCTAssertEqual(try Day03.solve2(input), 2805)
    }
}
