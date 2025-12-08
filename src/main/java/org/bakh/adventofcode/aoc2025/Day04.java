package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.ParserUtils;
import org.bakh.adventofcode.utils.data.Grid;
import org.bakh.adventofcode.utils.data.Point;

import java.util.LinkedList;
import java.util.List;


/**
 * <a href="https://adventofcode.com/2025/day/3">Day 4: Printing Department</a>
 */
public class Day04 extends Day<Grid> {

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

    public Day04(final String filename) {
        super(filename, ParserUtils.SPATIAL_MATRIX);
    }

    static void main() {
        new Day04("2025/day04.input").printParts();
    }

    @Override
    public String runPartOne() {
        final var toForkLift = getRolls();
        return String.valueOf(toForkLift.size());
    }

    @Override
    public String runPartTwo() {
        var totalRemoved = 0;
        var removedInPass = Integer.MIN_VALUE;

        while (removedInPass != 0) {
            removedInPass = removeRolls();
            totalRemoved += removedInPass;
        }

        return String.valueOf(totalRemoved);
    }

    private Integer removeRolls() {
        final var rollsToRemove = getRolls();

        for (final var point : rollsToRemove) {
            getData().data().get(point.y()).set(point.x(), '.');
        }

        return rollsToRemove.size();
    }

    private List<Point> getRolls() {
        final var list = new LinkedList<Point>();
        for (var row = 0; row < getData().height(); row++) {
            for (var col = 0; col < getData().width(); col++) {
                if (getData().data().get(row).get(col) == '@' && canBeLifted(col, row)) {
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

            if (checkCol >= 0 && checkCol < getData().width() && checkRow >= 0 && checkRow < getData().height()) {
                if (getData().data().get(checkRow).get(checkCol) == '@') {
                    adjacentRolls++;
                }
            }
        }
        return adjacentRolls < 4;
    }

}
