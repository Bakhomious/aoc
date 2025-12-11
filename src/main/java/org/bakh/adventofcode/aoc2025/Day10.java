package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.operations.Gaussian;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * <a href="https://adventofcode.com/2025/day/10">Day 10: Factory</a>
 */
public class Day10 extends Day<List<String>> {

    private final List<Machine> machines;

    public Day10(final String fileName) {
        super(fileName);
        machines = getData().stream()
            .map(Machine::new)
            .toList();
    }

    static void main() {
        new Day10("2025/day10.input").printParts();
    }

    @Override
    public String runPartOne() {
        final var result = machines.stream()
            .map(Machine::turnOn)
            .mapToLong(Long::valueOf)
            .sum();
        return String.valueOf(result);
    }

    @Override
    public String runPartTwo() {
        final var result = machines.stream()
            .map(Machine::tweakJoltage)
            .mapToLong(Long::valueOf)
            .sum();
        return String.valueOf(result);
    }

    record Machine(
        BitSet targetState,
        List<Integer> joltageTarget,
        List<BitSet> buttonMasks,
        Integer numLights
    ) {

        private static final Pattern TARGET_PATTERN = Pattern.compile("\\[(.*?)]");
        private static final Pattern BUTTON_PATTERN = Pattern.compile("\\((.*?)\\)");
        private static final Pattern JOLTAGE_PATTERN = Pattern.compile("\\{(.*?)}");

        public Machine(final String line) {
            final var targetState = parseTarget(line);
            final var numLights = parseNumLights(line);
            final var buttons = parseButtons(line);
            final var joltage = parseJoltage(line);
            this(targetState, joltage, buttons, numLights);
        }

        private static BitSet parseTarget(final String line) {
            final var targetMatcher = TARGET_PATTERN.matcher(line);
            final var targetState = new BitSet();

            if (targetMatcher.find()) {
                final var lightsStr = targetMatcher.group(1);

                for (var i = 0; i < lightsStr.length(); i++) {
                    if (lightsStr.charAt(i) == '#') {
                        targetState.set(i);
                    }
                }
                return targetState;
            }
            throw new IllegalStateException("No target state found in line");
        }

        private static Integer parseNumLights(final String line) {
            final var targetMatcher = TARGET_PATTERN.matcher(line);
            return targetMatcher.find() ? targetMatcher.group(1).length() : 0;
        }

        private static ArrayList<BitSet> parseButtons(final String line) {
            final var buttonMatcher = BUTTON_PATTERN.matcher(line);
            final var buttons = new ArrayList<BitSet>();

            while (buttonMatcher.find()) {
                final var buttonMask = new BitSet();
                final var buttonStr = buttonMatcher.group(1);

                if (!buttonStr.isEmpty()) {
                    final var indices = buttonStr.split(",");

                    for (final var index : indices) {
                        buttonMask.set(Integer.parseInt(index));
                    }
                }
                buttons.add(buttonMask);
            }
            return buttons;
        }

        private static List<Integer> parseJoltage(final String line) {
            final var matcher = JOLTAGE_PATTERN.matcher(line);
            if (matcher.find()) {
                final var parts = matcher.group(1).split(",");
                return Stream.of(parts)
                    .map(Integer::valueOf)
                    .toList();
            }
            return Collections.emptyList();
        }

        public Long tweakJoltage() {
            final var matrix = buildAugmentedMatrix();
            final var gaussian = new Gaussian(matrix, 160);

            final var reduced = gaussian.reduce();

            final var result = gaussian.findMinIntegerSolution(reduced);

            return result == Long.MAX_VALUE
                ? 0
                : result;
        }

        private Double[][] buildAugmentedMatrix() {
            final var rows = joltageTarget.size();
            final var cols = buttonMasks.size();
            final var matrix = new Double[rows][cols + 1];

            for (var r = 0; r < rows; r++) {
                for (var c = 0; c < cols; c++) {
                    if (buttonMasks.get(c).get(r)) {
                        matrix[r][c] = 1.0;
                    } else {
                        matrix[r][c] = 0.0;
                    }
                }
                matrix[r][cols] = Double.valueOf(joltageTarget.get(r));
            }

            return matrix;
        }


        private void search(
            final Integer index,
            final BitSet state,
            final Integer count,
            final AtomicInteger minPresses
        ) {
            if (count >= minPresses.get()) {
                return;
            }

            if (index == buttonMasks.size()) {
                if (state.equals(targetState)) {
                    minPresses.set(count);
                }
                return;
            }

            final var mask = buttonMasks.get(index);

            // Don't Press Button
            search(index + 1, state, count, minPresses);

            // Press Button
            state.xor(mask);
            search(index + 1, state, count + 1, minPresses);

            // Backtrack
            state.xor(mask);
        }

        public Integer turnOn() {
            final var minPresses = new AtomicInteger(Integer.MAX_VALUE);

            search(0, new BitSet(), 0, minPresses);

            final var result = minPresses.get();
            return result == Integer.MAX_VALUE
                ? -1
                : result;
        }

    }

}
