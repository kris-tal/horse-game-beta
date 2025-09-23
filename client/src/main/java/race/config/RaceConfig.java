package race.config;

import data.horse.HorseType;

public class RaceConfig {
    private final int lanes;
    private final int length;
    private final float speed;
    private final HorseType horseType;
    private final int visibleCols;

    public RaceConfig(int lanes, int length, float speed, HorseType horseType, int visibleCols) {
        this.lanes = lanes;
        this.length = length;
        this.speed = speed;
        this.horseType = horseType;
        this.visibleCols = visibleCols;
    }

    public int getLanes() { return lanes; }
    public int getLength() { return length; }
    public float getSpeed() { return speed; }
    public HorseType getHorseType() { return horseType; }
    public int getVisibleCols() { return visibleCols; }

    public static RaceConfig createDefault() {
        return new RaceConfig(3, 300, 4f, HorseType.BORING_HORSE, 10);
    }

    //tutaj tworzymy nie deafaultowe ustawienia wyścigu
    public static RaceConfig createCustom(int lanes, int length, float speed, HorseType horseType) {
        return new RaceConfig(lanes, length, speed, horseType, 10);
    }
}
