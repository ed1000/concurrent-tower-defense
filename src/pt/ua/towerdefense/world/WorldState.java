package pt.ua.towerdefense.world;

import pt.ua.concurrent.CThread;
import pt.ua.gboard.Gelem;
import pt.ua.gboard.games.Labyrinth;
import pt.ua.towerdefense.definitions.Direction;
import pt.ua.towerdefense.monsters.Monster;
import pt.ua.towerdefense.towers.Tower;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Class that encapsulates and monitors the world state.
 *
 * @author Eduardo Sousa
 */
public class WorldState {
    /* Gboard's labyrinth */
    private final Labyrinth maze;

    /* Position where the maze begins */
    private final Position begin;

    /* Position where the maze ends */
    private final Position end;

    /* Image that represents a monster */
    private final Gelem monsterGelem;

    /* Image that represents a tower */
    private final Gelem towerGelem;

    /* Tower locations */
    private final ConcurrentMap<Position, Tower> towerLocations;

    /* Monster locations */
    private final ConcurrentMap<Position, Monster> monsterLocations;

    /**
     * Constructor for the world state.
     *
     * @param maze labyrinth object where monsters and tower should be placed.
     * @param begin position where the monsters start their journey.
     * @param end position where the monsters must reach.
     * @param monsterGelem image that represents the monsters.
     * @param towerGelem image that represents the towers.
     */
    public WorldState(Labyrinth maze, Position begin, Position end, Gelem monsterGelem, Gelem towerGelem) {
        assert maze != null;
        assert begin != null;
        assert begin.getCoordinateX() >= 0 && begin.getCoordinateX() < maze.numberOfColumns;
        assert begin.getCoordinateY() >= 0 && begin.getCoordinateY() < maze.numberOfLines;
        assert end != null;
        assert end.getCoordinateX() >= 0 && end.getCoordinateX() < maze.numberOfColumns;
        assert end.getCoordinateY() >= 0 && end.getCoordinateY() < maze.numberOfLines;
        assert monsterGelem != null;
        assert towerGelem != null;

        this.maze = maze;
        this.begin = begin;
        this.end = end;
        this.monsterGelem = monsterGelem;
        this.towerGelem = towerGelem;
        this.towerLocations = new ConcurrentHashMap<>();
        this.monsterLocations = new ConcurrentHashMap<>();
    }

    /**
     * Method to add a tower to the world state.
     *
     * @param tower a tower object to be added.
     */
    public synchronized void addTower(Tower tower) {
        assert tower != null;
        assert isPositionInMap(tower.getPosition());
        assert isPositionForTower(tower.getPosition());

        this.maze.board.draw(towerGelem, tower.getPosition().getCoordinateY(), tower.getPosition().getCoordinateX(), 1);
        this.towerLocations.put(tower.getPosition(), tower);

        assert !towerLocations.isEmpty();
        assert towerLocations.containsKey(tower.getPosition());
    }

    /**
     * Method to add a monster to the world state.
     *
     * @param monster a monster object to be added.
     */
    public synchronized void addMonster(Monster monster) {
        assert monster != null;
        assert isPositionInMap(monster.getActualPosition());

        while(!isPositionAvailable(monster.getActualPosition()))
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        this.maze.board.draw(monsterGelem, begin.getCoordinateY(), begin.getCoordinateX(), 1);
        this.monsterLocations.put(begin, monster);

        assert !monsterLocations.isEmpty();
        assert monsterLocations.containsKey(monster.getActualPosition());
    }

    /**
     * Method to remove a monster when has been killed.
     */
    public synchronized void removeMonster() {
        assert CThread.currentThread() instanceof Monster;

        Monster monster = (Monster) CThread.currentThread();

        Position pos = this.monsterLocations.entrySet().parallelStream().filter(entry -> entry.getValue().equals(monster)).
                findFirst().get().getKey();

        this.maze.board.erase(monsterGelem, pos.getCoordinateY(), pos.getCoordinateX());
        this.monsterLocations.remove(pos);

        notifyAll();
    }

    /**
     * Method to verify if the position is not occupied by a monster.
     *
     * @param position position to be verified.
     *
     * @return true if the position is available, false otherwise.
     */
    public synchronized boolean isPositionAvailable(Position position) {
        assert CThread.currentThread() instanceof Monster;
        assert position != null;
        assert isPositionInMap(position);

        return !monsterLocations.containsKey(position);
    }

    /**
     * Method to get the monster that are in the radar range of a tower.
     *
     * @return positions of monsters inside the radar range.
     */
    public synchronized List<Position> getMonstersInRange() {
        assert CThread.currentThread() instanceof Tower;

        Tower tower = (Tower) CThread.currentThread();
        int range = tower.getRadarRange();
        Position pos = tower.getPosition();
        List<Position> positions = new ArrayList<>();

        do {
            for(int x = pos.getCoordinateX() - range; x <= pos.getCoordinateX() + range; x++) {
                for(int y = pos.getCoordinateY() - range; y <= pos.getCoordinateY() + range; y++) {
                    if(x >= 0 && x < maze.numberOfColumns && y >= 0 && y < maze.numberOfLines) {
                        Position tempPos = new Position(x, y);
                        positions.addAll(monsterLocations.keySet().parallelStream().
                                filter(position -> position.equals(tempPos)).collect(Collectors.toList()));
                    }
                }
            }

            if(positions.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (positions.isEmpty());

        assert positions != null;
        assert !positions.isEmpty();

        return positions;
    }

    /**
     * Method to shoot a monster if he is in that position.
     *
     * @param position position to shoot at.
     */
    public synchronized void shootPosition(Position position) {
        assert CThread.currentThread() instanceof Tower;

        Tower tower = (Tower) CThread.currentThread();
        Monster monster = monsterLocations.get(position);

        if(monster != null)
            monster.gotShot(tower.getShotDamage());
    }

    /**
     * Method to move monster to the next desired position.
     */
    public synchronized void moveMonster() {
        assert CThread.currentThread() instanceof Monster;

        Monster monster = (Monster) CThread.currentThread();
        Position initialPosition = this.monsterLocations.entrySet().parallelStream().filter(entry -> entry.getValue().equals(monster)).
                findFirst().get().getKey();
        Position finalPosition = monster.getActualPosition();

        while (!isPositionAvailable(finalPosition))
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        this.maze.board.move(monsterGelem, initialPosition.getCoordinateY(), initialPosition.getCoordinateX(),
                finalPosition.getCoordinateY(), finalPosition.getCoordinateX());

        this.monsterLocations.remove(initialPosition);
        this.monsterLocations.put(finalPosition, monster);

        notifyAll();
    }

    /**
     * Method to check if a position is available to place a tower.
     *
     * @param position place to place the tower.
     *
     * @return true if the position is available to place a tower, false otherwise.
     */
    public synchronized boolean isPositionForTower(Position position) {
        assert position != null;
        assert isPositionInMap(position);

        return this.maze.isWall(position.getCoordinateY(), position.getCoordinateX());
    }

    /**
     * Method to get the path beginning.
     *
     * @return the position where the path begins and the monsters should be placed.
     */
    public Position getPathBeginning() {
        return this.begin;
    }

    /**
     * Method to check if a position is in the path where monster can pass.
     *
     * @param position position to check if it is in the path.
     *
     * @return true if the position is in the path, false otherwise.
     */
    public boolean isPositionInPath(Position position) {
        assert position != null;
        assert isPositionInMap(position);

        return this.maze.isOutside(position.getCoordinateY(), position.getCoordinateX());
    }

    /**
     * Method to check if the monster has the right heading.
     *
     * @param direction direction to where the monster is facing.
     *
     * @return true if there is a path in that direction, false otherwise.
     */
    public boolean isPathInDirection(Direction direction) {
        assert direction != null;
        assert CThread.currentThread() instanceof Monster;

        Monster monster = (Monster) CThread.currentThread();
        Position pos = monster.getActualPosition();

        switch (direction) {
            case NORTH:
                pos = new Position(pos.getCoordinateX(), pos.getCoordinateY()-1);
                break;
            case EAST:
                pos = new Position(pos.getCoordinateX()+1, pos.getCoordinateY());
                break;
            case SOUTH:
                pos = new Position(pos.getCoordinateX(), pos.getCoordinateY()+1);
                break;
            case WEST:
                pos = new Position(pos.getCoordinateX()-1, pos.getCoordinateY());
                break;
        }

        assert isPositionInMap(pos);

        return this.maze.isOutside(pos.getCoordinateY(), pos.getCoordinateX());
    }

    /**
     * Method to get the place where the monsters should go to.
     *
     * @return the position where path ends.
     */
    public Position getPathEnd() {
        return end;
    }

    /**
     * Method to verify if a position is inside the map or not.
     *
     * @param position position to be tested.
     *
     * @return true if the position is inside the map, false otherwise.
     */
    public boolean isPositionInMap(Position position) {
        assert position != null;

        return position.getCoordinateX() >= 0 && position.getCoordinateX() < maze.numberOfColumns &&
                position.getCoordinateY() >= 0 && position.getCoordinateY() < maze.numberOfLines;
    }
}
