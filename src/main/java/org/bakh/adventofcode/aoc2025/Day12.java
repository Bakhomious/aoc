package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.data.Cell;
import org.bakh.adventofcode.utils.data.Shape;
import org.bakh.adventofcode.utils.data.TilePlacing;
import org.bakh.adventofcode.utils.parser.TilePlacingParser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;


/**
 * <a href="https://adventofcode.com/2025/day/12">Day 12: Christmas Tree Farm</a>
 */
public class Day12 extends Day<List<String>> {

    private final List<Shape> shapes;
    private final List<TilePlacing> regions;

    public Day12(final String fileName) {
        super(fileName);
        final var parsed = TilePlacingParser.parseLines(getData());
        shapes = parsed.shapeLibrary;
        regions = parsed.placings;
    }

    static void main() {
        new Day12("2025/day12.input").printParts();
    }

    @Override
    public String runPartOne() {
        final var validPlacings = regions.parallelStream()
            .filter(this::giftAreas)
            .map(this::placeGiftsUnderTree)
            .filter(Objects::nonNull)
            .count();
        return String.valueOf(validPlacings);
    }

    @Override
    public String runPartTwo() {
        return "No Part Two. Merry Christmas!";
    }

    /**
     * For the REAL puzzle input (not the example), this is actually sufficient to solve the problem.
     * @param placing the tile placing
     * @return true if the total area of the placing is at least the total area of the gifts
     */
    private Boolean giftAreas(final TilePlacing placing) {
        final var totalArea = placing.width() * placing.height();
        final var giftArea = IntStream.range(0, placing.shapeCounts().size())
            .filter(i -> i < shapes.size() && placing.shapeCounts().get(i) > 0)
            .mapToLong(i -> (long) shapes.get(i).area() * placing.shapeCounts().get(i))
            .sum();
        return totalArea >= giftArea;
    }

    public int[][] placeGiftsUnderTree(final TilePlacing tilePlacing) {
        final var grid = new int[tilePlacing.height()][tilePlacing.width()];
        final var giftCounts = tilePlacing.shapeCounts();

        final var totalArea = tilePlacing.width() * tilePlacing.height();

        final var gifts = new LinkedHashMap<Shape, Integer>();
        IntStream.range(0, giftCounts.size())
            .filter(i -> i < shapes.size() && giftCounts.get(i) > 0)
            .mapToObj(shapes::get)
            .sorted((s1, s2) -> Integer.compare(s2.area(), s1.area()))
            .forEach(shape -> gifts.put(shape, giftCounts.get(shape.index())));

        final var takenArea = gifts.entrySet().stream()
            .mapToInt(e -> e.getKey().area() * e.getValue())
            .sum();

        // Gaps for heuristic purposes
        final var gapsNeeded = totalArea - takenArea;

        if (gapsNeeded > 0) {
            final var gapShape = new Shape(-2, "#");
            gifts.put(gapShape, gapsNeeded);
        }

        if (solve(grid, gifts, 0)) {
            return grid;
        }

        return null;
    }

    /**
     * Backtracking solver for placing gifts in the grid.
     * @param grid the grid to place gifts into
     * @param gifts the gifts to place
     * @param cellIndex the current cell index to try to place a gift
     * @return true if a solution is found
     */
    private boolean solve(
        final int[][] grid,
        final LinkedHashMap<Shape, Integer> gifts,
        int cellIndex
    ) {
        final var rows = grid.length;
        final var cols = grid[0].length;
        final var totalCells = rows * cols;


        var row = -1;
        var col = -1;

        while (cellIndex < totalCells) {
            final var r = cellIndex / cols;
            final var c = cellIndex % cols;
            if (grid[r][c] == 0) {
                row = r;
                col = c;
                break;
            }
            cellIndex++;
        }

        if (row == -1) {
            return true;
        }

        for (final var entry : gifts.entrySet()) {
            final var shape = entry.getKey();
            final var count = entry.getValue();

            if (count > 0) {
                for (final var rotation : shape.rotations()) {
                    if (canPlace(grid, rotation, row, col)) {
                        place(grid, rotation, row, col, shape.index() + 1);
                        entry.setValue(count - 1);

                        if (solve(grid, gifts, cellIndex + 1)) {
                            return true;
                        }

                        entry.setValue(count);
                        place(grid, rotation, row, col, 0);
                    }
                }
            }
        }

        return false;
    }

    private void place(
        final int[][] grid,
        final List<Cell> shapeCells,
        final int startRow,
        final int startCol,
        final int shapeIndex
    ) {
        for (final var cell : shapeCells) {
            grid[startRow + cell.y()][startCol + cell.x()] = shapeIndex;
        }
    }

    private boolean canPlace(final int[][] grid, final List<Cell> shapeCells, final int startRow, final int startCol) {
        final var rows = grid.length;
        final var cols = grid[0].length;

        for (final var cell : shapeCells) {
            final var r = startRow + cell.y();
            final var c = startCol + cell.x();

            if (r < 0 || r >= rows || c < 0 || c >= cols) {
                return false;
            }

            if (grid[r][c] != 0) {
                return false;
            }
        }
        return true;
    }

}
