package org.bakh.adventofcode.aoc2024;

import org.bakh.Day;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <a href="https://adventofcode.com/2024/day/1">Day 1: Historian Hysteria</a>
 */
public class One extends Day<Integer> {

    protected One() {
        super("2024/day1.input");
    }

    @Override
    public void solve() {
        final var leftList = new ArrayList<Integer>();
        final var rightList = new ArrayList<Integer>();

        getData()
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

        System.out.println("Part one solution: " + partOneSolution);

        final var countMap = leftList.stream()
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    n -> rightList.stream().filter(x -> x.equals(n)).count()
                )
            );

        final var partTwoSolution = countMap.entrySet().stream()
            .mapToLong(entry -> entry.getKey() * entry.getValue())
            .sum();

        System.out.println("Part two solution: " + partTwoSolution);
    }

    public static void main(String[] args) {
        final var one = new One();
        one.solve();
    }
}
