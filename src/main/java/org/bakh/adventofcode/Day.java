package org.bakh.adventofcode;

import org.tinylog.Logger;

import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


@Getter
public abstract class Day {

    private final List<String> data;
    private final Type type;

    public Day(final String filename) {
        this(filename, Type.LINES);
    }

    public Day(final String fileName, final Type type) {
        this.type = type;
        this.data = load(fileName, type);

        final var partOne = runPartOne();
        System.out.println("Part 1: " + partOne);

        final var partTwo = runPartTwo();
        System.out.println("Part 2: " + partTwo);
    }

    private List<String> load(final String fileName, final Type type) {
        try {
            final var filePath = Objects.requireNonNull(
                getClass().getClassLoader().getResource(fileName)
            ).toURI();

            return switch (type) {
                case LINES -> getDataLines(filePath);
                case CSV -> getDataCsv(filePath);
            };

        } catch (final URISyntaxException e) {
            Logger.error("Could not load file: " + fileName);
            throw new RuntimeException();
        }
    }

    public abstract String runPartOne();

    public abstract String runPartTwo();

    private static List<String> getDataLines(final URI filePath) {
        try (final var lines = Files.lines(Paths.get(filePath))) {
            return lines.toList();
        } catch (final IOException e) {
            Logger.error("Could not load file: " + filePath);
            throw new RuntimeException();
        }
    }

    private static List<String> getDataCsv(final URI filePath) {
        try (final var lines = Files.lines(Paths.get(filePath))) {
            return Pattern.compile(",")
                .splitAsStream(lines.findFirst().orElseThrow())
                .map(String::trim)
                .toList();
        } catch (final IOException e) {
            Logger.error("Could not load file: " + filePath);
            throw new RuntimeException();
        }
    }

    public enum Type {
        LINES,
        CSV
    }

}
