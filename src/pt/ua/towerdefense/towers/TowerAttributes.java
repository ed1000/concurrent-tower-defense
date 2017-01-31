package pt.ua.towerdefense.towers;

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

    /* Number of cycles required to rotate tower 45 degrees */
    private final int rotateCycles;

    /* Number of cycles before shooting */
    private final int shootCycles;

    /* Number of cycles between shots */
    private final int cooldownCycles;

    /* Max range to shoot at an object */
    private final int shotRange;

    /* Max range for the tower radar to detect an object */
    private final int radarRange;

    /* Time it takes for the radar to complete a sweep of the area */
    private final int radarCycles;

    /**
     * Constructor for the Tower Attributes objects.
     *
     * @param potentialDamage max damage caused by the tower.
     * @param buildCost building cost of the tower.
     * @param rotateCycles number of cycles that the tower takes to rotate the cannon.
     * @param shootCycles number of cycles that the tower takes before shooting.
     * @param cooldownCycles number of cycles that the tower takes after shooting.
     * @param shotRange max range to shoot at an object.
     * @param radarRange max range for the tower to detect an object.
     * @param radarCycles  minimum number of cycles that the radar takes.
     */
    public TowerAttributes(int potentialDamage, int buildCost, int rotateCycles, int shootCycles, int cooldownCycles,
                           int shotRange, int radarRange, int radarCycles) {
        this.potentialDamage = potentialDamage;
        this.buildCost = buildCost;
        this.rotateCycles = rotateCycles;
        this.shootCycles = shootCycles;
        this.cooldownCycles = cooldownCycles;
        this.shotRange = shotRange;
        this.radarRange = radarRange;
        this.radarCycles = radarCycles;
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
     * Getter for the number of cycles that it takes the tower to rotate the cannon 45 degrees.
     *
     * @return a positive integer that represents the number of cycles.
     */
    public int getRotateCycles() {
        return rotateCycles;
    }

    /**
     * Getter for the number of cycles that it takes the tower before shooting.
     *
     * @return a positive integer that represents the number of cycles.
     */
    public int getShootCycles() {
        return shootCycles;
    }

    /**
     * Getter for the number of cycles that it takes the tower to cooldown after shooting.
     *
     * @return a positive integer that represents the number of cycles.
     */
    public int getCooldownCycles() {
        return cooldownCycles;
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
     * Getter for the number of cycles it takes for the tower to complete a search of all objects in radar range.
     *
     * @return a positive integer that represents the amount of time that the radar uses.
     */
    public int getRadarCycles() {
        return radarCycles;
    }
}
