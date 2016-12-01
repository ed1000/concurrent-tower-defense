package pt.ua.towerdefense.tower;

/**
 * Class that represents the attributes of a tower, like for example: potential damage, shooting range.
 * The objects of this class are immutable.
 *
 * @author Eduardo Sousa
 */
public class TowerAttributes {
    /* Max damage caused by a tower shot */
    private final int potentialDamage;

    /* Building cost of the tower */
    private final int buildCost;

    /* Speed at which the tower rotates the cannon */
    private final int rotationalSpeed;

    /* Max range to shoot at an object */
    private final int shotRange;

    /* Max range for the tower radar to detect an object */
    private final int radarRange;

    /* Time it takes for the radar to complete a sweep of the area */
    private final int radarTime;

    /**
     * Constructor for the Tower Attributes objects.
     *
     * @param potentialDamage max damage caused by the tower.
     * @param buildCost building cost of the tower.
     * @param rotationalSpeed speed at which the tower rotates the cannon.
     * @param shotRange max range to shoot at an object.
     * @param radarRange max range for the tower to detect an object.
     * @param radarTime time the tower takes to complete a search of the area
     */
    public TowerAttributes(int potentialDamage, int buildCost, int rotationalSpeed, int shotRange, int radarRange,
                           int radarTime) {
        this.potentialDamage = potentialDamage;
        this.buildCost = buildCost;
        this.rotationalSpeed = rotationalSpeed;
        this.shotRange = shotRange;
        this.radarRange = radarRange;
        this.radarTime = radarTime;
    }

    /**
     * Getter for the potential damage the a tower can cause.
     *
     * @return a positive integer that represents the potential damage.
     */
    public int getPotentialDamage() {
        return potentialDamage;
    }

    /**
     * Getter for the build cost of a tower.
     *
     * @return a positive integer that represents the cost of building a tower.
     */
    public int getBuildCost() {
        return buildCost;
    }

    /**
     * Getter for the rotational speed of the tower's cannon.
     *
     * @return a positive integer that represents the angular speed of the tower's cannon.
     */
    public int getRotationalSpeed() {
        return rotationalSpeed;
    }

    /**
     * Getter for the shooting range.
     *
     * @return a positive integer that represents the shooting range.
     */
    public int getShotRange() {
        return shotRange;
    }

    /**
     * Getter for the radar range.
     *
     * @return a positive integer that represents the radar range.
     */
    public int getRadarRange() {
        return radarRange;
    }

    /**
     * Getter for the time it takes for a tower to complete a search of all objects in radar range.
     *
     * @return a positive integer that represents the amount of time that the radar uses.
     */
    public int getRadarTime() {
        return radarTime;
    }
}
