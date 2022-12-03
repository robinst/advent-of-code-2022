@testable import AOC2022
import XCTest

final class Day02Tests: XCTestCase {
    func testExample() throws {
        let example = """
        A Y
        B X
        C Z
        """
        XCTAssertEqual(try Day02.solve1(example), 15)
        XCTAssertEqual(try Day02.solve2(example), 12)
    }

    func testInput() throws {
        let input = try Input.read("day02")
        XCTAssertEqual(try Day02.solve1(input), 12156)
        XCTAssertEqual(try Day02.solve2(input), 10835)
    }
}
