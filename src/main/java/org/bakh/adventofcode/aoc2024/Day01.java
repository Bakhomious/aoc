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
public class Day01 extends Day {

    private List<Integer> leftList;
    private List<Integer> rightList;

    public Day01(String fileName) {
        super(fileName);
    }

    @Override
    public String runPartOne() {
        this.leftList = new ArrayList<>();
        this.rightList = new ArrayList<>();

        getData().stream()
            .map(x -> x.split(" +"))
            .forEach(x -> {
                leftList.add(Integer.parseInt(x[0]));
                rightList.add(Integer.parseInt(x[1]));
            });

        leftList.sort(Integer::compareTo);
        rightList.sort(Integer::compareTo);

        // Fallback in case of mismatching
        if (leftList.size() != rightList.size()) {
            throw new RuntimeException("Lists have to be the same size and equal to the initial input size");
        }

        final var partOneSolution = IntStream.range(0, leftList.size())
            .map(i -> Math.abs(leftList.get(i) - rightList.get(i)))
            .sum();

        return String.valueOf(partOneSolution);
    }

    @Override
    public String runPartTwo() {
        final var countMap = leftList.stream()
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    n -> rightList.stream().filter(x -> x.equals(n)).count(),
                    Long::sum
                )
            );

        final var partTwoSolution = countMap.entrySet().stream()
            .mapToLong(entry -> entry.getKey() * entry.getValue())
            .sum();

        return String.valueOf(partTwoSolution);
    }

    public static void main(String[] args) {
        new Day01("2024/day01.input");
    }

}
