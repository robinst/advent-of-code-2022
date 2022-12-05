@testable import AOC2022
import XCTest

final class Day05Tests: XCTestCase {
    func testExample() throws {
        let example = """
            [D]
        [N] [C]
        [Z] [M] [P]
         1   2   3

        move 1 from 2 to 1
        move 3 from 1 to 3
        move 2 from 2 to 1
        move 1 from 1 to 2
        """
        XCTAssertEqual(try Day05.solve1(example), "CMZ")
        XCTAssertEqual(try Day05.solve2(example), "MCD")
    }

    func testInput() throws {
        let input = try Input.read("day05")
        XCTAssertEqual(try Day05.solve1(input), "WCZTHTMPS")
        XCTAssertEqual(try Day05.solve2(input), "BLSGJSDTS")
    }
}
