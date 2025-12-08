package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.data.Grid;
import org.bakh.adventofcode.utils.operations.Operator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.bakh.adventofcode.utils.ParserUtils.SPATIAL_MATRIX;


public class Day06 extends Day<Grid> {

    public Day06(final String filename) {
        super(filename, SPATIAL_MATRIX);
    }

    @Override
    public String runPartOne() {
        final var operatorRow = getData().data().getLast();
        final var operators = parseOperators(operatorRow);

        final var gridRows = getData().data().subList(0, getData().height() - 1);
        final var numCols = operators.size();

        final var sum = IntStream.range(0, numCols)
            .mapToLong(
                i -> {
                    final var numbers = getNumbersInColumn(gridRows, i);
                    return operators.get(i).apply(numbers);
                }
            )
            .sum();

        return String.valueOf(sum);
    }

    /**
     * Spatial Matrix in this case matters (White spaces are important)
     */
    @Override
    public String runPartTwo() {
        final var grid = getData().data();
        final var width = getData().width();

        final List<Long> results = new ArrayList<>();
        final List<Integer> currentBlockCols = new ArrayList<>();

        for (var col = 0; col < width; col++) {
            if (isColumnEmpty(grid, col)) {
                // Similar to the idea of: Keep scanning until you hit a wall
                if (!currentBlockCols.isEmpty()) {
                    results.add(solveBlock(grid, currentBlockCols));
                    currentBlockCols.clear();
                }
            } else {
                currentBlockCols.add(col);
            }
        }

        if (!currentBlockCols.isEmpty()) {
            results.add(solveBlock(grid, currentBlockCols));
        }

        return String.valueOf(results.stream().mapToLong(Long::longValue).sum());
    }

    private boolean isColumnEmpty(final List<? extends List<Character>> grid, final Integer col) {
        for (var row = 0; row < grid.size() - 1; row++) {
            if (getChar(grid, row, col) != ' ') {
                return false;
            }
        }
        return true;
    }

    private long solveBlock(final List<? extends List<Character>> grid, final List<Integer> cols) {
        final var operatorRowIdx = grid.size() - 1;

        final var op = cols.stream()
            .map(c -> getChar(grid, operatorRowIdx, c))
            .map(String::valueOf)
            .filter(Operator::isSymbol)
            .map(Operator::from)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Block missing operator"));

        // Cephalopod is written right-to-left in columns
        final List<Long> numbers = new ArrayList<>();
        for (var i = cols.size() - 1; i >= 0; i--) {
            final int col = cols.get(i);
            final var sb = new StringBuilder();

            for (var row = 0; row < operatorRowIdx; row++) {
                final var c = getChar(grid, row, col);
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
            if (!sb.isEmpty()) {
                numbers.add(Long.parseLong(sb.toString()));
            }
        }

        return op.apply(numbers);
    }

    private char getChar(final List<? extends List<Character>> grid, final int r, final int c) {
        if (r < 0 || r >= grid.size()) {
            return ' ';
        }

        final var row = grid.get(r);
        if (c < 0 || c >= row.size()) {
            return ' ';
        }

        return row.get(c);
    }

    private List<Operator> parseOperators(final List<Character> row) {
        final var line = row.stream().map(String::valueOf).collect(Collectors.joining());
        return Arrays.stream(line.trim().split("\\s+"))
            .map(Operator::from)
            .toList();
    }

    private List<Long> getNumbersInColumn(
        final List<? extends List<Character>> grid, final int colIndex
    ) {
        return grid.stream()
            // Rejoin to trim whitespaces
            .map(
                chars -> chars.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining())
            )
            .map(String::trim)
            .map(line -> line.split("\\s+"))
            .map(tokens -> tokens[colIndex])
            .map(Long::parseLong)
            .toList();
    }

    static void main() {
        new Day06("2025/day06.input").printParts();
    }

}
