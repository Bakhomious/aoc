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
            Arguments.of(Day01.class, "2025/day01.input", "3", "6")
        );
    }

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

}
