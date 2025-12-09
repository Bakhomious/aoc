package org.bakh.adventofcode.utils.data;

import org.bakh.adventofcode.utils.operations.Operations;

import java.util.List;


public record Polygon(
    List<Point> vertices,
    Integer n
) {

    public Polygon(final List<Point> vertices) {
        this(vertices, vertices.size());
    }

    public Boolean contains(final Rectangle r) {
        if (!isPointInside(r.centreX(), r.centreY())) {
            return false;
        }

        return !hasInteriorIntersection(r);
    }

    private Boolean isPointInside(final double x, final double y) {
        var inside = false;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            final var pi = vertices.get(i);
            final var pj = vertices.get(j);

            // min(yi, yj) < y <= max(yi,yj)
            final var withinYRange = Operations.xor((pi.y() > y), (pj.y() > y));
            if (withinYRange) {

                // xint = xi + (y - yi) * (xj - xi) / (yj - yi)
                final double edgeRun = pj.x() - pi.x();
                final double edgeRise = pj.y() - pi.y();
                final var pointRise = y - pi.y();

                final var intersectX = pi.x() + (edgeRun * (pointRise / edgeRise));

                if (x < intersectX) {
                    inside = !inside;
                }
            }
        }
        return inside;
    }

    private Boolean hasInteriorIntersection(final Rectangle r) {
        for (var i = 0; i < n; i++) {
            final var pA = vertices.get(i);
            final var pB = vertices.get((i + 1) % n);
            final var intersectingCandidate = new Rectangle(pA, pB);

            if (r.isIntersectedBy(intersectingCandidate)) {
                return true;
            }
        }
        return false;
    }

}
