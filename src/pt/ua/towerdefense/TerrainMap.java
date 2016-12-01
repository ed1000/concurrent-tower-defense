package pt.ua.towerdefense;

public class TerrainMap {
    private final int width;
    private final int height;

    public TerrainMap(int width, int height) {
        assert width > 0;
        assert height > 0;

        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isPositionInPath(Position pos) {
        assert pos != null;
        assert pos.getCoordinateX() >= 0;
        assert pos.getCoordinateX() <= this.width;
        assert pos.getCoordinateY() >= 0;
        assert pos.getCoordinateY() <= this.height;

        return true;
    }

    public boolean isPositionNotInPath(Position pos) {
        assert pos != null;
        assert pos.getCoordinateX() >= 0;
        assert pos.getCoordinateX() <= this.width;
        assert pos.getCoordinateY() >= 0;
        assert pos.getCoordinateY() <= this.height;

        return false;
    }

    public boolean isPositionAvailable(Position pos) {
        assert pos != null;
        assert pos.getCoordinateX() >= 0;
        assert pos.getCoordinateX() <= this.width;
        assert pos.getCoordinateY() >= 0;
        assert pos.getCoordinateY() <= this.height;

        return true;
    }

    public boolean moveToPosition(Position pos) {
        return true;
    }

    public void shootPosition(Position pos) {

    }
}
