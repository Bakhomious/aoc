package org.bakh.adventofcode.aoc2024;

import org.bakh.adventofcode.Day;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2024/day/3">Day 3: Mull It Over</a>
 */
public class Three extends Day {

    private static final String MUL_EXPR = "mul\\((\\d{1,3}),(\\d{1,3})\\)";
    private static final Pattern MUL_PATTERN = Pattern.compile(MUL_EXPR);

    protected Three(String fileName) {
        super(fileName);
    }

    @Override
    protected String runPartOne() {
        final var instructionResults = getData().stream()
            .flatMap(this::processPatternMatcher)
            .mapToInt(Integer::intValue)
            .sum();

        return String.valueOf(instructionResults);
    }

    @Override
    protected String runPartTwo() {
        final var dataStream = String.join("", getData());
        // First we split on `do()`
        // Then we split them on the first `don't()` occurrence, ignore the second part (index 1) as it's deactivated
        final var conditionalResults = Arrays.stream(dataStream.split("do\\(\\)"))
            .map(part -> part.split("don't\\(\\)", 2)[0])
            .flatMap(this::processPatternMatcher)
            .mapToInt(Integer::intValue)
            .sum();
        return String.valueOf(conditionalResults);
    }

    private Stream<Integer> processPatternMatcher(final String line) {
        final var matcher = MUL_PATTERN.matcher(line);
        return Stream.generate(() -> matcher.find() ? matcher : null)
            .takeWhile(Objects::nonNull)
            .map(this::parseResult);
    }

    private Integer parseResult(final Matcher matcher) {
        final var x = Integer.parseInt(matcher.group(1));
        final var y = Integer.parseInt(matcher.group(2));
        return x * y;
    }

    public static void main(String[] args) {
        new Three("2024/day3.input");
    }
}
