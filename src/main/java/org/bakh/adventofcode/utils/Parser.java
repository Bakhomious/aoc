package org.bakh.adventofcode.utils;

import java.io.IOException;
import java.net.URI;


@FunctionalInterface
public interface Parser<R> {

    R parse(URI uri) throws IOException;

}

