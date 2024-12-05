package org.bakh.adventofcode.aoc2024;

import org.bakh.adventofcode.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day05 extends Day {

    private static final String PAGES_SEPARATOR = "\\|";
    private static final String DATA_SEPARATOR = "";

    public Day05(String fileName) {
        super(fileName);
    }

    @Override
    public String runPartOne() {
        final var separator = getData().indexOf(DATA_SEPARATOR);

        // Key: pages needed to be printed before
        // Value: pages needed to be printed after
        final var pages = getData().subList(0, separator).stream()
            .map(page -> page.split(PAGES_SEPARATOR))
            .collect(
                Collectors.toMap(
                    tokens -> Integer.parseInt(tokens[0]),
                    tokens -> {
                        final var values = new ArrayList<Integer>();
                        values.add(Integer.parseInt(tokens[1]));
                        return values;
                    },
                    (valuesList, duplicateKeyList) -> {
                        valuesList.addAll(duplicateKeyList);
                        return valuesList;
                    }
                )
            );

        System.out.println(pages);

        return "";
    }

    @Override
    public String runPartTwo() {
        return "";
    }

    public static void main(String[] args) {
        new Day05("2024/day05.input");
    }
}
