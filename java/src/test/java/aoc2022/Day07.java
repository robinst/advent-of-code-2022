package aoc2022;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

sealed interface Node {
    String name();
}

record File(String name, int size) implements Node {
}

record Dir(String name, List<Node> entries, Dir parent) implements Node {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dir dir = (Dir) o;
        return name.equals(dir.name) && entries.equals(dir.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, entries);
    }
}

public class Day07 {

    public static long solve1(String input) {
        Dir root = buildFileSystem(input);

        var sizes = new HashMap<Dir, Long>();
        calculateDirSizes(root, sizes);

        return sizes.values().stream().filter(s -> s < 100000).mapToLong(Long::valueOf).sum();
    }

    public static long solve2(String input) {
        Dir root = buildFileSystem(input);

        var sizes = new HashMap<Dir, Long>();
        calculateDirSizes(root, sizes);

        var totalSize = sizes.get(root);
        var available = 70000000;
        var max = available - 30000000;

        var freeUpAtLeast = totalSize - max;
        return sizes.values().stream().filter(s -> s >= freeUpAtLeast).min(Long::compare).get();
    }

    private static Dir buildFileSystem(String input) {
        var dir = new Dir("/", new ArrayList<>(), null);
        var root = dir;

        var lines = input.lines().skip(1).toList();

        for (String line : lines) {
            if (line.startsWith("$ ls")) {
                // Nothing to do here
            } else if (line.startsWith("$ cd")) {
                var target = line.substring("$ cd ".length());
                if (target.equals("..")) {
                    dir = dir.parent();
                } else {
                    var targetDir = dir.entries().stream().filter(n -> n.name().equals(target)).findFirst();
                    dir = (Dir) targetDir.get();
                }
            } else if (line.startsWith("dir ")) {
                var dirName = line.substring("dir ".length());
                dir.entries().add(new Dir(dirName, new ArrayList<>(), dir));
            } else {
                var parts = line.split(" ");
                var size = Integer.parseInt(parts[0]);
                var name = parts[1];
                dir.entries().add(new File(name, size));
            }
        }
        return root;
    }

    private static long calculateDirSizes(Dir dir, Map<Dir, Long> sizes) {
        long sum = 0;
        for (Node entry : dir.entries()) {
            if (entry instanceof File) {
                sum += ((File) entry).size();
            } else {
                var dirSize = calculateDirSizes((Dir) entry, sizes);
                sum += dirSize;
            }
        }
        sizes.put(dir, sum);
        return sum;
    }

    public static void main(String[] args) throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day07.txt"));
        System.out.println(solve1(input));
        System.out.println(solve2(input));
    }

    @Test
    void example() {
        final String input = """
                $ cd /
                $ ls
                dir a
                14848514 b.txt
                8504156 c.dat
                dir d
                $ cd a
                $ ls
                dir e
                29116 f
                2557 g
                62596 h.lst
                $ cd e
                $ ls
                584 i
                $ cd ..
                $ cd ..
                $ cd d
                $ ls
                4060174 j
                8033020 d.log
                5626152 d.ext
                7214296 k
                """;
        assertEquals(95437, solve1(input));
        assertEquals(24933642, solve2(input));
    }

    @Test
    void input() throws Exception {
        var input = Resources.readString(Resources.class.getResource("/day07.txt"));
        assertEquals(1743217, solve1(input));
        assertEquals(8319096, solve2(input));
    }
}
