package pt.ua.towerdefense;

/**
 * Class that represents the map where all objects are in.
 * It also defines the paths where monsters can walk and
 * the location of the towers.
 *
 * @author Eduardo Sousa
 */
public class TerrainMap {
    /* Map width in cells */
    private final int width;

    /* Map height in cells */
    private final int height;

    /**
     * Constructor for the TerrainMap object.
     *
     * @param width map height.
     * @param height map width.
     */
    public TerrainMap(int width, int height) {
        assert width > 0;
        assert height > 0;

        this.width = width;
        this.height = height;
    }

    /**
     * Getter for the map width.
     *
     * @return map width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter for the map height.
     *
     * @return map heigth.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Checks if the position is in the path.
     *
     * @param pos position to be checked.
     * @return true if the position is the path.
     */
    public boolean isPositionInPath(Position pos) {
        assert pos != null;
        assert pos.getCoordinateX() >= 0;
        assert pos.getCoordinateX() <= this.width;
        assert pos.getCoordinateY() >= 0;
        assert pos.getCoordinateY() <= this.height;

        // TODO: Lacks implementation.

        return true;
    }

    /**
     * Checks if the position is not occupied by another object.
     *
     * @param pos position to be checked.
     * @return true if the position is not occupied.
     */
    public boolean isPositionAvailable(Position pos) {
        assert pos != null;
        assert pos.getCoordinateX() >= 0;
        assert pos.getCoordinateX() <= this.width;
        assert pos.getCoordinateY() >= 0;
        assert pos.getCoordinateY() <= this.height;

        // TODO: Lacks implementation.

        return true;
    }

    /**
     * Function to move an object from one position to the other.<br>
     * Function blocks while the object is moving.
     *
     * @param pos position to where you want to move the object.
     * @return true if the object was moved.
     */
    public boolean moveToPosition(Position pos) {
        // TODO: Lacks implementation.
        return true;
    }

    /**
     * Function to shoot into a determined position. If there is
     * an object there, the function will also update the state of
     * the object that was shot.<br>
     * Function blocks while shooting.
     *
     * @param pos position to where you want to shoot.
     */
    public void shootPosition(Position pos) {
        // TODO: Lacks implementation.
    }
}
