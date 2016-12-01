package pt.ua.towerdefense;

public class Position {
    private static TerrainMap map;

    private final int coordinateX;
    private final int coordinateY;

    public static void setMap(TerrainMap map) {
        assert Position.map == null;
        assert map != null;

        Position.map = map;

        assert Position.map != null;
    }

    public Position(int coordinateX, int coordinateY) {
        assert Position.map != null;
        assert coordinateX >= 0;
        assert coordinateY >= 0;
        assert coordinateX <= Position.map.getWidth();
        assert coordinateY <= Position.map.getHeight();

        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }
}
