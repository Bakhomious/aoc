package org.bakh.adventofcode.aoc2024;

import org.bakh.adventofcode.Day;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * <a href="https://adventofcode.com/2024/day/3">Day 3: Mull It Over</a>
 */
public class Day03 extends Day<List<String>> {

    private static final String MUL_EXPR = "mul\\((\\d{1,3}),(\\d{1,3})\\)";
    private static final Pattern MUL_PATTERN = Pattern.compile(MUL_EXPR);

    public Day03(final String fileName) {
        super(fileName);
    }

    static void main() {
        new Day03("2024/day03.input").printParts();
    }

    @Override
    public String runPartOne() {
        final var instructionResults = getData().stream()
            .flatMap(this::processPatternMatcher)
            .mapToInt(Integer::intValue)
            .sum();

        return String.valueOf(instructionResults);
    }

    @Override
    public String runPartTwo() {
        // Have to be one string, otherwise the answer would be invalid
        final var dataStream = String.join("", getData());

        final var conditionalResults = Stream.of(dataStream)
            .flatMap(this::processConditionalPatternMatcher)
            .mapToInt(Integer::intValue)
            .sum();

        return String.valueOf(conditionalResults);
    }

    private Stream<Integer> processConditionalPatternMatcher(final String data) {
        final var matcher = Pattern.compile("(^|do\\(\\)).+?(don't\\(\\)|$)").matcher(data);

        return Stream.generate(() -> matcher.find() ? matcher.group() : null)
            .takeWhile(Objects::nonNull)
            .flatMap(this::processPatternMatcher);
    }

    private Stream<Integer> processPatternMatcher(final String line) {
        final var matcher = MUL_PATTERN.matcher(line);
        return Stream.generate(() -> matcher.find() ? matcher : null)
            .takeWhile(Objects::nonNull)
            .filter(Objects::nonNull)
            .map(this::parseResult);
    }

    private Integer parseResult(final Matcher matcher) {
        final var x = Integer.parseInt(matcher.group(1));
        final var y = Integer.parseInt(matcher.group(2));
        return x * y;
    }

}
