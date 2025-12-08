package org.bakh.adventofcode.aoc2024;

import org.bakh.adventofcode.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * <a href="https://adventofcode.com/2024/day/2">Day 2: Red-Nosed Reports</a>
 */
public class Day02 extends Day<List<String>> {

    private static final Integer ALLOWED_DIFFERENCE = 3;
    private List<List<Integer>> reports;

    public Day02(final String fileName) {
        super(fileName);
    }

    static void main() {
        new Day02("2024/day02.input").printParts();
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

    private boolean canBeSafeWithDampener(final List<Integer> report) {
        return isSafe(report)
            ? isSafe(report)
            : IntStream.range(0, report.size())
                .mapToObj(i -> removeElement(report, i))
                .anyMatch(this::isSafe);
    }

    private List<Integer> removeElement(final List<Integer> report, final int index) {
        final var newList = new ArrayList<>(report);
        newList.remove(index);
        return newList;
    }

    private boolean isSafe(final List<Integer> report) {
        return (isIncreasing(report) || isDecreasing(report))
            && differenceInRange(report);
    }

    private boolean isIncreasing(final List<Integer> report) {
        return IntStream.range(0, report.size() - 1)
            .allMatch(i -> report.get(i) < report.get(i + 1));
    }

    private boolean isDecreasing(final List<Integer> report) {
        return IntStream.range(0, report.size() - 1)
            .allMatch(i -> report.get(i) > report.get(i + 1));
    }

    private boolean differenceInRange(final List<Integer> report) {
        return IntStream.range(0, report.size() - 1)
            .allMatch(i -> Math.abs(report.get(i) - report.get(i + 1)) <= ALLOWED_DIFFERENCE);
    }

}
