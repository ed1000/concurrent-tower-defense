package pt.ua.towerdefense.monsters;

import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Metronome;
import pt.ua.towerdefense.definitions.Direction;
import pt.ua.towerdefense.definitions.RotationWise;
import pt.ua.towerdefense.world.Position;
import pt.ua.towerdefense.world.WorldState;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that represents Monsters.
 *
 * @author Eduardo Sousa
 */
public class Monster extends CThread {
    /* Monster's attributes. Defines speed and health */
    private final MonsterAttributes attributes;

    /* World state */
    private final WorldState worldState;

    /* Metronome that helps sync all threads based on time */
    private final Metronome metronome;

    /* Health that the monster still has */
    private final AtomicInteger remainingHealth;

    /* Position where the monster is at */
    private Position actualPosition;

    /* Heading of the monster */
    private Direction direction;

    /**
     * Constructor for the Monsters object.
     *
     * @param attributes attributes that define the monster's characteristics.
     * @param world world where the monster is.
     * @param metronome metronome to sync in time.
     * @param actualPosition position where the object must start.
     * @param direction heading of the monster.
     */
    public Monster(MonsterAttributes attributes, WorldState world, Metronome metronome, Position actualPosition,
                   Direction direction) {
        super();

        assert attributes != null;
        assert world != null;
        assert metronome != null;
        assert actualPosition != null;
        assert world.getPathBeginning().equals(actualPosition);
        assert direction != null;

        this.attributes = attributes;
        this.worldState = world;
        this.metronome = metronome;
        this.actualPosition = actualPosition;
        this.direction = direction;

        this.remainingHealth = new AtomicInteger(this.attributes.getHealth());
    }

    @Override
    public void run() {
        worldState.addMonster(this);

        boolean left, front;

        while(remainingHealth.get() > 0 && !actualPosition.equals(worldState.getPathEnd())) {
            left = isRoadAvailableLeft();
            front = isRoadAvailableCenter();

            if(front)
                moveInFront();
            else if(left)
                rotate(RotationWise.ANTICLOCKWISE);
            else
                rotate(RotationWise.CLOCKWISE);
        }

        worldState.removeMonster();
    }

    /**
     * Checks if there is a road to the left side of the monster.
     *
     * @return true if there is a road at his left.
     */
    private boolean isRoadAvailableLeft() {
        assert direction != null;

        Direction dir = Direction.NORTH;

        switch (this.direction) {
            case NORTH:
                dir = Direction.WEST;
                break;
            case SOUTH:
                dir = Direction.EAST;
                break;
            case EAST:
                dir = Direction.NORTH;
                break;
            case WEST:
                dir = Direction.SOUTH;
                break;
        }

        return this.worldState.isPathInDirection(dir);
    }

    /**
     * Checks if there is a road to the front side of the monster.
     *
     * @return true if there is a road at his front.
     */
    private boolean isRoadAvailableCenter() {
        assert direction != null;

        return this.worldState.isPathInDirection(this.direction);
    }

    /**
     * Moves the monster to the tile in front of him.<br>
     * Blocks while the monster is moving.
     */
    private void moveInFront() {
        assert remainingHealth.get() > 0;
        assert direction != null;
        assert worldState.isPositionInPath(actualPosition);

        int waitingCycles = this.attributes.getMoveCycles();

        while (waitingCycles > 0) {
            if(remainingHealth.get() <= 0)
                return;

            this.metronome.sync();
            waitingCycles--;
        }

        if(remainingHealth.get() <= 0)
            return;

        switch (direction) {
            case NORTH:
                this.actualPosition = new Position(this.actualPosition.getCoordinateX(), this.actualPosition.getCoordinateY() - 1);
                break;
            case WEST:
                this.actualPosition = new Position(this.actualPosition.getCoordinateX() - 1, this.actualPosition.getCoordinateY());
                break;
            case SOUTH:
                this.actualPosition = new Position(this.actualPosition.getCoordinateX(), this.actualPosition.getCoordinateY() + 1);
                break;
            case EAST:
                this.actualPosition = new Position(this.actualPosition.getCoordinateX() + 1, this.actualPosition.getCoordinateY());
                break;
        }

        worldState.moveMonster();

        assert worldState.isPositionInPath(actualPosition);
    }

    /**
     * Rotates the monster to the desired heading.<br>
     * Blocks while rotating.
     *
     * @param rotation rotation direction (clockwise or counterclockwise).
     */
    private void rotate(RotationWise rotation) {
        assert remainingHealth.get() > 0;
        assert direction != null;

        int waitingCycles = this.attributes.getRotateCycles();

        while (waitingCycles > 0) {
            if(remainingHealth.get() <= 0)
                return;

            this.metronome.sync();
            waitingCycles--;
        }

        if(remainingHealth.get() <= 0)
            return;

        if (rotation == RotationWise.CLOCKWISE) {
            switch (this.direction) {
                case NORTH:
                    this.direction = Direction.EAST;
                    break;
                case WEST:
                    this.direction = Direction.NORTH;
                    break;
                case SOUTH:
                    this.direction = Direction.WEST;
                    break;
                case EAST:
                    this.direction = Direction.SOUTH;
                    break;
            }
        } else if (rotation == RotationWise.ANTICLOCKWISE) {
            switch (this.direction) {
                case NORTH:
                    this.direction = Direction.WEST;
                    break;
                case WEST:
                    this.direction = Direction.SOUTH;
                    break;
                case SOUTH:
                    this.direction = Direction.EAST;
                    break;
                case EAST:
                    this.direction = Direction.NORTH;
                    break;
            }
        }

        assert direction != null;
    }

    /**
     * Function to decrease the amount of health of a monster when it is shot by a tower.
     *
     * @param damage damage inflicted by the shot.
     */
    public synchronized void gotShot(int damage) {
        assert damage > 0;
        assert remainingHealth.get() > 0;

        int remaining = remainingHealth.get() - damage;
        this.remainingHealth.set(remaining > 0 ? remaining : 0);

        assert remainingHealth.get() >= 0;
    }

    /**
     * Synchronized getter for actual position.
     *
     * @return the position where it is.
     */
    public synchronized Position getActualPosition() {
        assert actualPosition != null;

        return this.actualPosition;
    }
}
