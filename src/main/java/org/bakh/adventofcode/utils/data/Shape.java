package org.bakh.adventofcode.utils.data;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public record Shape(
    Integer index,
    List<List<Cell>> rotations
) {

    public Shape(final Integer index, final String asciiArt) {
        this(index, processRotations(asciiArt));
    }

    public Integer area() {
        return rotations.getFirst().size();
    }

    private static List<List<Cell>> processRotations(final String asciiArt) {
        final var baseShape = parse(asciiArt);

        final Set<List<Cell>> uniqueRotations = new LinkedHashSet<>();
        var current = baseShape;

        for (var i = 0; i < 4; i++) {
            uniqueRotations.add(normalize(current));
            current = rotate90(current);
        }

        return new ArrayList<>(uniqueRotations);
    }

    private static List<Cell> parse(final String ascii) {
        final List<Cell> cells = new ArrayList<>();
        final var lines = ascii.trim().split("\n");
        for (var y = 0; y < lines.length; y++) {
            final var chars = lines[y].trim().toCharArray();
            for (var x = 0; x < chars.length; x++) {
                if (chars[x] == '#') {
                    cells.add(new Cell(x, y));
                }
            }
        }
        return cells;
    }

    private static List<Cell> normalize(final List<Cell> shape) {
        if (shape.isEmpty()) {
            return shape;
        }

        final var minX = shape.stream()
            .mapToInt(Cell::x)
            .min()
            .orElse(0);
        final var minY = shape.stream()
            .mapToInt(Cell::y)
            .min()
            .orElse(0);

        return shape.stream()
            .map(c -> new Cell(c.x() - minX, c.y() - minY))
            .sorted()
            .toList();
    }

    private static List<Cell> rotate90(final List<Cell> shape) {
        // (x, y) -> (y, -x)
        return shape.stream()
            .map(c -> new Cell(c.y(), -c.x()))
            .toList();
    }

}
