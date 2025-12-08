package org.bakh.adventofcode.utils.data;

public record Point(
    int x,
    int y
) {

    public Point add(final Point p) {
        return new Point(x + p.x, y + p.y);
    }

}
