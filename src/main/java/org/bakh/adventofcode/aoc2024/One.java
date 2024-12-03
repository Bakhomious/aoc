package org.bakh.adventofcode.aoc2024;

import org.bakh.adventofcode.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com/2024/day/1">Day 1: Historian Hysteria</a>
 */
public class One extends Day {

    private static final List<Integer> LEFT_LIST = new ArrayList<>();
    private static final List<Integer> RIGHT_LIST = new ArrayList<>();

    protected One(String fileName) {
        super(fileName);
    }

    @Override
    protected String runPartOne() {
        getData().stream()
            .map(x -> x.split(" +"))
            .forEach(x -> {
                LEFT_LIST.add(Integer.parseInt(x[0]));
                RIGHT_LIST.add(Integer.parseInt(x[1]));
            });

        LEFT_LIST.sort(Integer::compareTo);
        RIGHT_LIST.sort(Integer::compareTo);

        // Fallback in case of mismatching
        if (LEFT_LIST.size() != RIGHT_LIST.size()) {
            throw new RuntimeException("Lists have to be the same size and equal to the initial input size");
        }

        final var partOneSolution = IntStream.range(0, LEFT_LIST.size())
            .map(i -> Math.abs(LEFT_LIST.get(i) - RIGHT_LIST.get(i)))
            .sum();

        return String.valueOf(partOneSolution);
    }

    @Override
    protected String runPartTwo() {
        final var countMap = LEFT_LIST.stream()
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    n -> RIGHT_LIST.stream().filter(x -> x.equals(n)).count()
                )
            );

        final var partTwoSolution = countMap.entrySet().stream()
            .mapToLong(entry -> entry.getKey() * entry.getValue())
            .sum();

        return String.valueOf(partTwoSolution);
    }

    public static void main(String[] args) {
        new One("2024/day1.input");
    }

}
