package aoc2022;

import java.util.Collection;

public record PosBounds(int minX, int maxX, int minY, int maxY) {

    public static PosBounds calculate(Collection<Pos> positions) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Pos elf : positions) {
            minX = Math.min(elf.x(), minX);
            maxX = Math.max(elf.x(), maxX);
            minY = Math.min(elf.y(), minY);
            maxY = Math.max(elf.y(), maxY);
        }
        return new PosBounds(minX, maxX, minY, maxY);
    }

    public int size() {
        return (maxX - minX + 1) * (maxY - minY + 1);
    }
}
