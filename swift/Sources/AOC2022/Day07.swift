protocol Node {
    var name: String { get }
}

class Dir: Node {
    var name: String
    var nodes: [Node] = []

    init(name: String) {
        self.name = name
    }
}

class File: Node {
    var name: String
    var size: Int

    init(name: String, size: Int) {
        self.name = name
        self.size = size
    }
}

enum Day07 {
    static func solve1(_ input: String) throws -> Int {
        let tree = tree(input)

        var sizes: [Int] = []
        calculateSizes(of: tree, in: &sizes)
        return sizes.filter { s in s < 100000 }.reduce(0, +)
    }

    static func solve2(_ input: String) throws -> Int {
        let tree = tree(input)

        var sizes: [Int] = []
        let totalSize = calculateSizes(of: tree, in: &sizes)

        let available = 70000000
        let max = available - 30000000

        let freeUpAtLeast = totalSize - max

        return sizes.filter { s in s >= freeUpAtLeast }.min()!
    }

    static func tree(_ input: String) -> Dir {
        let root = Dir(name: "/")
        var dirs: [Dir] = []
        var dir = root

        let lines = input.components(separatedBy: "\n")
        for line in lines {
            if line.starts(with: "$ ") {
                let cmd = line.deletingPrefix("$ ")
                if cmd.starts(with: "cd ") {
                    let target = cmd.deletingPrefix("cd ")
                    switch target {
                    case "..":
                        if !dirs.isEmpty {
                            dir = dirs.removeLast()
                        }
                    case "/":
                        dirs = []
                        dir = root
                    default:
                        dirs.append(dir)
                        dir = dir.nodes.first(where: { $0.name == target })! as! Dir
                    }
                }
            } else {
                // Not a comand, so a listing
                if line.starts(with: "dir ") {
                    let name = line.deletingPrefix("dir ")
                    dir.nodes.append(Dir(name: name))
                } else {
                    let parts = line.components(separatedBy: " ")
                    let size = Int(parts[0])!
                    let name = parts[1]
                    dir.nodes.append(File(name: name, size: size))
                }
            }
        }
        return root
    }

    @discardableResult
    static func calculateSizes(of tree: Dir, in sizes: inout [Int]) -> Int {
        var sum = 0
        for node in tree.nodes {
            switch node {
            case let dir as Dir:
                sum += calculateSizes(of: dir, in: &sizes)
            case let file as File:
                sum += file.size
            default:
                assertionFailure("Unexpected node type")
            }
        }
        sizes.append(sum)
        return sum
    }
}
