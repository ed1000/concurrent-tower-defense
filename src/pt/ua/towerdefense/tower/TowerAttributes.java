package pt.ua.towerdefense.tower;

public class TowerAttributes {
    private final int potentialDamage;
    private final int buildCost;
    private final int rotationalSpeed;
    private final int shootRange;
    private final int radarRange;
    private final int radarTime;

    public TowerAttributes(int potentialDamage, int buildCost, int rotationalSpeed, int shootRange, int radarRange, int radarTime) {
        this.potentialDamage = potentialDamage;
        this.buildCost = buildCost;
        this.rotationalSpeed = rotationalSpeed;
        this.shootRange = shootRange;
        this.radarRange = radarRange;
        this.radarTime = radarTime;
    }

    public int getPotentialDamage() {
        return potentialDamage;
    }

    public int getBuildCost() {
        return buildCost;
    }

    public int getRotationalSpeed() {
        return rotationalSpeed;
    }

    public int getShootRange() {
        return shootRange;
    }

    public int getRadarRange() {
        return radarRange;
    }

    public int getRadarTime() {
        return radarTime;
    }
}
