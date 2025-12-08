package org.bakh.adventofcode.aoc2024;

import org.bakh.adventofcode.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <a href="https://adventofcode.com/2024/day/5">Day 5: Print Queue</a>
 */
public class Day05 extends Day<List<String>> {

    private static final String PAGES_SEPARATOR = "\\|";
    private static final String DATA_SEPARATOR = "";

    public Day05(final String fileName) {
        super(fileName);
    }

    static void main() {
        new Day05("2024/day05.input").printParts();
    }

    @Override
    public String runPartOne() {
        final var rules = parseRules();
        final var updates = parseUpdates();

        final var validUpdatesSum = updates.stream()
            .filter(update -> isValid(rules, update))
            .mapToInt(update -> update.get(update.size() / 2))
            .sum();

        return String.valueOf(validUpdatesSum);
    }

    @Override
    public String runPartTwo() {
        final var rules = parseRules();
        final var updates = parseUpdates();

        final var updatedOrderSum = updates.stream()
            .filter(update -> !isValid(rules, update))
            .mapToInt(update -> orderUpdate(rules, update).get(update.size() / 2))
            .sum();

        return String.valueOf(updatedOrderSum);
    }

    private static List<Integer> orderUpdate(
        final Map<Integer, Set<Integer>> rules,
        final List<Integer> update
    ) {
        final var fixed = new int[update.size()];
        for (final int page : update) {
            var fixedIndex = 0;
            for (final var entry : rules.entrySet()) {
                if (entry.getValue().contains(page) && update.contains(entry.getKey())) {
                    fixedIndex++;
                }
            }
            fixed[fixedIndex] = page;
        }
        return Arrays.stream(fixed)
            .boxed()
            .toList();
    }

    private Map<Integer, Set<Integer>> parseRules() {
        final var separator = getData().indexOf(DATA_SEPARATOR);

        return getData().subList(0, separator).stream()
            .map(page -> page.split(PAGES_SEPARATOR))
            .collect(
                Collectors.toMap(
                    tokens -> Integer.parseInt(tokens[0]),
                    tokens -> {
                        final Set<Integer> values = new HashSet<>();
                        values.add(Integer.parseInt(tokens[1]));
                        return values;
                    },
                    (valuesList, duplicateKeyList) -> {
                        valuesList.addAll(duplicateKeyList);
                        return valuesList;
                    }
                )
            );
    }

    private List<List<Integer>> parseUpdates() {
        final var separator = getData().indexOf(DATA_SEPARATOR) + 1;
        final var rules = getData().subList(separator, getData().size());

        return rules.stream()
            .map(
                update -> Arrays.stream(update.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList())
            )
            .toList();
    }

    private static boolean isValid(
        final Map<Integer, Set<Integer>> rules,
        final List<Integer> update
    ) {
        for (var i = 0; i < update.size(); i++) {
            final var key = update.get(i);
            if (!rules.containsKey(key)) {
                return i == update.size() - 1;
            }
            for (final int page : update.subList(i + 1, update.size())) {
                if (!rules.get(key).contains(page)) {
                    return false;
                }
            }
        }
        return true;
    }

}
