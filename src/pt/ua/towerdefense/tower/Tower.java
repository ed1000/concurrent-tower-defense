package pt.ua.towerdefense.tower;

import pt.ua.concurrent.CThread;
import pt.ua.towerdefense.Position;
import pt.ua.towerdefense.TerrainMap;

import java.util.List;

public class Tower extends CThread {
    private final TerrainMap map;
    private final Position position;
    private final TowerAttributes attributes;

    private int rotationAngle;

    public Tower(TerrainMap map, Position position, TowerAttributes attributes) {
        super();

        assert map != null;
        assert position != null;
        assert attributes != null;
        assert map.isPositionNotInPath(position);

        this.map = map;
        this.position = position;
        this.attributes = attributes;

        this.rotationAngle = 0;
    }

    @Override
    public void run() {

    }

    private List<Position> activateRadar() {
        return null;
    }

    private void rotateToAngle(int ang) {

    }

    private void shoot(Position pos) {

    }
}
