package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.ParserUtils;
import org.bakh.adventofcode.utils.data.Edge;
import org.bakh.adventofcode.utils.data.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * <a href="https://adventofcode.com/2025/day/8">Day 8: Playground</a>
 */
public class Day08 extends Day<List<Point>> {

    private final Integer limit;

    @SuppressWarnings("unused")
    public Day08(final String fileName) {
        this(fileName, 10);
    }

    public Day08(final String fileName, final Integer limit) {
        super(fileName, ParserUtils.POINTS);
        this.limit = limit;
    }

    static void main() {
        new Day08("2025/day08.input", 1000).printParts();
    }

    @Override
    public String runPartOne() {
        final var edges = generateAndSortPoints().stream()
            .limit(limit)
            .toList();

        return runAlgorithm(edges, false);
    }

    private List<Edge> generateAndSortPoints() {
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
        return edges;
    }

    /**
     * Runs the Kruskal's algorithm on a specific set of edges.
     * In this problem, treat the Junction Boxes as <b>Nodes</b> and Straight Line Distances as <b>Weights</b> .
     * <br>
     * Kruskal's minimal spanning tree algorithm is one of the efficient methods to find the minimum spanning tree of
     * a graph. A minimum spanning tree is a subgraph that connects all the vertices present in the main graph with the
     * least possible edges and minimum cost (sum of the weights assigned to each edge).
     *
     * @param edges The subset of edges to process (limited for Part 1, full for Part 2)
     * @param part2 If true, returns immediately when the graph becomes fully connected.
     * @return The Part 2 result (product of larges 3 circuits) if part2 is triggered,
     * otherwise the Part 1 result (group size product).
     */
    private String runAlgorithm(final List<Edge> edges, final boolean part2) {
        final var groups = IntStream.range(0, getData().size())
            .boxed()
            .collect(
                Collectors.toMap(
                    i -> i,
                    i -> {
                        final var list = new ArrayList<Point>();
                        list.add(getData().get(i));
                        return list;
                    }
                )
            );

        final var pointToGroupId = new HashMap<Point, Integer>();
        for (var i = 0; i < getData().size(); i++) {
            pointToGroupId.put(getData().get(i), i);
        }

        for (final var edge : edges) {
            final var a = edge.a();
            final var b = edge.b();

            final var groupIdA = pointToGroupId.get(a);
            final var groupIdB = pointToGroupId.get(b);

            // Case: Same Group
            if (groupIdA.equals(groupIdB)) {
                continue;
            }

            // Case: Merging
            final var groupA = groups.get(groupIdA);
            final var groupB = groups.get(groupIdB);

            // Merge smaller group into bigger group
            if (groupB.size() > groupA.size()) {
                merge(
                    groupB,
                    groupA,
                    groupIdB,
                    groupIdA,
                    pointToGroupId,
                    groups
                );
            } else {
                merge(
                    groupA,
                    groupB,
                    groupIdA,
                    groupIdB,
                    pointToGroupId,
                    groups
                );
            }

            if (part2 && groups.size() == 1) {
                final var productLastTwoBoxes = a.x() * b.x();
                return String.valueOf(productLastTwoBoxes);
            }
        }

        final var product = groups.values().stream()
            .mapToLong(List::size)
            .boxed()
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .reduce(1L, (a, b) -> a * b);

        return String.valueOf(product);
    }

    /**
     * Simple Merger. Updates the lookup table for the eliminated list of points and
     * removes its group.
     *
     * @param survivor        List to keep (usually the bigger group)
     * @param victim          List to remove (usually the smaller group)
     * @param survivorGroupId Group ID of the list to keep in the look up table
     * @param victimGroupId   Group ID of the list to remove from the map
     * @param lookupTable     Look up table for the points
     * @param groups          Map of groups
     */
    private void merge(
        final List<Point> survivor,
        final List<Point> victim,
        final Integer survivorGroupId,
        final Integer victimGroupId,
        final Map<Point, Integer> lookupTable,
        final Map<Integer, ? extends List<Point>> groups
    ) {
        for (final var point : victim) {
            lookupTable.put(point, survivorGroupId);
        }

        survivor.addAll(victim);

        groups.remove(victimGroupId);
    }

    @Override
    public String runPartTwo() {
        final var edges = generateAndSortPoints().stream()
            .toList();

        return runAlgorithm(edges, true);
    }

}
