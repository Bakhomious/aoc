package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;


/**
 * <a href="https://adventofcode.com/2025/day/1">Day 1: Secret Entrance/a>
 */
public class Day01 extends Day {

    public Day01(final String fileName) {
        super(fileName);
    }

    static void main(final String[] args) {
        new Day01("2025/day01.input");
    }

    @Override
    public String runPartOne() {
        final var dialSize = 100;

        var position = 50;
        var zeroCount = 0;

        for (final var entry : getData()) {
            final var rotation = entry.charAt(0);
            final var value = Integer.parseInt(entry.substring(1));

            if (rotation == 'R') {
                position = (position + value) % dialSize;
            } else {
                position = (position - value) % dialSize;
            }

            if (position == 0) {
                zeroCount += 1;
            }

        }

        return String.valueOf(zeroCount);
    }

    @Override
    public String runPartTwo() {
        final var dialSize = 100;

        var currentPosition = 50;
        var zeroCount = 0;

        for (final var entry : getData()) {
            final var rotation = entry.charAt(0);
            var value = Integer.parseInt(entry.substring(1));
            var nextRawValue = 0;

            if (value > 100) {
                zeroCount += value / dialSize;
                value = value % dialSize;
            }

            if (rotation == 'R') {
                nextRawValue = currentPosition + value;
                if (nextRawValue % dialSize == 0 || nextRawValue > dialSize) {
                    zeroCount++;
                }
            } else {
                nextRawValue = currentPosition - value;
                if (nextRawValue <= 0) {
                    if (currentPosition > 0) {
                        zeroCount++;
                    }
                }
            }

            currentPosition = nextRawValue % dialSize;
            if (currentPosition < 0) {
                currentPosition += dialSize;
            }

        }
        return String.valueOf(zeroCount);
    }

}
