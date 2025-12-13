package org.bakh.adventofcode.utils.data;

public record Cell(
    Integer x,
    Integer y
) implements Comparable<Cell> {

    @Override
    public int compareTo(final Cell other) {
        final var yComp = this.y.compareTo(other.y);
        return yComp != 0 ? yComp : this.x.compareTo(other.x);
    }

}
