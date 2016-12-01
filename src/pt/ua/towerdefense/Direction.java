package pt.ua.towerdefense;

public enum Direction {
    NORTH(false),
    WEST(false),
    SOUTH(false),
    EAST(false),
    NORTH_WEST(true),
    NORTH_EAST(true),
    SOUTH_WEST(true),
    SOUTH_EAST(true);

    private boolean subdirection;

    Direction(boolean subdirection) {
        this.subdirection = subdirection;
    }

    public boolean isSubdirection() {
        return this.subdirection;
    }
}
