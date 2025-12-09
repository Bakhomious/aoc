package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.ParserUtils;
import org.bakh.adventofcode.utils.data.Point;
import org.bakh.adventofcode.utils.data.Polygon;
import org.bakh.adventofcode.utils.data.Rectangle;

import java.util.List;


/**
 * <a href="https://adventofcode.com/2025/day/9">Day 9: Movie Theater</a>
 */
public class Day09 extends Day<List<Point>> {

    public Day09(final String fileName) {
        super(fileName, ParserUtils.POINTS);
    }

    static void main() {
        new Day09("2025/day09.input").printParts();
    }

    @Override
    public String runPartOne() {
        final var result = largestRedRectangle();
        return String.valueOf(result);
    }

    private Long largestRedRectangle() {
        final var vertices = getData();
        var maxArea = 0L;
        final var n = vertices.size();

        for (var i = 0; i < n; i++) {
            for (var j = i + 1; j < n; j++) {
                final var p1 = vertices.get(i);
                final var p2 = vertices.get(j);

                maxArea = Math.max(maxArea, p1.area(p2));
            }
        }

        return maxArea;
    }

    @Override
    public String runPartTwo() {
        final var result = largestRectangleInPolygon();
        return String.valueOf(result);
    }

    private Long largestRectangleInPolygon() {
        final var vertices = getData();
        final var theater = new Polygon(vertices);
        final var n = theater.n();

        var maxArea = 0L;

        for (var i = 0; i < n; i++) {
            for (var j = i + 1; j < n; j++) {
                final var p1 = vertices.get(i);
                final var p2 = vertices.get(j);

                final var candidate = new Rectangle(p1, p2);

                if (candidate.area() <= maxArea) {
                    continue;
                }

                if (theater.contains(candidate)) {
                    maxArea = candidate.area();
                }
            }
        }

        return maxArea;
    }

}
