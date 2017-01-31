package pt.ua.towerdefense.monsters;

import com.sun.org.apache.xpath.internal.SourceTree;
import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Metronome;
import pt.ua.towerdefense.definitions.Direction;
import pt.ua.towerdefense.definitions.RotationWise;
import pt.ua.towerdefense.world.Position;
import pt.ua.towerdefense.world.WorldState;

/**
 * Class that represents Monsters.
 *
 * @author Eduardo Sousa
 */
public class Monster extends CThread {
    /* Monster's attributes. Defines speed and health */
    private final MonsterAttributes attributes;

    /* */
    private final WorldState worldState;

    /* Metronome that helps sync all threads based on time */
    private final Metronome metronome;

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
        assert actualPosition != null;
        assert direction != null;
        assert !direction.isSubdirection();
        assert world.isPositionInPath(actualPosition);
        assert world.getPathBeginning().equals(actualPosition);

        this.attributes = attributes;
        this.worldState = world;
        this.metronome = metronome;
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
        worldState.addMonster(this);

        boolean left, front;

        while(remainingHealth > 0 && !actualPosition.equals(worldState.getPathEnd())) {
            left = isRoadAvailableLeft();
            front = isRoadAvailableCenter();

            if(front)
                moveInFront();
            else if(left)
                rotate(RotationWise.ANTICLOCKWISE);
            else
                rotate(RotationWise.CLOCKWISE);
        }

        if(remainingHealth <= 0)
            System.out.println("I got killed");
        else
            System.out.println("I reached the end");

        worldState.removeMonster();
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
        assert this.worldState.isPositionInPath(this.actualPosition);

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
        assert this.direction != null;
        assert !this.direction.isSubdirection();
        assert this.actualPosition != null;
        assert this.worldState.isPositionInPath(this.actualPosition);

        return this.worldState.isPathInDirection(this.direction);
    }

    /**
     * Moves the monster to the tile in front of him.<br>
     * Blocks while the monster is moving.
     */
    private void moveInFront() {
        int waitingCycles = this.attributes.getMoveCycles();

        assert this.direction != null;
        assert !this.direction.isSubdirection();
        assert this.actualPosition != null;
        assert this.worldState.isPositionInPath(this.actualPosition);

        while (waitingCycles > 0) {
            this.metronome.sync();
            waitingCycles--;
        }

        switch (direction) {
            case NORTH:
                this.actualPosition = new Position(this.actualPosition.getCoordinateX(), this.actualPosition.getCoordinateY()-1);
                break;
            case WEST:
                this.actualPosition = new Position(this.actualPosition.getCoordinateX()-1, this.actualPosition.getCoordinateY());
                break;
            case SOUTH:
                this.actualPosition = new Position(this.actualPosition.getCoordinateX(), this.actualPosition.getCoordinateY()+1);
                break;
            case EAST:
                this.actualPosition = new Position(this.actualPosition.getCoordinateX()+1, this.actualPosition.getCoordinateY());
                break;
        }

        worldState.moveMonster();

        assert this.actualPosition != null;
        assert this.worldState.isPositionInPath(this.actualPosition);
    }

    /**
     * Rotates the monster to the desired heading.<br>
     * Blocks while rotating.
     *
     * @param rotation rotation direction (clockwise or counterclockwise).
     */
    private void rotate(RotationWise rotation) {
        int waitingCycles = this.attributes.getRotateCycles();

        assert this.direction != null;
        assert !this.direction.isSubdirection();
        assert rotation != null;

        while (waitingCycles > 0) {
            this.metronome.sync();
            waitingCycles--;
        }

        if(rotation == RotationWise.CLOCKWISE) {
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
        } else if(rotation == RotationWise.ANTICLOCKWISE) {
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

        assert this.direction != null;
        assert !this.direction.isSubdirection();
    }

    /**
     * Function to decrease the amount of health of a monster when it is shot by a tower.
     *
     * @param damage damage inflicted by the shot.
     */
    public synchronized void gotShot(int damage) {
        assert damage >= 0;
        assert this.remainingHealth > 0;
        assert this.remainingHealth <= this.attributes.getHealth();

        this.remainingHealth -= (damage > this.remainingHealth) ? this.remainingHealth : damage;

        assert this.remainingHealth >= 0;
        assert this.remainingHealth <= this.attributes.getHealth();
    }

    public synchronized Position getActualPosition() {
        return this.actualPosition;
    }
}
