package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;


/**
 * <a href="https://adventofcode.com/2025/day/3">Day 3: Lobby</a>
 */
public class Day03 extends Day<List<String>> {

    public Day03(final String filename) {
        super(filename);
    }

    static void main() {
        new Day03("2025/day03.input").printParts();
    }

    @Override
    public String runPartOne() {
        final var result = getData().stream()
            .map(s -> deque(s, 2))
            .mapToLong(Long::valueOf)
            .sum();

        return String.valueOf(result);
    }

    @Override
    public String runPartTwo() {
        final var result = getData().stream()
            .map(s -> deque(s, 12))
            .mapToLong(Long::valueOf)
            .sum();

        return String.valueOf(result);
    }

    private static String deque(final String s, final Integer targetLength) {
        final var n = s.length();
        var toRemove = n - targetLength;
        final Deque<Character> stack = new ArrayDeque<>();

        for (final var digit : s.toCharArray()) {
            while (
                !stack.isEmpty()
                    && stack.peekLast() < digit
                    && toRemove > 0
            ) {
                stack.removeLast();
                toRemove--;
            }
            stack.addLast(digit);
        }

        while (stack.size() > targetLength) {
            stack.removeLast();
        }

        final var sb = new StringBuilder();
        stack.forEach(sb::append);
        return sb.toString();
    }

}
