package pt.ua.towerdefense.definitions;

/**
 * Enum that represents the type of a tile.
 *
 * @author Eduardo Sousa
 */
public enum Tile {
    /**
     * Empty tile where nothing can be placed.
     */
    EMPTY,

    /**
     * Tile where monsters can walk on.
     */
    PATH,

    /**
     * Tile where a tower can be placed.
     */
    TOWER,
}
