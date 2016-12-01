package pt.ua.towerdefense.monster;

import pt.ua.concurrent.CThread;
import pt.ua.towerdefense.Direction;
import pt.ua.towerdefense.Position;
import pt.ua.towerdefense.RotationWise;
import pt.ua.towerdefense.TerrainMap;

public class Monster extends CThread {
    private final MonsterAttributes attributes;
    private final TerrainMap map;

    private int remainingHealth;
    private Position actualPosition;
    private Direction direction;

    public Monster(MonsterAttributes attributes, TerrainMap map, Position actualPosition, Direction direction) {
        super();

        assert attributes != null;
        assert map != null;
        assert actualPosition != null;
        assert direction != null;
        assert map.isPositionInPath(actualPosition);

        this.attributes = attributes;
        this.map = map;
        this.actualPosition = actualPosition;
        this.direction = direction;

        this.remainingHealth = this.attributes.getHealth();
    }

    @Override
    public void run() {

    }

    private boolean isRoadAvailableLeft() {
        assert this.direction != null;
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

    private boolean isRoadAvailableRight() {
        assert this.direction != null;
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

    private boolean isRoadAvailableCenter() {
        assert this.direction != null;
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

    private void moveInFront() {
        assert this.direction != null;
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
            moved = this.map.moveTo(new Position(x, y));

            if(moved)
                this.actualPosition = new Position(x, y);
        }

        assert this.actualPosition != null;
        assert this.map.isPositionInPath(this.actualPosition);
    }

    private void rotate(RotationWise rotation) {
        assert this.direction != null;
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
    }

    public void gotShot(int damage) {
        assert damage >= 0;
        assert this.remainingHealth > 0;
        assert this.remainingHealth <= this.attributes.getHealth();

        this.remainingHealth -= (damage > this.remainingHealth) ? this.remainingHealth : damage;

        assert this.remainingHealth >= 0;
        assert this.remainingHealth <= this.attributes.getHealth();
    }
}
