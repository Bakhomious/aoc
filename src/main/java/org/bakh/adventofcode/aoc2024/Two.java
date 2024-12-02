package org.bakh.adventofcode.aoc2024;

import org.bakh.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2024/day/2">Day 2: Red-Nosed Reports</a>
 */
public class Two extends Day {

    private static final Integer ALLOWED_DIFFERENCE = 3;

    protected Two() {
        super("2024/day2.input");
    }

    @Override
    public void solve() {
        final var reports = getData()
            .map(
                line -> Stream.of(line.split(" "))
                    .map(Integer::parseInt)
                    .toList()
            )
            .toList();

        final var safeReports = reports.stream()
            .filter(this::isSafe)
            .count();

        System.out.println("Part one solution: " + safeReports);

        final var dampenedReports = reports.stream()
            .filter(this::canBeSafeWithDampener)
            .count();

        System.out.println("Part two solution: " + dampenedReports);
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
        final var two = new Two();
        two.solve();
//        two.getData().forEach(System.out::println);
    }

}
