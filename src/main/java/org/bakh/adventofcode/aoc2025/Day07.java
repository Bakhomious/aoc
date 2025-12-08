package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.ParserUtils;
import org.bakh.adventofcode.utils.data.Grid;

import java.util.Arrays;

/**
 * <a href="https://adventofcode.com/2025/day/7">Day 7: Laboratories</a>
 */
public class Day07 extends Day<Grid> {

    public Day07(final String fileName) {
        super(fileName, ParserUtils.SPATIAL_MATRIX);
    }

    static void main() {
        new Day07("2025/day07.input").printParts();
    }

    @Override
    public String runPartOne() {
        final var splitTimes = processManifold().splitTimes();
        return String.valueOf(splitTimes);
    }

    @Override
    public String runPartTwo() {
        final var totalTimelines = processManifold().totalTimelines();
        return String.valueOf(totalTimelines);
    }

    private TachyonSimulation processManifold() {
        final var beamEntry = findBeamEntry();
        final var height = getData().height();
        final var width = getData().width();

        final var timelineCounts = new long[width];
        timelineCounts[beamEntry] = 1;
        var splitTimes = 0;

        final var diagram = getData().data();

        for (var i = 1; i < height; i++) {
            final var row = diagram.get(i);
            for (var j = 0; j < width; j++) {
                if (row.get(j) == '^' && timelineCounts[j] > 0) {
                    splitTimes++;
                    timelineCounts[j - 1] += timelineCounts[j];
                    timelineCounts[j + 1] += timelineCounts[j];
                    timelineCounts[j] = 0;
                }
            }
        }

        final var totalTimelines = Arrays.stream(timelineCounts).sum();
        return new TachyonSimulation(splitTimes, totalTimelines);
    }

    private Integer findBeamEntry() {
        return getData()
            .data()
            .getFirst()
            .indexOf('S');
    }

    record TachyonSimulation(
        Integer splitTimes,
        Long totalTimelines
    ) {

    }

}
