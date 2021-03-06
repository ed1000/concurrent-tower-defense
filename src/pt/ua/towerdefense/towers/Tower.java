package pt.ua.towerdefense.towers;

import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Metronome;
import pt.ua.towerdefense.definitions.Direction;
import pt.ua.towerdefense.world.Position;
import pt.ua.towerdefense.definitions.RotationWise;
import pt.ua.towerdefense.world.WorldState;

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
    /* World state */
    private final WorldState worldState;

    /* Metronome that helps synchronize threads based on time */
    private final Metronome metronome;

    /* Position in the map where the tower is located. */
    private final Position position;

    /* Attributes of the tower. */
    private final TowerAttributes attributes;

    /* Direction to where the cannon of the tower is placed. */
    private Position aimingPosition;

    /**
     * Constructor for the tower
     *
     * @param worldState reference to the world tower interface.
     * @param metronome metronome.
     * @param position reference to the position where the tower is located.
     * @param attributes reference to the tower attributes.
     */
    public Tower(WorldState worldState, Metronome metronome, Position position, TowerAttributes attributes) {
        super();

        assert worldState != null;
        assert metronome != null;
        assert position != null;
        assert attributes != null;
        assert worldState.isPositionForTower(position);

        this.worldState = worldState;
        this.metronome = metronome;
        this.position = position;
        this.attributes = attributes;
    }

    @Override
    public void run() {
        worldState.addTower(this);

        while (true) {
            List<Position> positions = activateRadar();

            if(positions.size() > 0) {
                for(Position pos : positions) {
                    if(positionInShootingRange(pos)) {
                        if(!pointingToPosition(pos))
                            rotateToAngle(pos);

                        shoot(pos);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Blocking function to wait for the targets in range.
     *
     * @return a list of positions where there are targets.
     */
    private List<Position> activateRadar() {
        int waitingCycles = this.attributes.getRadarCycles();

        while (waitingCycles > 0) {
            this.metronome.sync();
            waitingCycles--;
        }

        List<Position> result = this.worldState.getMonstersInRange();

        assert result != null;
        assert !result.isEmpty();

        return result;
    }

    /**
     * Rotates the tower's cannon in the desired direction.
     * Blocks during the time it is rotating.
     */
    private void rotateToAngle(Position position) {
        assert position != null;
        assert worldState.isPositionInMap(position);

        int waitingCycles = this.attributes.getRotateCycles();

        while (waitingCycles > 0) {
            this.metronome.sync();
            waitingCycles--;
        }

        aimingPosition = position;

        assert aimingPosition != null;
    }

    /**
     * Shoots into a desired position.<br>
     * Blocks while shooting.
     *
     * @param pos position to shoot into.
     */
    private void shoot(Position pos) {
        assert pos != null;
        assert worldState.isPositionInMap(pos);
        assert pointingToPosition(pos);
        assert positionInShootingRange(pos);

        int waitingCycles = this.attributes.getShootCycles();

        while (waitingCycles > 0) {
            this.metronome.sync();
            waitingCycles--;
        }

        this.worldState.shootPosition(pos);

        waitingCycles = this.attributes.getCooldownCycles();

        while (waitingCycles > 0) {
            this.metronome.sync();
            waitingCycles--;
        }
    }

    /**
     * Check if the tower's cannon is pointing to the right position.
     *
     * @param pos position where the tower needs to be aiming.
     *
     * @return true if the tower is aiming for the right position.
     */
    private boolean pointingToPosition(Position pos) {
        assert pos != null;

        return aimingPosition != null && aimingPosition.equals(pos);
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

        int minX = this.position.getCoordinateX() - this.attributes.getShotRange();
        int maxX = this.position.getCoordinateX() + this.attributes.getShotRange();
        int minY = this.position.getCoordinateY() - this.attributes.getShotRange();
        int maxY = this.position.getCoordinateY() + this.attributes.getShotRange();

        return pos.getCoordinateX() >= minX && pos.getCoordinateX() <= maxX &&
                pos.getCoordinateY() >= minY && pos.getCoordinateY() <= maxY;
    }

    /**
     * Getter for the position where the tower was placed.
     *
     * @return position of the tower.
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Getter for the damage caused by a shot.
     *
     * @return a positive integer.
     */
    public int getShotDamage() {
        return this.attributes.getPotentialDamage();
    }

    /**
     * Getter for the radar range.
     *
     * @return a positive integer.
     */
    public int getRadarRange() {
        return this.attributes.getRadarRange();
    }
}
