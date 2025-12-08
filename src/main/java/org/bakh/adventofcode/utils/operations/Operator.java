package org.bakh.adventofcode.utils.operations;

import java.util.Arrays;
import java.util.List;
import java.util.function.LongBinaryOperator;


public enum Operator {
    MULTIPLY("*", (a, b) -> a * b, 1L),
    ADD("+", Long::sum, 0L);

    private final String symbol;
    private final LongBinaryOperator mathOp;
    private final Long identity;

    Operator(
        final String symbol,
        final LongBinaryOperator mathOp,
        final Long identity
    ) {
        this.symbol = symbol;
        this.mathOp = mathOp;
        this.identity = identity;
    }

    public static Operator from(final String symbol) {
        return Arrays.stream(values())
            .filter(op -> op.symbol.equals(symbol))
            .findFirst()
            .orElseThrow();
    }

    public static boolean isSymbol(final String s) {
        return Arrays.stream(values())
            .anyMatch(op -> op.symbol.equals(s));
    }

    public Long apply(final List<Long> numbers) {
        return numbers.stream()
            .mapToLong(Long::longValue)
            .reduce(identity, mathOp);
    }
}
