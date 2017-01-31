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
    /* */
    private final Labyrinth maze;

    /**/
    private final Position begin;

    /**/
    private final Position end;

    /**/
    private final Gelem monsterGelem;

    /**/
    private final Gelem towerGelem;

    /* */
    private final ConcurrentMap<Position, Tower> towerLocations;

    /* */
    private final ConcurrentMap<Position, Monster> monsterLocations;

    /**
     *
     *
     * @param maze
     * @param begin
     * @param end
     * @param monsterGelem
     * @param towerGelem
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
     *
     * @param tower
     */
    public synchronized void addTower(Tower tower) {
        this.maze.board.draw(towerGelem, tower.getPosition().getCoordinateY(), tower.getPosition().getCoordinateX(), 1);
        this.towerLocations.put(tower.getPosition(), tower);
    }

    /**
     *
     * @param monster
     */
    public synchronized void addMonster(Monster monster) {
        while(!isPositionAvailable(monster.getActualPosition()))
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        this.maze.board.draw(monsterGelem, begin.getCoordinateY(), begin.getCoordinateX(), 1);
        this.monsterLocations.put(begin, monster);
    }

    /**
     *
     */
    public synchronized void removeMonster() {
        Monster monster = (Monster) CThread.currentThread();

        Position pos = this.monsterLocations.entrySet().parallelStream().filter(entry -> entry.getValue().equals(monster)).
                findFirst().get().getKey();

        this.maze.board.erase(monsterGelem, pos.getCoordinateY(), pos.getCoordinateX());
        this.monsterLocations.remove(pos);

        notifyAll();
    }

    /**
     *
     * @param position
     * @return
     */
    public synchronized boolean isPositionAvailable(Position position) {
        return !monsterLocations.containsKey(position);
    }

    /**
     *
     * @return
     */
    public synchronized List<Position> getMonstersInRange() {
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

        return positions;
    }

    /**
     *
     * @param position
     */
    public synchronized void shootPosition(Position position) {
        Tower tower = (Tower) CThread.currentThread();
        Monster monster = monsterLocations.get(position);

        if(monster != null) {
            System.out.println("Shot hit at position: " + position.getCoordinateX() + " , " + position.getCoordinateY());
            monster.gotShot(tower.getShotDamage());
        } else {
            System.out.println("Shot miss");
        }
    }

    /**
     *
     */
    public synchronized void moveMonster() {
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
     *
     * @param position
     * @return
     */
    public synchronized boolean isPositionForTower(Position position) {
        return this.maze.isWall(position.getCoordinateY(), position.getCoordinateX());
    }

    /**
     *
     * @return
     */
    public Position getPathBeginning() {
        return this.begin;
    }

    /**
     *
     * @param position
     * @return
     */
    public boolean isPositionInPath(Position position) {
        return this.maze.isOutside(position.getCoordinateY(), position.getCoordinateX());
    }

    /**
     *
     * @param direction
     * @return
     */
    public boolean isPathInDirection(Direction direction) {
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

        return this.maze.isOutside(pos.getCoordinateY(), pos.getCoordinateX());
    }

    /**
     *
     * @return
     */
    public Position getPathEnd() {
        return end;
    }

    /**
     *
     * @param position
     * @return
     */
    public boolean isPositionInMap(Position position) {
        return position.getCoordinateX() >= 0 && position.getCoordinateX() < maze.numberOfColumns &&
                position.getCoordinateY() >= 0 && position.getCoordinateY() < maze.numberOfLines;
    }
}
