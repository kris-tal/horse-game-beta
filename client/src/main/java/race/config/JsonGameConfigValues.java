package race.config;

public class JsonGameConfigValues implements GameConfigValues {

    @Override
    public int getDefaultLanes() {
        return GameConfigLoader.getConfig().get("defaultLanes").getAsInt();
    }

    @Override
    public int getRaceLength() {
        return GameConfigLoader.getConfig().get("raceLength").getAsInt();
    }

    @Override
    public int getTrainingLength() {
        return GameConfigLoader.getConfig().get("trainingLength").getAsInt();
    }

    @Override
    public float getDefaultSpeed() {
        return GameConfigLoader.getConfig().get("defaultSpeed").getAsFloat();
    }

    @Override
    public int getDefaultVisibleCols() {
        return GameConfigLoader.getConfig().get("defaultVisibleCols").getAsInt();
    }

    @Override
    public float getTrainingPointsMultiplier() {
        return GameConfigLoader.getConfig().get("trainingPointsMultiplier").getAsFloat();
    }

    @Override
    public float getSprintLevelMultiplier() {
        return GameConfigLoader.getConfig().get("sprintLevelMultiplier").getAsFloat();
    }

    @Override
    public float getDefaultSprint() {
        return GameConfigLoader.getConfig().get("defaultSprint").getAsFloat();
    }

    @Override
    public float getDefaultSprintCooldown() {
        return GameConfigLoader.getConfig().get("defaultSprintCooldown").getAsFloat();
    }
}
