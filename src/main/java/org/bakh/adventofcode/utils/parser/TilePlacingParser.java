package org.bakh.adventofcode.utils.parser;


import org.bakh.adventofcode.utils.data.Shape;
import org.bakh.adventofcode.utils.data.TilePlacing;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


@UtilityClass
public class TilePlacingParser {

    private static final Pattern SHAPE_HEADER_PATTERN = Pattern.compile("^(\\d+):$");
    private static final Pattern TILE_PLACING_PATTERN = Pattern.compile("^(\\d+)x(\\d+):\\s*(.*)$");

    public static ParseResult parseLines(final List<String> lines) {
        final var result = new ParseResult();

        Integer currentShapeId = null;
        final var shapeAsciiSb = new StringBuilder();

        for (var line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            final var shapeHeaderMatcher = SHAPE_HEADER_PATTERN.matcher(line);
            final var tilesPlacingMatcher = TILE_PLACING_PATTERN.matcher(line);

            if (shapeHeaderMatcher.matches()) {
                flushShape(currentShapeId, shapeAsciiSb, result.shapeLibrary);
                currentShapeId = Integer.parseInt(shapeHeaderMatcher.group(1));
            } else if (tilesPlacingMatcher.matches()) {
                flushShape(currentShapeId, shapeAsciiSb, result.shapeLibrary);
                currentShapeId = null;

                final var width = Integer.parseInt(tilesPlacingMatcher.group(1));
                final var height = Integer.parseInt(tilesPlacingMatcher.group(2));
                final var shapeCountsStr = tilesPlacingMatcher.group(3);

                result.placings.add(new TilePlacing(width, height, parseCounts(shapeCountsStr)));
            } else {
                if (currentShapeId != null) {
                    shapeAsciiSb.append(line).append('\n');
                }
            }
        }

        return result;
    }

    private static void flushShape(
        final Integer id,
        final StringBuilder shapeAsciiSb,
        final List<Shape> shapeLibrary
    ) {
        if (id != null && !shapeAsciiSb.isEmpty()) {
            shapeLibrary.add(new Shape(id, shapeAsciiSb.toString()));
            shapeAsciiSb.setLength(0);
        }
    }

    private static List<Integer> parseCounts(final String shapeCountsStr) {
        return Arrays.stream(shapeCountsStr.split("\\s+"))
            .map(Integer::parseInt)
            .toList();
    }

    public static class ParseResult {

        public final List<Shape> shapeLibrary = new ArrayList<>();
        public final List<TilePlacing> placings = new ArrayList<>();

    }

}
