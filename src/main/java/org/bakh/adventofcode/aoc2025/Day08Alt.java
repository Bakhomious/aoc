package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.ParserUtils;
import org.bakh.adventofcode.utils.data.DSU;
import org.bakh.adventofcode.utils.data.Edge;
import org.bakh.adventofcode.utils.data.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;


/**
 * <a href="https://adventofcode.com/2025/day/8">Day 8: Playground</a>
 * <br>
 * An alternative using DSU
 */
public class Day08Alt extends Day<List<Point>> {

    private final Integer limit;

    private List<Edge> cacheSortedEdges;

    @SuppressWarnings("unused")
    public Day08Alt(final String fileName) {
        this(fileName, 10);
    }

    public Day08Alt(final String fileName, final Integer limit) {
        super(fileName, ParserUtils.POINTS);
        this.limit = limit;
    }

    static void main() {
        new Day08Alt("2025/day08.input", 1000).printParts();
    }

    @Override
    public String runPartOne() {
        final var edges = generateAndSortPoints().stream()
            .limit(limit)
            .toList();

        return runAlgorithm(edges, false);
    }

    private List<Edge> generateAndSortPoints() {
        if (cacheSortedEdges != null) {
            return cacheSortedEdges;
        }
        final var n = getData().size();
        final var edges = new ArrayList<Edge>();

        for (var i = 0; i < n; i++) {
            for (var j = i + 1; j < n; j++) {
                edges.add(
                    new Edge(
                        getData().get(i),
                        getData().get(j)
                    )
                );
            }
        }

        Collections.sort(edges);
        this.cacheSortedEdges = edges;
        return edges;
    }

    private String runAlgorithm(final List<Edge> edges, final boolean part2) {
        final var points = getData();
        final var pointToIndex = new HashMap<Point, Integer>();
        for (var i = 0; i < points.size(); i++) {
            pointToIndex.put(points.get(i), i);
        }

        final var dsu = new DSU(points.size());

        for (final var edge : edges) {
            final var indexA = pointToIndex.get(edge.a());
            final var indexB = pointToIndex.get(edge.b());

            if (dsu.union(indexA, indexB)) {
                if (part2 && dsu.getNumberOfSets() == 1) {
                    final var productLastTwoBoxes = edge.a().x() * edge.b().x();
                    return String.valueOf(productLastTwoBoxes);
                }
            }
        }

        final var product = IntStream.range(0, points.size())
            .map(dsu::find)
            .distinct()
            .mapToLong(dsu::getSize)
            .boxed()
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .reduce(1L, (a, b) -> a * b);

        return String.valueOf(product);
    }

    @Override
    public String runPartTwo() {
        final var edges = generateAndSortPoints().stream()
            .toList();

        return runAlgorithm(edges, true);
    }

}
