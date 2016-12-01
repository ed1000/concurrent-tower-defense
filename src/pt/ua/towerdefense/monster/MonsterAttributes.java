package pt.ua.towerdefense.monster;

public class MonsterAttributes {
    private final int health;
    private final int speed;

    public MonsterAttributes(int health, int speed) {
        assert health > 0;
        assert speed > 0;

        this.health = health;
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public int getSpeed() {
        return speed;
    }
}
