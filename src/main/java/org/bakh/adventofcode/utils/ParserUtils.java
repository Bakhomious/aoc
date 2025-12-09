package org.bakh.adventofcode.utils;

import org.bakh.adventofcode.utils.data.Grid;
import org.bakh.adventofcode.utils.data.Point;

import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    public static final Parser<List<Point>> POINTS = uri -> {
        try (final var lines = Files.lines(Paths.get(uri))) {
            return lines.map(s -> s.split(","))
                .map(
                    p -> {
                        if (p.length != 3) {
                            final var x = Integer.parseInt(p[0]);
                            final var y = Integer.parseInt(p[1]);
                            return new Point(x, y);
                        } else {
                            return new Point(p);
                        }
                    }
                )
                .toList();
        }
    };

    public static final Parser<Set<Point>> POINTS_SET = uri -> {
        try (final var lines = Files.lines(Paths.get(uri))) {
            return lines.map(s -> s.split(","))
                .map(Point::new)
                .collect(Collectors.toSet());
        }
    };

}
