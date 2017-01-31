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

public class TowerDefense {
    public static void main(String[] args) throws InterruptedException {
        WorldState state = initializeWorldState();
        MonsterAttributes monsterAttributes = initializeMonsterAttributes();
        TowerAttributes towerAttributes = initializeTowerAttributes();
        Metronome metronome = initializeMetronome();

        Tower tower = new Tower(state, metronome, new Position(4, 0), towerAttributes);
        tower.start();
        tower = new Tower(state, metronome, new Position(4, 4), towerAttributes);
        tower.start();

        for(int i = 0; i < 25; i++) {
            Monster monster = new Monster(monsterAttributes, state, metronome, state.getPathBeginning(), Direction.EAST);
            monster.start();
        }

    }

    private static WorldState initializeWorldState() {
        LabyrinthGelem.setShowRoadBoundaries();
        Labyrinth maze = new Labyrinth("map.txt", new char[] {'X'}, 1);
        Gelem wall = new ImageGelem("wall.png", maze.board, 100);
        maze.attachGelemToWallSymbol('#', wall);
        Gelem monster = new ImageGelem("monster.png", maze.board, 100);
        Gelem tower = new ImageGelem("tower.png", maze.board, 100);

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
