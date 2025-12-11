package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.operations.PathCounter;

import java.util.List;


/**
 * <a href="https://adventofcode.com/2025/day/11">Day 11: Reactor</a>
 */
public class Day11 extends Day<List<String>> {

    public Day11(final String fileName) {
        super(fileName);
    }

    static void main() {
        new Day11("2025/day11.input").printParts();
    }

    @Override
    public String runPartOne() {
        final var pathCounter = new PathCounter(getData());
        final var result = pathCounter.countPathsFrom("you", "out");
        return String.valueOf(result);
    }

    @Override
    public String runPartTwo() {
        final var pathCounter = new PathCounter(getData());
        final var result = pathCounter.countPathPassingThrough(
            "svr",
            "out",
            "dac",
            "fft"
        );
        return String.valueOf(result);
    }


}
