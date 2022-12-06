@testable import AOC2022
import XCTest

final class Day06Tests: XCTestCase {
    func testExample() throws {
        XCTAssertEqual(try Day06.solve1("mjqjpqmgbljsphdztnvjfqwrcgsmlb"), 7)
        XCTAssertEqual(try Day06.solve1("bvwbjplbgvbhsrlpgdmjqwftvncz"), 5)
        XCTAssertEqual(try Day06.solve1("nppdvjthqldpwncqszvftbrmjlhg"), 6)
        XCTAssertEqual(try Day06.solve1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"), 10)
        XCTAssertEqual(try Day06.solve1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"), 11)
        
        XCTAssertEqual(try Day06.solve2("mjqjpqmgbljsphdztnvjfqwrcgsmlb"), 19)
        XCTAssertEqual(try Day06.solve2("bvwbjplbgvbhsrlpgdmjqwftvncz"), 23)
        XCTAssertEqual(try Day06.solve2("nppdvjthqldpwncqszvftbrmjlhg"), 23)
        XCTAssertEqual(try Day06.solve2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"), 29)
        XCTAssertEqual(try Day06.solve2("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"), 26)
    }

    func testInput() throws {
        let input = try Input.read("day06")
        XCTAssertEqual(try Day06.solve1(input), 1892)
        XCTAssertEqual(try Day06.solve2(input), 2313)
    }
}
