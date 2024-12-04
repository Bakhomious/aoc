package org.bakh.adventofcode.aoc2024;

import org.bakh.adventofcode.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2024/day/2">Day 2: Red-Nosed Reports</a>
 */
public class Day02 extends Day {

    private List<List<Integer>> reports;
    private static final Integer ALLOWED_DIFFERENCE = 3;

    public Day02(String fileName) {
        super(fileName);
    }

    @Override
    public String runPartOne() {
        this.reports = getData().stream()
            .map(
                line -> Stream.of(line.split(" "))
                    .map(Integer::parseInt)
                    .toList()
            )
            .toList();

        final var safeReports = reports.stream()
            .filter(this::isSafe)
            .count();

        return String.valueOf(safeReports);
    }

    @Override
    public String runPartTwo() {
        final var dampenedReports = reports.stream()
            .filter(this::canBeSafeWithDampener)
            .count();

        return String.valueOf(dampenedReports);
    }

    private boolean isSafe(List<Integer> report) {
        return (isIncreasing(report) || isDecreasing(report))
            && differenceInRange(report);
    }

    private boolean canBeSafeWithDampener(List<Integer> report) {
        return isSafe(report)
            ? isSafe(report)
            : IntStream.range(0, report.size())
                .mapToObj(i -> removeElement(report, i))
                .anyMatch(this::isSafe);
    }

    private boolean isIncreasing(List<Integer> report) {
        return IntStream.range(0, report.size() - 1)
            .allMatch(i -> report.get(i) < report.get(i + 1));
    }

    private boolean isDecreasing(List<Integer> report) {
        return IntStream.range(0, report.size() - 1)
            .allMatch(i -> report.get(i) > report.get(i + 1));
    }

    private boolean differenceInRange(List<Integer> report) {
        return IntStream.range(0, report.size() - 1)
            .allMatch(i -> Math.abs(report.get(i) - report.get(i + 1)) <= ALLOWED_DIFFERENCE);
    }

    private List<Integer> removeElement(List<Integer> report, int index) {
        final var newList = new ArrayList<>(report);
        newList.remove(index);
        return newList;
    }

    public static void main(String[] args) {
        new Day02("2024/day02.input");
    }
}
