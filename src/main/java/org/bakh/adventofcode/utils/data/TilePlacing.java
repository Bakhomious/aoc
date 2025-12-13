package org.bakh.adventofcode.utils.data;

import java.util.List;


public record TilePlacing(
    Integer width,
    Integer height,
    List<Integer> shapeCounts
) {

}
