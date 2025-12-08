package org.bakh.adventofcode.utils.data;

import java.util.List;


public record Grid(
    List<? extends List<Character>> data,
    Integer height,
    Integer width
) {

    public Grid(final List<? extends List<Character>> data) {
        final var width = data.stream()
            .mapToInt(List::size)
            .max()
            .orElseThrow();

        this(data, data.size(), width);
    }

}
