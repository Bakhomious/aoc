package org.bakh.adventofcode.utils.data;

public record Edge(
    Point a,
    Point b,
    Long distance
) implements Comparable<Edge> {

    public Edge(final Point a, final Point b) {
        this(a, b, a.distance(b));
    }

    @Override
    public int compareTo(final Edge o) {
        return distance.compareTo(o.distance);
    }

}
