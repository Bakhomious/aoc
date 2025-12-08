package org.bakh.adventofcode.aoc2025;

import org.bakh.adventofcode.Day;
import org.bakh.adventofcode.utils.data.Range;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <a href="https://adventofcode.com/2025/day/5">Day 5: Cafeteria</a>
 */
public class Day05 extends Day<List<String>> {

    private List<String> ranges;
    private Set<Long> ids;

    public Day05(final String filename) {
        super(filename);
    }

    static void main() {
        new Day05("2025/day05.input").printParts();
    }

    @Override
    public String runPartOne() {
        loadRanges();
        final var freshIds = ids.stream()
            .filter(id -> isIdInRange(id, ranges))
            .count();

        return String.valueOf(freshIds);
    }

    private void loadRanges() {
        final var indexEmptyLine = getData().indexOf("");
        ranges = getData().subList(0, indexEmptyLine);
        ids = getData().subList(indexEmptyLine + 1, getData().size()).stream()
            .map(Long::parseLong)
            .collect(Collectors.toSet());
    }

    private Boolean isIdInRange(final Long targetId, final List<String> ranges) {
        final var mergedRanges = merge(ranges);
        for (final var range : mergedRanges) {
            if (targetId >= range.min() && targetId <= range.max()) {
                return true;
            }
        }
        return false;
    }

    private List<Range> merge(final List<String> ranges) {
        final var parsedRanges = parseAndSortRanges(ranges);
        final var mergedRanges = new LinkedList<Range>();

        mergedRanges.add(parsedRanges.getFirst());

        for (var i = 1; i < parsedRanges.size(); i++) {
            final var current = parsedRanges.get(i);
            final var lastMerged = mergedRanges.getLast();

            if (current.min() <= lastMerged.max() + 1) {
                final var newMax = Math.max(current.max(), lastMerged.max());
                mergedRanges.removeLast();
                mergedRanges.add(new Range(lastMerged.min(), newMax));
            } else {
                mergedRanges.add(current);
            }
        }
        return mergedRanges;
    }

    private List<Range> parseAndSortRanges(final List<String> ranges) {
        final var parsedRanges = new LinkedList<Range>();
        for (final var range : ranges) {
            final var split = range.split("-");
            final var min = Long.parseLong(split[0]);
            final var max = Long.parseLong(split[1]);
            parsedRanges.add(new Range(min, max));
        }

        parsedRanges.sort(Comparator.comparing(Range::min));
        return parsedRanges;
    }

    @Override
    public String runPartTwo() {
        loadRanges();
        final var result = countUnique(ranges);
        return String.valueOf(result);
    }

    private Long countUnique(final List<String> ranges) {
        final var mergedRanges = merge(ranges);
        var count = 0L;
        for (final var range : mergedRanges) {
            count += (range.max() - range.min()) + 1;
        }
        return count;
    }

}
