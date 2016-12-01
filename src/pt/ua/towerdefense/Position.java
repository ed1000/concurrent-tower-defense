package pt.ua.towerdefense;

/**
 * Class that represents the position of an object in the
 * map. Objects of this class are immutable.
 *
 * <b>Note:</b>  it's required to set the map before creating
 * objects of this class.
 *
 * @author Eduardo Sousa
 */
public class Position {
    /* Map to which the position is relative to */
    private static TerrainMap map;

    /* Coordinate in the XX axis */
    private final int coordinateX;

    /* Coordinate in the YY axis */
    private final int coordinateY;

    /**
     * Sets the map to which the position is relative to.
     *
     * @param map map for where all position will be relative to.
     */
    public static void setMap(TerrainMap map) {
        assert Position.map == null;
        assert map != null;

        Position.map = map;

        assert Position.map != null;
    }

    /**
     * Constructor for the Position object.
     *
     * @param coordinateX coordinate in the XX axis.
     * @param coordinateY coordinate in the YY axis.
     */
    public Position(int coordinateX, int coordinateY) {
        assert Position.map != null;
        assert coordinateX >= 0;
        assert coordinateY >= 0;
        assert coordinateX <= Position.map.getWidth();
        assert coordinateY <= Position.map.getHeight();

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
}
