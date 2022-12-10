struct Pos: Hashable {
    var x: Int
    var y: Int

    init(_ x: Int, _ y: Int) {
        self.x = x
        self.y = y
    }

    static func + (left: Pos, right: Pos) -> Pos {
        return Pos(left.x + right.x, left.y + right.y)
    }

    static func directions() -> [Pos] {
        return [Pos(0, -1), Pos(1, 0), Pos(0, 1), Pos(-1, 0)]
    }
}

struct Grid<T> {
    var lines: [[T]]

    func get(_ pos: Pos) -> T? {
        if pos.y >= 0, pos.y < lines.count {
            let line = lines[pos.y]
            if pos.x >= 0, pos.x < line.count {
                return line[pos.x]
            }
        }
        return nil
    }

    func height() -> Int {
        return lines.count
    }

    func width() -> Int {
        return lines[0].count
    }

    func rowPositions(y: Int) -> [Pos] {
        return (0..<width()).map { x in Pos(x, y) }
    }

    func colPositions(x: Int) -> [Pos] {
        return (0..<height()).map { y in Pos(x, y) }
    }

    func allPositions() -> [Pos] {
        return (0..<height())
            .flatMap { y in
                (0..<width())
                    .map { x in Pos(x, y) }
            }
    }

    func cells(after: Pos, direction: Pos) -> [T] {
        var pos = after + direction
        var result: [T] = []
        while let cell = get(pos) {
            result.append(cell)
            pos = pos + direction
        }
        return result
    }
}

enum Day08 {
    static func solve1(_ input: String) -> Int {
        let grid = parse(input)
        var result = Set<Pos>()

        for y in 0..<grid.height() {
            result.formUnion(visible(positions: grid.rowPositions(y: y), grid: grid))
            result.formUnion(visible(positions: grid.rowPositions(y: y).reversed(), grid: grid))
        }
        for x in 0..<grid.width() {
            result.formUnion(visible(positions: grid.colPositions(x: x), grid: grid))
            result.formUnion(visible(positions: grid.colPositions(x: x).reversed(), grid: grid))
        }

        return result.count
    }

    static func visible(positions: [Pos], grid: Grid<Int>) -> Set<Pos> {
        var result = Set<Pos>()
        var previousHeight = -1
        for pos in positions {
            if let height = grid.get(pos) {
                if height > previousHeight {
                    previousHeight = height
                    result.insert(pos)
                }
            }
        }
        return result
    }

    static func solve2(_ input: String) -> Int {
        let grid = parse(input)
        let positions = grid.allPositions()

        return positions
            .map { pos in scenicScore(start: pos, grid: grid) }
            .max()!
    }

    static func scenicScore(start: Pos, grid: Grid<Int>) -> Int {
        return Pos.directions()
            .map { direction in viewingDistance(start: start, grid: grid, direction: direction) }
            .reduce(1, *)
    }

    static func viewingDistance(start: Pos, grid: Grid<Int>, direction: Pos) -> Int {
        let houseHeight = grid.get(start)!
        var distance = 0

        for height in grid.cells(after: start, direction: direction) {
            if height >= houseHeight {
                return distance + 1
            }

            distance += 1
        }
        return distance
    }

    static func parse(_ input: String) -> Grid<Int> {
        let lines = input.components(separatedBy: "\n")
        let gridLines = lines.map { line in
            Array(line).map { Int(String($0))! }
        }
        return Grid(lines: gridLines)
    }
}
