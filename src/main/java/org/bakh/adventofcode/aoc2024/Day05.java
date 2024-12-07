package org.bakh.adventofcode.aoc2024;

import org.bakh.adventofcode.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * a href="https://adventofcode.com/2024/day/6">Day 6: Guard Gallivant</a>
 */
public class Day05 extends Day {

    private static final String PAGES_SEPARATOR = "\\|";
    private static final String DATA_SEPARATOR = "";

    public Day05(String fileName) {
        super(fileName);
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

    private Map<Integer, Set<Integer>> parseRules() {
        final var separator = getData().indexOf(DATA_SEPARATOR);

        final var rules = getData().subList(0, separator).stream()
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

        return rules;
    }

    private List<List<Integer>> parseUpdates() {
        final var separator = getData().indexOf(DATA_SEPARATOR) + 1;
        final var rules = getData().subList(separator, getData().size());

        final var updates = rules.stream()
            .map(
                update -> Arrays.stream(update.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList())
            )
            .toList();

        return updates;
    }

    private static boolean isValid(
        Map<Integer, Set<Integer>> rules,
        List<Integer> update
    ) {
        for (int i = 0; i < update.size(); i++) {
            var key = update.get(i);
            if (!rules.containsKey(key)) {
                return i == update.size() - 1;
            }
            for (int page : update.subList(i + 1, update.size())) {
                if (!rules.get(key).contains(page)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<Integer> orderUpdate(
        Map<Integer, Set<Integer>> rules,
        List<Integer> update
    ) {
        final var fixed = new int[update.size()];
        for (int page : update) {
            int fixedIndex = 0;
            for (var entry : rules.entrySet()) {
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

    public static void main(String[] args) {
        new Day05("2024/day05.input");
    }
}
