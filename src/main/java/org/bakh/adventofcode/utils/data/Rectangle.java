package org.bakh.adventofcode.utils.data;

public record Rectangle(
    Long minX,
    Long maxX,
    Long minY,
    Long maxY
) {

    public Rectangle(final Point p1, final Point p2) {
        this(
            (long) Math.min(p1.x(), p2.x()),
            (long) Math.max(p1.x(), p2.x()),
            (long) Math.min(p1.y(), p2.y()),
            (long) Math.max(p1.y(), p2.y())
        );
    }

    public Long area() {
        final var width = maxX - minX + 1;
        final var height = maxY - minY + 1;
        return width * height;
    }

    public Double centreX() {
        return (minX + maxX) / 2.0;
    }

    public Double centreY() {
        return (minY + maxY) / 2.0;
    }

    public Boolean isIntersectedBy(final Rectangle other) {
        return isIntersectedBy(other.minX(), other.maxX(), other.minY(), other.maxY());
    }

    private Boolean isIntersectedBy(
        final Long edgeMinX,
        final Long edgeMaxX,
        final Long edgeMinY,
        final Long edgeMaxY
    ) {
        final var overlapsX = (edgeMinX < maxX) && (edgeMaxX > minX);
        final var overlapsY = (edgeMinY < maxY) && (edgeMaxY > minY);
        return overlapsX && overlapsY;
    }

}
