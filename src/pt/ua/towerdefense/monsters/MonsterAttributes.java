package pt.ua.towerdefense.monsters;

/**
 * Class that represents the Monsters attributes, like: health, speed.
 * The objects of this class are immutable.
 *
 * @author Eduardo Sousa
 */
public class MonsterAttributes {
    /* Health of the monster */
    private final int health;

    /* Number of cycles that takes a monster to move one cell */
    private final int moveCycles;

    /* Number of cycles that takes a monster to rotate 90 degrees */
    private final int rotateCycles;

    /**
     * Constructor for objects of the type MonsterAttributes.
     *
     * @param health health of the monster
     * @param moveCycles number of cycles to move
     * @param rotateCycles number of cycles to rotate
     */
    public MonsterAttributes(int health, int moveCycles, int rotateCycles) {
        assert health > 0;
        assert moveCycles > 0;
        assert rotateCycles > 0;

        this.health = health;
        this.moveCycles = moveCycles;
        this.rotateCycles = rotateCycles;
    }

    /**
     * Getter for the monster's health.
     *
     * @return a positive integer representing the monster's health.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Getter for the number of cycles that the monster takes to move one cell.
     *
     * @return a positive integer representing the number of cycles.
     */
    public int getMoveCycles() {
        return moveCycles;
    }

    /**
     * Getter for the number of cycles that the monster takes to rotate 90 degrees.
     *
     * @return a positive integer representing the number of cycles.
     */
    public int getRotateCycles() {
        return rotateCycles;
    }
}
