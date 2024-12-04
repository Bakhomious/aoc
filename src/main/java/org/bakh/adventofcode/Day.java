package org.bakh.adventofcode;

import lombok.Getter;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class Day {

    private final List<String> data;

    public Day(String fileName) {
        this.data = load(fileName);

        final var partOne = runPartOne();
        System.out.println("Part 1: " + partOne);

        final var partTwo = runPartTwo();
        System.out.println("Part 2: " + partTwo);
    }

    public abstract String runPartOne();
    public abstract String runPartTwo();

    private List<String> load(String fileName) {
        try {
            final var filePath = Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI();
            try (var lines = Files.lines(Paths.get(filePath))) {
                return lines.toList();
            }
        } catch (IOException | URISyntaxException e) {
            Logger.error("Could not load file: " + fileName);
            throw new RuntimeException();
        }
    }

}
