package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.tinylog.Logger;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class Days2025ParameterizedTests {

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
            Arguments.of(Named.of("Day 1", Day01.class), "2025/day01.input", "3", "6"),
            Arguments.of(Named.of("Day 2", Day02.class), "2025/day02.input", "1227775554", "4174379265"),
            Arguments.of(Named.of("Day 3", Day03.class), "2025/day03.input", "357", "3121910778619"),
            Arguments.of(Named.of("Day 4", Day04.class), "2025/day04.input", "13", "43"),
            Arguments.of(Named.of("Day 5", Day05.class), "2025/day05.input", "3", "14"),
            Arguments.of(Named.of("Day 6", Day06.class), "2025/day06.input", "4277556", "3263827"),
            Arguments.of(Named.of("Day 7", Day07.class), "2025/day07.input", "21", "40"),
            Arguments.of(Named.of("Day 8", Day08.class), "2025/day08.input", "40", "25272"),
            Arguments.of(Named.of("Day 8 Alt", Day08Alt.class), "2025/day08.input", "40", "25272"),
            Arguments.of(Named.of("Day 9", Day09.class), "2025/day09.input", "50", "24"),
            Arguments.of(Named.of("Day 10", Day10.class), "2025/day10.input", "7", "33"),
            Arguments.of(Named.of("Day 11 Part 1", Day11.class), "2025/day11.input", "5", null),
            Arguments.of(Named.of("Day 11 Part 2", Day11.class), "2025/day11_2.input", null, "2")
        );
    }

    @SuppressWarnings("rawtypes")
    @MethodSource("testDataProvider")
    @ParameterizedTest(name = "{0} [{1}]")
    void testDays(
        final Class<? extends Day> dayClass,
        final String fileName,
        final String expectedPart1,
        final String expectedPart2
    ) {
        try {
            final var day = dayClass.getDeclaredConstructor(String.class).newInstance(fileName);
            if (expectedPart1 != null) {
                assertEquals(expectedPart1, day.runPartOne());
            }
            if (expectedPart2 != null) {
                assertEquals(expectedPart2, day.runPartTwo());
            }
        } catch (
            final NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e
        ) {
            Logger.error("Cannot find suitable constructor for class {}", dayClass);
            throw new RuntimeException(e);
        }
    }

}
