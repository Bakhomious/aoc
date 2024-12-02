package org.bakh.adventofcode;

import lombok.Getter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
public abstract class Day {

    private final Stream<String> data;

    protected Day(String fileName) {
        this.data = load(fileName);

        final var partOne = runPartOne();
        System.out.println("Part 1: " + partOne);

        final var partTwo = runPartTwo();
        System.out.println("Part 2: " + partTwo);
    }

    protected abstract String runPartOne();
    protected abstract String runPartTwo();

    private Stream<String> load(String fileName) {
        try {
            final var filePath = Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI();
            return Files.lines(Paths.get(filePath));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
