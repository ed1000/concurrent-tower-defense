package pt.ua.towerdefense.tower;

import pt.ua.concurrent.CThread;
import pt.ua.towerdefense.Direction;
import pt.ua.towerdefense.Position;
import pt.ua.towerdefense.RotationWise;
import pt.ua.towerdefense.TerrainMap;

import java.util.List;

public class Tower extends CThread {
    private final TerrainMap map;
    private final Position position;
    private final TowerAttributes attributes;

    private Direction direction;

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

    private List<Position> activateRadar() {
        return null;
    }

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

        assert this.direction != null;
    }

    private void shoot(Position pos) {
        assert pos != null;
        assert this.pointingToPosition(pos);
        assert this.positionInShootingRange(pos);

        this.map.shootPosition(pos);
    }

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
