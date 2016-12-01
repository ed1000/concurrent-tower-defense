package pt.ua.towerdefense.tower;

import pt.ua.concurrent.CThread;
import pt.ua.towerdefense.Direction;
import pt.ua.towerdefense.Position;
import pt.ua.towerdefense.RotationWise;
import pt.ua.towerdefense.TerrainMap;

import java.util.List;

/**
 * Class responsible for the tower threads.
 * Waits until a monster is in range. When a monster is in range,
 * it will shoot it until the monster dies or is out of shooting
 * range.
 * Then goes back to sleep.
 *
 * @author Eduardo Sousa
 */
public class Tower extends CThread {
    /* Map that defines the positions and contains all objects.
    * It also acts as the synchronism point to block while the
    * tower is waiting for a target. */
    private final TerrainMap map;

    /* Position in the map where the tower is located. */
    private final Position position;

    /* Attributes of the tower. */
    private final TowerAttributes attributes;

    /* Direction to where the cannon of the tower is placed. */
    private Direction direction;

    /**
     * Constructor for the tower
     *
     * @param map reference to the TerrainMap.
     * @param position reference to the position where the tower
     *                 is located.
     * @param attributes reference to the tower attributes.
     */
    public Tower(TerrainMap map, Position position, TowerAttributes attributes) {
        super();

        assert map != null;
        assert position != null;
        assert attributes != null;
        assert map.isPositionNotInPath(position);

        this.map = map;
        this.position = position;
        this.attributes = attributes;

        this.direction = Direction.NORTH;
    }

    @Override
    public void run() {

    }

    /**
     * Blocking function to wait for the targets in range.
     *
     * @return a list of positions where there are targets.
     */
    private List<Position> activateRadar() {
        //TODO: Lacks implementation
        return null;
    }

    /**
     * Rotates the tower's cannon in the desired direction.
     * Blocks during the time it is rotating.
     *
     * @param rotation direction of the rotation (clockwise
     *                 or counterclockwise)
     */
    private void rotateToAngle(RotationWise rotation) {
        assert rotation != null;
        assert this.direction != null;

        switch (this.direction) {
            case NORTH:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.NORTH_EAST;
                else
                    this.direction = Direction.NORTH_WEST;
                break;
            case NORTH_EAST:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.EAST;
                else
                    this.direction = Direction.NORTH;
                break;
            case EAST:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.SOUTH_EAST;
                else
                    this.direction = Direction.NORTH_EAST;
                break;
            case SOUTH_EAST:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.SOUTH;
                else
                    this.direction = Direction.EAST;
                break;
            case SOUTH:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.SOUTH_WEST;
                else
                    this.direction = Direction.SOUTH_EAST;
                break;
            case SOUTH_WEST:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.WEST;
                else
                    this.direction = Direction.SOUTH;
                break;
            case WEST:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.NORTH_WEST;
                else
                    this.direction = Direction.SOUTH_WEST;
                break;
            case NORTH_WEST:
                if(rotation == RotationWise.CLOCKWISE)
                    this.direction = Direction.NORTH;
                else
                    this.direction = Direction.WEST;
                break;
        }

        //TODO: Block while rotating

        assert this.direction != null;
    }

    /**
     * Shoots into a desired position.
     * Blocks while shooting.
     *
     * @param pos position to shoot into.
     */
    private void shoot(Position pos) {
        assert pos != null;
        assert this.pointingToPosition(pos);
        assert this.positionInShootingRange(pos);

        this.map.shootPosition(pos);

        //TODO: Block while shooting
    }

    /**
     * Check if the tower's cannon is pointing to the
     * right position.
     *
     * @param pos position where the tower needs to be
     *            aiming.
     *
     * @return true if the tower is aiming for the right
     *         position.
     */
    private boolean pointingToPosition(Position pos) {
        double EPSILON = 1.0;

        assert pos != null;
        assert this.position != null;
        assert this.direction != null;

        if(this.position.getCoordinateX() - pos.getCoordinateX() != 0) {
            double angle = Math.toDegrees(Math.atan(((double) this.position.getCoordinateY() - pos.getCoordinateY()) /
                    ((double) this.position.getCoordinateX() - pos.getCoordinateX())));

            if(angle > 0 - EPSILON && angle < 0 + EPSILON) {
                if(this.position.getCoordinateX() > pos.getCoordinateX())
                    return this.direction == Direction.WEST;
                else
                    return this.direction == Direction.EAST;
            } else if (angle > 45 - EPSILON && angle < 45 + EPSILON) {
                if(this.position.getCoordinateX() < pos.getCoordinateX() &&
                        this.position.getCoordinateY() > pos.getCoordinateY())
                    return this.direction == Direction.NORTH_EAST;
                else
                    return this.direction == Direction.SOUTH_WEST;
            } else if(angle > -45 - EPSILON && angle < -45 + EPSILON) {
                if(this.position.getCoordinateX() < pos.getCoordinateX() &&
                        this.position.getCoordinateY() < pos.getCoordinateY())
                    return this.direction == Direction.SOUTH_EAST;
                else
                    return this.direction == Direction.NORTH_WEST;
            } else
                return false;
        } else {
            if(this.position.getCoordinateY() > pos.getCoordinateY())
                return this.direction == Direction.NORTH;
            else
                return this.direction == Direction.SOUTH;
        }
    }

    /**
     * Check if the target is in shooting range.
     *
     * @param pos position of the target.
     * @return true if the target is in shooting range.
     */
    private boolean positionInShootingRange(Position pos) {
        assert pos != null;
        assert this.attributes != null;
        assert this.position != null;

        int minX = this.position.getCoordinateX() - this.attributes.getRadarRange();
        int maxX = this.position.getCoordinateX() + this.attributes.getRadarRange();
        int minY = this.position.getCoordinateY() - this.attributes.getRadarRange();
        int maxY = this.position.getCoordinateY() + this.attributes.getRadarRange();

        return pos.getCoordinateX() >= minX && pos.getCoordinateX() <= maxX &&
                pos.getCoordinateY() >= minY && pos.getCoordinateY() <= maxY;
    }
}
