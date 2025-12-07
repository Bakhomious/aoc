package org.bakh.adventofcode.utils;

public record Point(
    int x,
    int y
) {

    public Point add(final Point p) {
        return new Point(x + p.x, y + p.y);
    }

}
