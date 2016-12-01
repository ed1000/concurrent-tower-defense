package pt.ua.towerdefense.monster;

import pt.ua.concurrent.CThread;
import pt.ua.towerdefense.Direction;
import pt.ua.towerdefense.Position;
import pt.ua.towerdefense.RotationWise;
import pt.ua.towerdefense.TerrainMap;

/**
 * Class that represents Monsters.
 *
 * @author Eduardo Sousa
 */
public class Monster extends CThread {
    /* Monster's attributes. Defines speed and health */
    private final MonsterAttributes attributes;

    /* Defines the map where the monster is */
    private final TerrainMap map;

    /* Health that the monster still has */
    private int remainingHealth;

    /* Position where the monster is at */
    private Position actualPosition;

    /* Heading of the monster */
    private Direction direction;

    /**
     * Constructor for the Monsters object.
     *
     * @param attributes attributes that define the monster's characteristics.
     * @param map map where the monster is.
     * @param actualPosition position where the object must start.
     * @param direction heading of the monster.
     */
    public Monster(MonsterAttributes attributes, TerrainMap map, Position actualPosition, Direction direction) {
        super();

        assert attributes != null;
        assert map != null;
        assert actualPosition != null;
        assert direction != null;
        assert !direction.isSubdirection();
        assert map.isPositionInPath(actualPosition);

        this.attributes = attributes;
        this.map = map;
        this.actualPosition = actualPosition;
        this.direction = direction;

        this.remainingHealth = this.attributes.getHealth();

        assert this.actualPosition != null;
        assert this.direction != null;
        assert !this.direction.isSubdirection();
        assert this.remainingHealth == this.attributes.getHealth();
    }

    @Override
    public void run() {

    }

    /**
     * Checks if there is a road to the left side of the monster.
     *
     * @return true if there is a road at his left.
     */
    private boolean isRoadAvailableLeft() {
        assert this.direction != null;
        assert !this.direction.isSubdirection();
        assert this.actualPosition != null;
        assert this.map.isPositionInPath(this.actualPosition);

        int x = this.actualPosition.getCoordinateX();
        int y = this.actualPosition.getCoordinateY();

        switch (direction) {
            case NORTH:
            case SOUTH:
                if(direction == Direction.NORTH)
                    x--;
                else
                    x++;
                break;
            case EAST:
            case WEST:
                if(direction == Direction.EAST)
                    y--;
                else
                    y++;
                break;
        }

        return x >= 0 && x <= this.map.getWidth() &&
                y >= 0 && y <= this.map.getHeight() &&
                this.map.isPositionInPath(new Position(x, y)) &&
                this.map.isPositionAvailable(new Position(x, y));
    }

    /**
     * Checks if there is a road to the right side of the monster.
     *
     * @return true if there is a road at his right.
     */
    private boolean isRoadAvailableRight() {
        assert this.direction != null;
        assert !this.direction.isSubdirection();
        assert this.actualPosition != null;
        assert this.map.isPositionInPath(this.actualPosition);

        int x = this.actualPosition.getCoordinateX();
        int y = this.actualPosition.getCoordinateY();

        switch (direction) {
            case NORTH:
            case SOUTH:
                if(direction == Direction.NORTH)
                    x++;
                else
                    x--;
                break;
            case EAST:
            case WEST:
                if(direction == Direction.EAST)
                    y++;
                else
                    y--;
                break;
        }

        return x >= 0 && x <= this.map.getWidth() &&
                y >= 0 && y <= this.map.getHeight() &&
                this.map.isPositionInPath(new Position(x, y)) &&
                this.map.isPositionAvailable(new Position(x, y));
    }

    /**
     * Checks if there is a road to the front side of the monster.
     *
     * @return true if there is a road at his front.
     */
    private boolean isRoadAvailableCenter() {
        assert this.direction != null;
        assert !this.direction.isSubdirection();
        assert this.actualPosition != null;
        assert this.map.isPositionInPath(this.actualPosition);

        int x = this.actualPosition.getCoordinateX();
        int y = this.actualPosition.getCoordinateY();

        switch (direction) {
            case NORTH:
            case SOUTH:
                if (direction == Direction.NORTH)
                    y--;
                else
                    y++;
                break;
            case EAST:
            case WEST:
                if (direction == Direction.EAST)
                    x++;
                else
                    x--;
                break;
        }

        return x >= 0 && x <= this.map.getWidth() &&
                y >= 0 && y <= this.map.getHeight() &&
                this.map.isPositionInPath(new Position(x, y)) &&
                this.map.isPositionAvailable(new Position(x, y));
    }

    /**
     * Moves the monster to the tile in front of him.<br>
     * Blocks while the monster is moving.
     */
    private void moveInFront() {
        assert this.direction != null;
        assert !this.direction.isSubdirection();
        assert this.actualPosition != null;
        assert this.map.isPositionInPath(this.actualPosition);

        boolean moved;
        int x = this.actualPosition.getCoordinateX();
        int y = this.actualPosition.getCoordinateY();

        switch (direction) {
            case NORTH:
            case SOUTH:
                if (direction == Direction.NORTH)
                    y--;
                else
                    y++;
                break;
            case EAST:
            case WEST:
                if (direction == Direction.EAST)
                    x++;
                else
                    x--;
                break;
        }

        if(x >= 0 && x <= this.map.getWidth() &&
                y >= 0 && y <= this.map.getHeight() &&
                this.map.isPositionInPath(new Position(x, y)) &&
                this.map.isPositionAvailable(new Position(x, y))) {
            moved = this.map.moveToPosition(new Position(x, y));

            if(moved)
                this.actualPosition = new Position(x, y);
        }

        assert this.actualPosition != null;
        assert this.map.isPositionInPath(this.actualPosition);
    }

    /**
     * Rotates the monster to the desired heading.<br>
     * Blocks while rotating.
     *
     * @param rotation rotation direction (clockwise or counterclockwise).
     */
    private void rotate(RotationWise rotation) {
        assert this.direction != null;
        assert !this.direction.isSubdirection();
        assert rotation != null;

        switch (this.direction) {
            case NORTH:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.EAST;
                else
                    this.direction = Direction.WEST;
                break;
            case EAST:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.SOUTH;
                else
                    this.direction = Direction.NORTH;
                break;
            case SOUTH:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.WEST;
                else
                    this.direction = Direction.EAST;
                break;
            case WEST:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.NORTH;
                else
                    this.direction = Direction.SOUTH;
                break;
        }

        assert this.direction != null;
        assert !this.direction.isSubdirection();
    }

    /**
     * Function to decrese the amount of health of a monster when it is shot by a tower.
     *
     * @param damage damage inflicted by the shot.
     */
    public void gotShot(int damage) {
        assert damage >= 0;
        assert this.remainingHealth > 0;
        assert this.remainingHealth <= this.attributes.getHealth();

        this.remainingHealth -= (damage > this.remainingHealth) ? this.remainingHealth : damage;

        assert this.remainingHealth >= 0;
        assert this.remainingHealth <= this.attributes.getHealth();
    }
}
