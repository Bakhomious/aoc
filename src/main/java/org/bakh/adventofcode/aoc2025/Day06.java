package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


/**
 * <a href="https://adventofcode.com/2025/day/5">Day 6: Trash Compactor</a>
 */
public class Day06 extends Day {

    private List<List<Long>> worksheet;
    private List<String> operators;

    public Day06(final String filename) {
        super(filename);
    }

    static void main() {
        new Day06("2025/day06.input");
    }

    @Override
    public String runPartOne() {
        loadGrid();
        final var results = new ArrayList<Long>();
        final var numCols = operators.size();
        for (var c = 0; c < numCols; c++) {
            final var op = operators.get(c);
            final var data = worksheet.get(c);

            var result = 0L;
            switch (op) {
                case "*" -> result = data.stream().reduce(1L, (a, b) -> a * b);
                case "+" -> result = data.stream().mapToLong(Long::longValue).sum();
                default -> throw new IllegalStateException("Unknown operator " + op);
            }

            results.add(result);
        }

        final var sum = results.stream()
            .mapToLong(Long::longValue)
            .sum();

        return String.valueOf(sum);
    }

    private void loadGrid() {
        operators = Arrays.stream(getData().getLast().trim().split("\\s+"))
            .filter(s -> !s.isEmpty())
            .toList();
        final var worksheetData = getData().subList(0, getData().size() - 1)
            .stream()
            .map(String::trim)
            .map(s -> s.split("\\s+"))
            .map(
                row -> Arrays.stream(row)
                    .map(Long::parseLong)
                    .toList()
            )
            .toList();

        final var numColumns = worksheetData.getFirst().size();

        // Transform into columnar data
        worksheet = IntStream.range(0, numColumns)
            .mapToObj(
                col -> worksheetData.stream()
                    .map(row -> row.get(col))
                    .toList()
            )
            .toList();
    }

    @Override
    public String runPartTwo() {
        final var paddedSheet = getGridWithPadding();
        final var height = paddedSheet.length;
        final var width = paddedSheet[0].length;

        final var results = new ArrayList<Long>();
        final var currentBlockCols = new ArrayList<Integer>();

        for (var col = 0; col < width; col++) {
            if (isSeparatorColumns(paddedSheet, col, height)) {
                if (!currentBlockCols.isEmpty()) {
                    results.add(solveBlock(paddedSheet, currentBlockCols));
                    currentBlockCols.clear();
                }
            } else {
                currentBlockCols.add(col);
            }
        }

        if (!currentBlockCols.isEmpty()) {
            results.add(solveBlock(paddedSheet, currentBlockCols));
        }

        return String.valueOf(results.stream().mapToLong(Long::longValue).sum());
    }

    private Long solveBlock(final char[][] grid, final List<Integer> cols) {
        final var height = grid.length;
        final var operatorRow = height - 1;

        String op = null;
        for (final var c : cols) {
            final var ch = grid[operatorRow][c];
            if (ch == '*' || ch == '+') {
                op = String.valueOf(ch);
                break;
            }
        }

        final var numbers = new ArrayList<Long>();
        for (var i = cols.size() - 1; i >= 0; i--) {
            final var c = cols.get(i);
            final var sb = new StringBuilder();

            for (var r = 0; r < operatorRow; r++) {
                final var ch = grid[r][c];
                if (Character.isDigit(ch)) {
                    sb.append(ch);
                }
            }

            if (!sb.isEmpty()) {
                numbers.add(Long.parseLong(sb.toString()));
            }
        }

        assert op != null;

        return switch (op) {
            case "*" -> numbers.stream().reduce(1L, (a, b) -> a * b);
            case "+" -> numbers.stream().mapToLong(Long::longValue).sum();
            default -> throw new IllegalStateException("Unknown operator " + op);
        };

    }

    private char[][] getGridWithPadding() {
        final var lines = getData();
        final var height = lines.size();
        final var width = lines.stream().mapToInt(String::length).max().orElseThrow();

        final var grid = new char[height][width];
        for (var row = 0; row < height; row++) {
            final var line = lines.get(row);
            for (var col = 0; col < width; col++) {
                if (col < line.length()) {
                    grid[row][col] = line.charAt(col);
                } else {
                    grid[row][col] = ' ';
                }
            }
        }
        return grid;
    }

    private boolean isSeparatorColumns(final char[][] grid, final int col, final int height) {
        for (var row = 0; row < height - 1; row++) {
            if (grid[row][col] != ' ') {
                return false;
            }
        }
        return true;
    }

}
