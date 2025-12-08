package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.ParserUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;


/**
 * <a href="https://adventofcode.com/2025/day/2">Day 2: Gift Shop</a>
 */
public class Day02 extends Day<List<String>> {

    public Day02(final String filename) {
        super(filename, ParserUtils.CSV);
    }

    static void main() {
        new Day02("2025/day02.input").printParts();
    }

    @Override
    public String runPartOne() {
        final var result = getData()
            .stream().flatMapToLong(Day02::rangeSplit)
            .filter(this::isSequenceRepeatedTwice)
            .sum();

        return String.valueOf(result);
    }

    private static LongStream rangeSplit(final String s) {
        final var range = Arrays.asList(s.split("-"));
        if (range.size() > 2) {
            throw new IllegalStateException("Invalid range format");
        }
        final var min = Long.parseLong(range.getFirst());
        final var max = Long.parseLong(range.getLast());

        return LongStream.range(min, max + 1);
    }

    private Boolean isSequenceRepeatedTwice(final Long number) {
        final var strNumber = String.valueOf(number);
        final var length = strNumber.length();

        if (length % 2 != 0) {
            return false;
        }

        final var divisor = length / 2;
        final var sequence = strNumber.substring(0, divisor);

        return strNumber.equals(sequence + sequence);
    }

    @Override
    public String runPartTwo() {
        final var result = getData()
            .stream().flatMapToLong(Day02::rangeSplit)
            .filter(this::isSequenceRepeatedAtLeastTwiceOptimized)
            .sum();

        return String.valueOf(result);
    }

    /**
     * String doubling. Scans for the original string after doubling.
     * If it's less than the original length, returns true.
     */
    private Boolean isSequenceRepeatedAtLeastTwiceOptimized(final Long number) {
        final var s = String.valueOf(number);
        return (s + s).indexOf(s, 1) < s.length();
    }

    /**
     * Brute force solution. Very slow. Roughly O(n^2).
     */
    @SuppressWarnings("unused")
    private Boolean isSequenceRepeatedAtLeastTwice(final Long number) {
        final var strNumber = String.valueOf(number);
        final var length = strNumber.length();

        for (var d = 1; d < length; d++) {
            if (length % d != 0) {
                continue;
            }

            final var repetitions = length / d;

            if (repetitions >= 2) {
                final var sequence = strNumber.substring(0, d);
                if (strNumber.equals(sequence.repeat(repetitions))) {
                    return true;
                }
            }
        }

        return false;
    }

}
