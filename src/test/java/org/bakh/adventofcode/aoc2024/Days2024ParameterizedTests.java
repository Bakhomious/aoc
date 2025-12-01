package org.bakh.adventofcode.aoc2024;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.aoc2024.Day01;
import org.bakh.adventofcode.aoc2024.Day02;
import org.bakh.adventofcode.aoc2024.Day03;
import org.bakh.adventofcode.aoc2024.Day04;
import org.bakh.adventofcode.aoc2024.Day05;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Days2024ParameterizedTests {

    @ParameterizedTest
    @MethodSource("testDataProvider")
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

    static Stream<Arguments> testDataProvider() {
        return Stream.of(
            Arguments.of(Day01.class, "2024/day01.input", "11", "31"),
            Arguments.of(Day02.class, "2024/day02.input", "2", "4"),
            Arguments.of(Day03.class, "2024/day03.input", "161", "48"),
            Arguments.of(Day04.class, "2024/day04.input", "18", "9"),
            Arguments.of(Day05.class, "2024/day05.input", "143", "123")
        );
    }

}
