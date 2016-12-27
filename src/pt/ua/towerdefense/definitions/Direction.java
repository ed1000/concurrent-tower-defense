package pt.ua.towerdefense.definitions;

/**
 * Enum that represents all the directions that a tower
 * or a monster can have.
 *
 * @author Eduardo Sousa
 */
public enum Direction {
    /**
     * Direction heading North.
     */
    NORTH(false),

    /**
     * Direction heading West.
     */
    WEST(false),

    /**
     * Direction heading South.
     */
    SOUTH(false),

    /**
     * Direction heading East.
     */
    EAST(false),

    /**
     * Subdirection heading Northwest.
     */
    NORTH_WEST(true),

    /**
     * Subdirection heading Northeast.
     */
    NORTH_EAST(true),

    /**
     * Subdirection heading Southwest.
     */
    SOUTH_WEST(true),

    /**
     * Subdirection heading Southeast.
     */
    SOUTH_EAST(true);

    /* Variable to store if it is a subdirection */
    private boolean subdirection;

    /**
     * Constructor for the enum Direction.
     *
     * @param subdirection indicates if it is a
     *                     subdirection.
     */
    Direction(boolean subdirection) {
        this.subdirection = subdirection;
    }

    /**
     * Check if the direction is a subdirection.
     * Subdirections: NW, NE, SW, SE.
     *
     * @return true if the direction is a subdirection.
     */
    public boolean isSubdirection() {
        return this.subdirection;
    }
}
