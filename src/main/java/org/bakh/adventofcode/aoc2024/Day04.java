package org.bakh.adventofcode.aoc2024;

import org.bakh.adventofcode.Day;

import java.util.List;


/**
 * <a href="https://adventofcode.com/2024/day/3">Day 4: Ceres Search</a>
 */
public class Day04 extends Day<List<String>> {

    private static final String WORD = "XMAS";
    private static final String REVERSED_WORD = new StringBuilder(WORD).reverse().toString();

    public Day04(final String fileName) {
        super(fileName);
    }

    static void main() {
        new Day04("2024/day04.input").printParts();
    }

    @Override
    public String runPartOne() {
        final var grid = getData().stream()
            .map(String::toCharArray)
            .toArray(char[][]::new);

        final var matches = countMatches(grid);

        return String.valueOf(matches);
    }

    @Override
    public String runPartTwo() {
        final var grid = getData().stream()
            .map(String::toCharArray)
            .toArray(char[][]::new);

        final var xmasPatterns = countXMasPattern(grid);

        return String.valueOf(xmasPatterns);
    }

    private Integer countXMasPattern(final char[][] grid) {
        var count = 0;
        final var rows = grid.length;
        final var cols = grid[0].length;

        for (var row = 1; row < rows; row++) {
            for (var col = 1; col < cols; col++) {
                count += xmasPattern(grid, row, col);
            }
        }

        return count;
    }

    private Integer xmasPattern(final char[][] grid, final int row, final int col) {
        var count = 0;

        // Center of the square is A
        if (grid[row][col] == 'A') {
            if (
                isValid(grid, row - 1, col - 1)
                    && isValid(grid, row + 1, col + 1)
                    && isValid(grid, row + 1, col - 1)
                    && isValid(grid, row - 1, col + 1)
            ) {
                // Check Cells around A if they have M and S
                if (grid[row - 1][col - 1] == 'M' && grid[row + 1][col + 1] == 'S' &&
                    grid[row + 1][col - 1] == 'M' && grid[row - 1][col + 1] == 'S') {
                    count++;
                }
                if (grid[row - 1][col - 1] == 'M' && grid[row + 1][col + 1] == 'S' &&
                    grid[row + 1][col - 1] == 'S' && grid[row - 1][col + 1] == 'M') {
                    count++;
                }
                if (grid[row - 1][col - 1] == 'S' && grid[row + 1][col + 1] == 'M' &&
                    grid[row + 1][col - 1] == 'M' && grid[row - 1][col + 1] == 'S') {
                    count++;
                }
                if (grid[row - 1][col - 1] == 'S' && grid[row + 1][col + 1] == 'M' &&
                    grid[row + 1][col - 1] == 'S' && grid[row - 1][col + 1] == 'M') {
                    count++;
                }
            }
        }

        return count;
    }

    private boolean isValid(final char[][] grid, final int row, final int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    private Integer countMatches(final char[][] grid) {
        var count = 0;
        final var rows = grid.length;
        final var cols = grid[0].length;

        final var directions = new int[][] {
            {0, 1}, // Right
            {1, 0}, // Down
            {1, 1}, // Diagonal Down-Right
            {1, -1} // Diagonal Down-Left
        };

        for (var row = 0; row < rows; row++) {
            for (var col = 0; col < cols; col++) {
                for (final var direction : directions) {
                    if (matchesWord(grid, row, col, direction, WORD)
                        || matchesWord(grid, row, col, direction, REVERSED_WORD)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private boolean matchesWord(
        final char[][] grid,
        final int row,
        final int col,
        final int[] direction,
        final String word
    ) {
        final var rows = grid.length;
        final var cols = grid[0].length;
        final var wordLength = word.length();

        for (var i = 0; i < wordLength; i++) {
            final var newRow = row + i * direction[0];
            final var newCol = col + i * direction[1];

            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                return false;
            }
            if (grid[newRow][newCol] != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }

}
