package pt.ua.towerdefense.world;

import pt.ua.concurrent.CThread;
import pt.ua.towerdefense.definitions.Position;
import pt.ua.towerdefense.monsters.Monster;
import pt.ua.towerdefense.towers.Tower;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

    /* Position where the path begins */
    private final Position begin;

    /* Position where the path ends */
    private final Position end;

    /* Terrain mapping (False - not path | True - path) */
    private final ConcurrentMap<Position, Boolean> map;

    /* Map that contains all monster locations in game */
    private ConcurrentMap<Position, Monster> monsterLocations;

    /* Map that contains all tower locations in game */
    private ConcurrentMap<Position, Tower> towerLocations;

    /**
     * Constructor for the TerrainMap object.
     *
     * @param width map height.
     * @param height map width.
     * @param begin position where the path begins.
     * @param end position where the path ends.
     * @param map terrain distribution.
     */
    public TerrainMap(int width, int height, Position begin, Position end, ConcurrentMap<Position, Boolean> map) {
        assert width > 0;
        assert height > 0;
        assert begin != null;
        assert begin.getCoordinateX() >= 0 && begin.getCoordinateX() <= width;
        assert begin.getCoordinateY() >= 0 && begin.getCoordinateY() <= height;
        assert end != null;
        assert end.getCoordinateX() >= 0 && end.getCoordinateX() <= width;
        assert end.getCoordinateY() >= 0 && end.getCoordinateY() <= height;
        assert !begin.equals(end);
        assert map != null;
        assert map.size() == width * height;

        this.width = width;
        this.height = height;
        this.begin = begin;
        this.end = end;
        this.map = map;
        this.monsterLocations = new ConcurrentHashMap<>();
        this.towerLocations = new ConcurrentHashMap<>();
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
     * @return map height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter for the beginning of the path.
     *
     * @return position where the path begins.
     */
    public Position getBegin() {
        return begin;
    }

    /**
     * Getter for the ending of the path.
     *
     * @return position where the path ends.
     */
    public Position getEnd() {
        return end;
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

        return this.map.get(pos);
    }

    /**
     * Checks if the position is not occupied by another object.
     *
     * @param pos position to be checked.
     * @return true if the position is not occupied.
     */
    public boolean isPositionAvailable(Position pos) {
        assert pos != null;
        assert pos.getCoordinateX() >= 0 && pos.getCoordinateX() <= this.width;
        assert pos.getCoordinateY() >= 0 && pos.getCoordinateY() <= this.height;

        Class t = Thread.currentThread().getClass();

        if(t.equals(Monster.class)) {
            return this.monsterLocations.containsKey(pos);
        } else if(t.equals(Tower.class)) {
            return this.towerLocations.containsKey(pos);
        } else
            return false;
    }

    /**
     * Inserts tower in the map.
     *
     * @param pos position where you want to put the tower.
     * @return true if the tower was successfully placed. False in case it failed.
     */
    public synchronized boolean insertTower(Position pos) {
        assert pos != null;
        assert pos.getCoordinateX() >= 0 && pos.getCoordinateX() <= this.width;
        assert pos.getCoordinateY() >= 0 && pos.getCoordinateY() <= this.height;
        assert CThread.currentThread().getClass().equals(Tower.class);
        assert !isPositionInPath(pos);

        Tower tower = (Tower) CThread.currentThread();

        if(towerLocations.containsKey(pos))
            return false;
        else {
            towerLocations.put(pos, tower);
            return true;
        }
    }

    /**
     * Insert monster in map.
     *
     * @param pos
     */
    public synchronized void insertMonster(Position pos) {
        assert pos != null;
        assert pos.getCoordinateX() >= 0 && pos.getCoordinateX() <= this.width;
        assert pos.getCoordinateY() >= 0 && pos.getCoordinateY() <= this.height;
        assert CThread.currentThread().getClass().equals(Monster.class);
        assert pos.equals(this.begin);


    }

    /**
     * Function to move an object from one position to the other.<br>
     * Function blocks while the object is moving.
     *
     * @param pos position to where you want to move the object.
     * @return true if the object was moved.
     */
    public synchronized boolean moveToPosition(Position pos) {
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
    public synchronized void shootPosition(Position pos) {
        // TODO: Lacks implementation.
    }
}
