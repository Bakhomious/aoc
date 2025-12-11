package org.bakh.adventofcode.utils.operations;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Gaussian {

    private static final double EPSILON = 1e-9;

    private final Double[][] matrix;
    private final Integer rows;
    private final Integer cols;
    private final Integer searchSteps;

    @Getter private final List<String> steps;

    public Gaussian(final Double[][] inputMatrix, final Integer searchSteps) {
        this.rows = inputMatrix.length;
        this.cols = inputMatrix[0].length - 1;
        this.matrix = new Double[rows][cols + 1];
        this.steps = new ArrayList<>();
        this.searchSteps = searchSteps;

        for (var i = 0; i < rows; i++) {
            System.arraycopy(
                inputMatrix[i],
                0,
                this.matrix[i],
                0,
                inputMatrix[i].length
            );
        }
    }

    /**
     * Reduces the matrix to Row Echelon Form and records every step.
     *
     * @return Array mapping [Column Index -> Pivot Row Index]. -1 means Free Variable.
     */
    public Integer[] reduce() {
        steps.clear();
        steps.add("Starting Reduction on " + rows + "x" + (cols + 1) + " matrix.");

        var pivotRow = 0;
        final var colToPivotRow = new Integer[cols];
        Arrays.fill(colToPivotRow, -1);

        for (var c = 0; c < cols && pivotRow < rows; c++) {
            steps.add("Processing Column " + c + "...");

            // Find Pivot
            var selectedRow = -1;
            for (var r = pivotRow; r < rows; r++) {
                if (Math.abs(matrix[r][c]) > EPSILON) {
                    selectedRow = r;
                    break;
                }
            }

            if (selectedRow == -1) {
                steps.add("  -> No pivot found. Variable x" + c + " is FREE.");
                continue;
            }

            // Swap Rows
            if (selectedRow != pivotRow) {
                swapRows(pivotRow, selectedRow);
                steps.add("  -> Swapped Row " + pivotRow + " with Row " + selectedRow);
            }

            // Normalize Pivot Row (Make pivot = 1.0)
            final double pivotVal = matrix[pivotRow][c];
            if (Math.abs(pivotVal - 1.0) > EPSILON) {
                divideRow(pivotRow, pivotVal);
                steps.add(String.format("  -> Normalized Row %d by dividing by %.2f", pivotRow, pivotVal));
            }

            // Eliminate Rows Below
            for (var r = 0; r < rows; r++) {
                if (r != pivotRow && Math.abs(matrix[r][c]) > EPSILON) {
                    final double factor = matrix[r][c];
                    subtractRow(r, pivotRow, factor);
                    steps.add(String.format("  -> Eliminated from Row %d using factor %.2f", r, factor));
                }
            }

            colToPivotRow[c] = pivotRow;
            pivotRow++;
        }

        steps.add("Reduction Complete.");
        return colToPivotRow;
    }

    private void swapRows(final int r1, final int r2) {
        final var temp = matrix[r1];
        matrix[r1] = matrix[r2];
        matrix[r2] = temp;
    }

    private void divideRow(final int r, final double divisor) {
        for (var c = 0; c <= cols; c++) {
            matrix[r][c] /= divisor;
        }
    }

    private void subtractRow(final int targetRow, final int sourceRow, final double factor) {
        for (var c = 0; c <= cols; c++) {
            matrix[targetRow][c] -= factor * matrix[sourceRow][c];
        }
    }

    /**
     * After reduction, finds the integer solution that minimizes the sum.
     */
    public long findMinIntegerSolution(final Integer[] colToPivotRow) {
        final List<Integer> freeVars = new ArrayList<>();
        for (var c = 0; c < cols; c++) {
            if (colToPivotRow[c] == -1) {
                freeVars.add(c);
            }
        }

        steps.add("Starting Search. Free Variables: " + freeVars);

        return recursiveSearch(0, freeVars, new Long[cols], colToPivotRow);
    }

    private long recursiveSearch(
        final Integer idx,
        final List<Integer> freeVars,
        final Long[] solution,
        final Integer[] colToPivotRow
    ) {
        if (idx == freeVars.size()) {
            return solveDependentVariables(solution, colToPivotRow);
        }

        final int varIndex = freeVars.get(idx);
        var minTotal = Long.MAX_VALUE;

        for (long val = 0; val <= searchSteps; val++) {
            solution[varIndex] = val;
            final var res = recursiveSearch(idx + 1, freeVars, solution, colToPivotRow);
            if (res != Long.MAX_VALUE) {
                minTotal = Math.min(minTotal, res);
            }
        }
        return minTotal;
    }

    private long solveDependentVariables(final Long[] solution, final Integer[] colToPivotRow) {
        for (var c = 0; c < cols; c++) {
            final var r = colToPivotRow[c];
            if (r != -1) { // Pivot Variable
                double val = matrix[r][cols]; // Start with Target Constant

                // Subtract all Free Variables
                for (var freeC = 0; freeC < cols; freeC++) {
                    if (colToPivotRow[freeC] == -1) {
                        val -= matrix[r][freeC] * solution[freeC];
                    }
                }

                // Validation: Must be non-negative integer
                if (val < -EPSILON || Math.abs(val - Math.round(val)) > EPSILON) {
                    return Long.MAX_VALUE;
                }
                solution[c] = Math.round(val);
            }
        }

        // Sum up total presses
        long sum = 0;
        for (final var s : solution) {
            sum += s;
        }
        return sum;
    }

}
