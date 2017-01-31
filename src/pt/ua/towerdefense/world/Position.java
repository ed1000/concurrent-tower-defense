package pt.ua.towerdefense.world;

import java.util.Objects;

/**
 * Class that represents the position of an object in the
 * map. Objects of this class are immutable.
 *
 * @author Eduardo Sousa
 */
public class Position {
    /* Coordinate in the XX axis */
    private final int coordinateX;

    /* Coordinate in the YY axis */
    private final int coordinateY;

    /**
     * Constructor for the Position object.
     *
     * @param coordinateX coordinate in the XX axis.
     * @param coordinateY coordinate in the YY axis.
     */
    public Position(int coordinateX, int coordinateY) {
        assert coordinateX >= 0;
        assert coordinateY >= 0;

        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    /**
     * Getter for the coordinate in the XX axis.
     *
     * @return coordinate in the XX axis.
     */
    public int getCoordinateX() {
        return coordinateX;
    }

    /**
     * Getter for the coordinate in the YY axis.
     *
     * @return coordinate in the YY axis.
     */
    public int getCoordinateY() {
        return coordinateY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.coordinateX, this.coordinateY);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;

        Position pos = (Position) obj;
        return this.coordinateX == pos.coordinateX && this.coordinateY == pos.coordinateY;
    }
}
