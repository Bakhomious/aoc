package org.bakh;

import lombok.Getter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
public abstract class Day<T> {

    private final Stream<String> data;

    protected Day(String fileName) {
        this.data = load(fileName);
    }

    private Stream<String> load(String fileName) {
        try {
            final var filePath = Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).toURI();
            return Files.lines(Paths.get(filePath));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void solve();

}
