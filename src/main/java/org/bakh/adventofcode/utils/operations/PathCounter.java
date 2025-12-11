package org.bakh.adventofcode.utils.operations;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PathCounter {

    @Getter
    private final Map<String, List<String>> graph;

    private final Map<String, Long> cache = new HashMap<>();

    public PathCounter(final List<String> data) {
        graph = new HashMap<>();
        data.forEach(this::populateMap);
    }

    private void populateMap(final String line) {
        final var tokens = line.split(": ");
        final var node = tokens[0];
        final var neighbours = tokens[1].split(" ");
        graph.put(node, List.of(neighbours));
    }

    public Long countPathPassingThrough(
        final String start,
        final String end,
        final String point1,
        final String point2
    ) {
        final var sequence1 = countSegment(start, point1) * countSegment(point1, point2) * countSegment(point2, end);
        final var sequence2 = countSegment(start, point2) * countSegment(point2, point1) * countSegment(point1, end);

        return sequence1 + sequence2;
    }

    public Long countPathsFrom(final String start, final String end) {
        var total = 0L;

        for (final var node : graph.keySet()) {
            if (node.equals(start)) {
                total += countPaths(node, end);
            }
        }

        return total;
    }

    private Long countSegment(final String from, final String to) {
        cache.clear();
        return countPaths(from, to);
    }

    private Long countPaths(final String node, final String end) {
        if (node.equals(end)) {
            return 1L;
        }

        if (cache.containsKey(node)) {
            return cache.get(node);
        }

        var total = 0L;
        final var neighbours = graph.getOrDefault(node, Collections.emptyList());

        for (final var child : neighbours) {
            total += countPaths(child, end);
        }

        cache.put(node, total);
        return total;
    }

}
