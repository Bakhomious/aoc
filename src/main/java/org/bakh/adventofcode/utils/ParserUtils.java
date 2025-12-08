package org.bakh.adventofcode.utils;

import org.bakh.adventofcode.utils.data.Grid;

import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@UtilityClass
public class ParserUtils {

    public static final Parser<List<String>> LINES = uri -> {
        try (final var lines = Files.lines(Paths.get(uri))) {
            return lines.toList();
        }
    };

    public static final Parser<List<String>> CSV = uri -> {
        try (final var lines = Files.lines(Paths.get(uri))) {
            return Pattern.compile(",")
                .splitAsStream(lines.findFirst().orElseThrow())
                .map(String::trim)
                .toList();
        }
    };

    public static final Parser<Grid> SPATIAL_MATRIX = uri -> {
        try (final var lines = Files.lines(Paths.get(uri))) {
            final var data = lines.map(
                    row -> row.chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.toCollection(ArrayList::new))
                )
                .collect(Collectors.toCollection(ArrayList::new));

            return new Grid(data);
        }
    };

}
