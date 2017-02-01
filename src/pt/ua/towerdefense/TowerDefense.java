package pt.ua.towerdefense;

import pt.ua.concurrent.Metronome;
import pt.ua.gboard.Gelem;
import pt.ua.gboard.ImageGelem;
import pt.ua.gboard.games.Labyrinth;
import pt.ua.gboard.games.LabyrinthGelem;
import pt.ua.towerdefense.definitions.ConfigurationValues;
import pt.ua.towerdefense.definitions.Direction;
import pt.ua.towerdefense.monsters.Monster;
import pt.ua.towerdefense.monsters.MonsterAttributes;
import pt.ua.towerdefense.towers.Tower;
import pt.ua.towerdefense.towers.TowerAttributes;
import pt.ua.towerdefense.world.Position;
import pt.ua.towerdefense.world.WorldState;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TowerDefense {
    private static int numberOfMonsters;
    private static Position begin;
    private static Position end;
    private static List<Position> towers = new ArrayList<>();
    private static List<String> map = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        if(args.length != 2) {
            System.out.println("Required arguments: <number_of_monsters> <map_file>.");
            System.exit(-1);
        }

        numberOfMonsters = Integer.decode(args[0]);

        if(numberOfMonsters <= 0) {
            System.out.println("You must pass a positive number of monsters.");
            System.exit(-1);
        }

        Scanner sc = new Scanner(args[1]);

        while (sc.hasNextLine()) {
            map.add(sc.nextLine().toUpperCase());
        }

        sc.close();

        if(map.isEmpty()) {
            System.out.println("Map must not be empty.");
            System.exit(-1);
        }

        processMap();

        if(begin == null || end == null) {
            System.out.println("One begin and end cell must be defined in the map.");
            System.exit(-1);
        }

        WorldState state = initializeWorldState();
        MonsterAttributes monsterAttributes = initializeMonsterAttributes();
        TowerAttributes towerAttributes = initializeTowerAttributes();
        Metronome metronome = initializeMetronome();

        for(Position pos : towers) {
            Tower tower = new Tower(state, metronome, pos, towerAttributes);
            tower.start();
        }

        for(int i = 0; i < numberOfMonsters; i++) {
            Monster monster = new Monster(monsterAttributes, state, metronome, state.getPathBeginning(), Direction.EAST);
            monster.start();
        }

    }

    private static void processMap() {
        List<String> processedMap = new ArrayList<>();

        for(int y = 0; y < map.size(); y++) {
            StringBuilder builder = new StringBuilder();

            for(int x = 0; x < map.get(y).length(); x++) {
                switch (map.get(y).charAt(x)) {
                    case 'B':
                        if(begin != null) {
                            System.out.println("Map must only contain one begin cell.");
                            System.exit(-1);
                        } else {
                            begin = new Position(x, y);
                        }
                        builder.append('X');
                        break;
                    case 'E':
                        if(end != null) {
                            System.out.println("Map must only contain one end cell.");
                            System.exit(-1);
                        } else {
                            end = new Position(x, y);
                        }
                        builder.append('X');
                        break;
                    case 'T':
                        towers.add(new Position(x, y));
                        builder.append('#');
                        break;
                    case 'X':
                        builder.append('X');
                        break;
                    case '#':
                        builder.append('#');
                        break;
                    default:
                        System.out.println("Unrecognized symbol in map definition. Allowed symbols: B, E, T, X, #.");
                        System.exit(-1);
                }
            }

            processedMap.add(builder.toString());
        }

        map = processedMap;
    }

    private static WorldState initializeWorldState() {
        LabyrinthGelem.setShowRoadBoundaries();
        Labyrinth maze = new Labyrinth("map.txt", new char[] {'X'}, 1);
        Gelem wall = new ImageGelem("textures/wall.png", maze.board, 100);
        maze.attachGelemToWallSymbol('#', wall);
        Gelem monster = new ImageGelem("textures/monster.png", maze.board, 100);
        Gelem tower = new ImageGelem("textures/tower.png", maze.board, 100);

        return new WorldState(maze, new Position(0, 1), new Position(9, 8),
                monster, tower);
    }

    private static MonsterAttributes initializeMonsterAttributes() {
        return new MonsterAttributes(ConfigurationValues.MONSTER_HEALTH, ConfigurationValues.MONSTER_MOVE_CYCLES,
                ConfigurationValues.MONSTER_ROTATION_CYCLES);
    }

    private static TowerAttributes initializeTowerAttributes() {
        return new TowerAttributes(ConfigurationValues.TOWER_POTENTIAL_DAMAGE, ConfigurationValues.TOWER_BUILD_COST,
                ConfigurationValues.TOWER_ROTATION_CYCLES, ConfigurationValues.TOWER_SHOOT_CYCLES,
                ConfigurationValues.TOWER_COOLDOWN_CYCLES, ConfigurationValues.TOWER_SHOT_RANGE,
                ConfigurationValues.TOWER_RADAR_RANGE, ConfigurationValues.TOWER_RADAR_CYCLES);
    }

    private static Metronome initializeMetronome() {
        return new Metronome(ConfigurationValues.TICK_MS);
    }
}
