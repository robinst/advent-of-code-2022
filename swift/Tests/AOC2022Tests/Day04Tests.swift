@testable import AOC2022
import XCTest

final class Day04Tests: XCTestCase {
    func testExample() throws {
        let example = """
        2-4,6-8
        2-3,4-5
        5-7,7-9
        2-8,3-7
        6-6,4-6
        2-6,4-8
        """
        XCTAssertEqual(try Day04.solve1(example), 2)
        XCTAssertEqual(try Day04.solve2(example), 4)
    }

    func testInput() throws {
        let input = try Input.read("day04")
        XCTAssertEqual(try Day04.solve1(input), 524)
        XCTAssertEqual(try Day04.solve2(input), 798)
    }
}
