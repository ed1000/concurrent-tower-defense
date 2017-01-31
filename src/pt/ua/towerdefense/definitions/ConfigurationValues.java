package pt.ua.towerdefense.definitions;

/**
 * Class that stores the configurable values.
 *
 * @author Eduardo Sousa
 */
public class ConfigurationValues {
    // GENERAL SECTION

    /**
     * Metronome tick time interval.
     */
    public static final long TICK_MS = 100;

    // MONSTER SECTION

    /**
     * Monster's health.
     */
    public static final int MONSTER_HEALTH = 100;

    /**
     * Number of cycles that takes the monster to rotate.
     */
    public static final int MONSTER_ROTATION_CYCLES = 2;

    /**
     * Number of cycles that takes the monster to move.
     */
    public static final int MONSTER_MOVE_CYCLES = 5;

    // TOWER SECTION

    /**
     * Amount of damage the tower can cause to the monster.
     */
    public static final int TOWER_POTENTIAL_DAMAGE = 50;

    /**
     * Cost of building a tower.
     */
    public static final int TOWER_BUILD_COST = 1;

    /**
     * Number of cycles that the tower takes to rotate.
     */
    public static final int TOWER_ROTATION_CYCLES = 1;

    /**
     * Number of cycles the tower takes before shooting.
     */
    public static final int TOWER_SHOOT_CYCLES = 1;

    /**
     * Number of cycles the tower takes after shooting and resumes operations.
     */
    public static final int TOWER_COOLDOWN_CYCLES = 2;

    /**
     * Shooting range (radius in cells).
     */
    public static final int TOWER_SHOT_RANGE = 1;

    /**
     * Radar range (radius in cells).
     */
    public static final int TOWER_RADAR_RANGE = 3;

    /**
     * Minimum number of cycles the tower takes to use the radar.
     */
    public static final int TOWER_RADAR_CYCLES = 2;
}
