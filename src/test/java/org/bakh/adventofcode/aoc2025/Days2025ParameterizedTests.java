package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.tinylog.Logger;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class Days2025ParameterizedTests {

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
            Arguments.of(Day01.class, "2025/day01.input", "3", "6"),
            Arguments.of(Day02.class, "2025/day02.input", "1227775554", "4174379265"),
            Arguments.of(Day03.class, "2025/day03.input", "357", "3121910778619"),
            Arguments.of(Day04.class, "2025/day04.input", "13", "43"),
            Arguments.of(Day05.class, "2025/day05.input", "3", "14"),
            Arguments.of(Day06.class, "2025/day06.input", "4277556", "3263827"),
            Arguments.of(Day07.class, "2025/day07.input", "21", "40"),
            Arguments.of(Day08.class, "2025/day08.input", "40", "25272"),
            Arguments.of(Day08Alt.class, "2025/day08.input", "40", "25272"),
            Arguments.of(Day09.class, "2025/day09.input", "50", "24"),
            Arguments.of(Day10.class, "2025/day10.input", "7", "33")
        );
    }

    @SuppressWarnings("rawtypes")
    @MethodSource("testDataProvider")
    @ParameterizedTest
    void testDays(
        final Class<? extends Day> dayClass,
        final String fileName,
        final String expectedPart1,
        final String expectedPart2
    ) {
        try {
            final var day = dayClass.getDeclaredConstructor(String.class).newInstance(fileName);
            assertEquals(expectedPart1, day.runPartOne());
            assertEquals(expectedPart2, day.runPartTwo());
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
