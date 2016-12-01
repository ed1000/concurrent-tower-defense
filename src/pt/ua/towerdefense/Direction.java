package pt.ua.towerdefense;

public enum Direction {
    NORTH(false),
    WEST(false),
    SOUTH(false),
    EAST(false),
    NORTHWEST(true),
    NORTHEAST(true),
    SOUTHWEST(true),
    SOUTHEAST(true);

    private boolean subdirection;

    Direction(boolean subdirection) {
        this.subdirection = subdirection;
    }

    public boolean isSubdirection() {
        return this.subdirection;
    }
}
