package race.config;

import data.horse.HorseType;

public class BaseGameConfig {
    protected final int lanes;
    protected final int length;
    protected final float speed;
    protected final HorseType horseType;
    protected final int visibleCols;
    protected final float sprintCooldownReduction;
    protected final int horseLevel;
    protected final float sprintDuration;
    protected final float sprintCooldown;
    protected final boolean playerImmortal;

    public BaseGameConfig(int lanes, int length, float speed, HorseType horseType, int visibleCols, float sprintCooldownReduction, int horseLevel, float sprintDuration, float sprintCooldown, boolean playerImmortal) {
        this.lanes = lanes;
        this.length = length;
        this.speed = speed;
        this.horseType = horseType;
        this.visibleCols = visibleCols;
        this.sprintCooldownReduction = sprintCooldownReduction;
        this.horseLevel = horseLevel;
        this.sprintDuration = sprintDuration;
        this.sprintCooldown = sprintCooldown;
        this.playerImmortal = playerImmortal;
    }

    public int getLanes() { return lanes; }
    public int getLength() { return length; }
    public float getSpeed() { return speed; }
    public HorseType getHorseType() { return horseType; }
    public int getVisibleCols() { return visibleCols; }
    public float getSprintCooldownReduction() { return sprintCooldownReduction; }
    public int getHorseLevel() { return horseLevel; }
    public float getSprintDuration() { return sprintDuration; }
    public float getSprintCooldown() { return sprintCooldown; }
    public boolean isPlayerImmortal() { return playerImmortal; }
}

