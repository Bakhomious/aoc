package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.Point;

import java.util.LinkedList;
import java.util.List;


/**
 * <a href="https://adventofcode.com/2025/day/3">Day 4: Printing Department</a>
 */
public class Day04 extends Day {

    private static final List<Point> directions = List.of(
        new Point(1, 0), // right
        new Point(-1, 0), // left
        new Point(0, 1), // down
        new Point(0, -1), // up
        new Point(1, 1), // down-right
        new Point(-1, 1), // down-left
        new Point(1, -1), // up-right
        new Point(-1, -1) // up-left
    );
    private char[][] paperDiagram;
    private int width;
    private int height;

    public Day04(final String filename) {
        super(filename);
    }

    private void loadGrid() {
        paperDiagram = getData().stream()
            .map(String::toCharArray)
            .toArray(char[][]::new);
        width = paperDiagram[0].length;
        height = paperDiagram.length;
    }

    static void main() {
        new Day04("2025/day04.input");
    }


    @Override
    public String runPartOne() {
        loadGrid();
        final var toForkLift = getRolls();
        return String.valueOf(toForkLift.size());
    }

    private List<Point> getRolls() {
        final var list = new LinkedList<Point>();
        for (var row = 0; row < height; row++) {
            for (var col = 0; col < width; col++) {
                if (paperDiagram[row][col] == '@' && canBeLifted(col, row)) {
                    list.add(new Point(col, row));
                }
            }
        }
        return list;
    }

    private Boolean canBeLifted(final int col, final int row) {
        var adjacentRolls = 0;
        for (final var direction : directions) {
            final var checkCol = col + direction.x();
            final var checkRow = row + direction.y();

            if (checkCol >= 0 && checkCol < width && checkRow >= 0 && checkRow < height) {
                if (paperDiagram[checkRow][checkCol] == '@') {
                    adjacentRolls++;
                }
            }
        }
        return adjacentRolls < 4;
    }

    private Integer removeRolls() {
        final var rollsToRemove = new LinkedList<Point>();
        for (var row = 0; row < height; row++) {
            for (var col = 0; col < width; col++) {
                if (paperDiagram[row][col] == '@' && canBeLifted(col, row)) {
                    rollsToRemove.add(new Point(col, row));
                }
            }
        }

        for (final var point : rollsToRemove) {
            paperDiagram[point.y()][point.x()] = '.';
        }

        return rollsToRemove.size();
    }

    @Override
    public String runPartTwo() {
        loadGrid();
        var totalRemoved = 0;
        var removedInPass = Integer.MIN_VALUE;

        while (removedInPass != 0) {
            removedInPass = removeRolls();
            totalRemoved += removedInPass;
        }

        return String.valueOf(totalRemoved);
    }

}
