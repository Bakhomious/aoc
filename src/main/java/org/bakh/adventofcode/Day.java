package org.bakh.adventofcode;

import org.bakh.adventofcode.utils.Parser;
import org.tinylog.Logger;

import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.bakh.adventofcode.utils.ParserUtils.LINES;


@Getter
public abstract class Day<T> {

    private final T data;
    private URI filePath;

    public Day(final String fileName) {
        //noinspection unchecked
        this(fileName, (Parser<T>) LINES);
    }

    public Day(final String fileName, final Parser<T> parser) {
        this.data = load(fileName, parser);
    }

    private T load(final String fileName, final Parser<T> parser) {
        try {
            filePath = Objects.requireNonNull(
                getClass().getClassLoader().getResource(fileName)
            ).toURI();

            return parser.parse(filePath);
        } catch (final URISyntaxException | IOException | NullPointerException e) {
            Logger.error("Could not load file: " + fileName);
            throw new RuntimeException(e);
        }
    }

    public void printParts() {
        final var partOne = runPartOne();
        Logger.info("Part 1: " + partOne);

        final var partTwo = runPartTwo();
        Logger.info("Part 2: " + partTwo);
    }

    public abstract String runPartOne();

    public abstract String runPartTwo();

}
