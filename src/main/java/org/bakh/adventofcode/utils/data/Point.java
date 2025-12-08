package org.bakh.adventofcode.utils.data;

public record Point(
    int x,
    int y,
    int z
) {

    public Point(final String[] array) {
        this(
            Integer.parseInt(array[0]),
            Integer.parseInt(array[1]),
            Integer.parseInt(array[2])
        );
    }

    public Point(final int x, final int y) {
        this(x, y, 0);
    }

    public Point add(final Point p) {
        return new Point(x + p.x, y + p.y, z + p.z);
    }

    public Long distance(final Point b) {
        final var dx = (long) this.x - b.x;
        final var dy = (long) this.y - b.y;
        final var dz = (long) this.z - b.z;

        return (dx * dx) + (dy * dy) + (dz * dz);
    }

}
