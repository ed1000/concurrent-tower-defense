package pt.ua.towerdefense.monster;

/**
 * Class that represents the Monsters attributes, like: health, speed.
 * The objects of this class are immutable.
 *
 * @author Eduardo Sousa
 */
public class MonsterAttributes {
    /* Health of the monster */
    private final int health;

    /* Speed to which the monster moves */
    private final int speed;

    /**
     * Constructor for objects of the type MonsterAttributes.
     *
     * @param health health of the monster
     * @param speed speed of the monster
     */
    public MonsterAttributes(int health, int speed) {
        assert health > 0;
        assert speed > 0;

        this.health = health;
        this.speed = speed;
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
     * Getter for the monster's speed.
     *
     * @return a positive integer representing the monster's speed.
     */
    public int getSpeed() {
        return speed;
    }
}
