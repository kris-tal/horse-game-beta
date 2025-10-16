package race.config;

import data.horse.HorseData;
import data.race.EquippedHorseService;

public class GameConfigFactory {
    
    private final EquippedHorseService horseService;
    private final GameConfigValues configValues;
    
    public GameConfigFactory(EquippedHorseService horseService, GameConfigValues configValues) {
        this.horseService = horseService;
        this.configValues = configValues;
    }
    
    public BaseGameConfig createMultiplayerRaceConfig() {
        HorseData horse = horseService.getEquippedHorse();
        return createConfigWithHorse(horse, configValues.getRaceLength(), true);
    }

    public BaseGameConfig createSingleplayerRaceConfig() {
        HorseData horse = horseService.getEquippedHorse();
        return createConfigWithHorse(horse, configValues.getRaceLength(), false);
    }

    public BaseGameConfig createTrainingConfig() {
        HorseData horse = horseService.getEquippedHorse();
        return createConfigWithHorse(horse, configValues.getTrainingLength(), false);
    }

    private BaseGameConfig createConfigWithHorse(HorseData horseData, int length, boolean playerImmortal) {
        float cooldownReduction = calculateCooldownReduction(horseData);
        float sprintDuration = calculateSprintDuration(horseData);
        float sprintCooldown = calculateSprintCooldown(horseData);
        
        return new BaseGameConfig(
                configValues.getDefaultLanes(),
                length,
                configValues.getDefaultSpeed(),
                horseData.getHorseType(),
                configValues.getDefaultVisibleCols(),
                cooldownReduction,
                horseData.getLevel(),
                sprintDuration,
                sprintCooldown,
                playerImmortal
        );
    }

    private float calculateCooldownReduction(HorseData horseData) {
        return horseData.getTrainingPoints() * configValues.getTrainingPointsMultiplier();
    }

    private float calculateSprintDuration(HorseData horseData) {
        float levelBonus = horseData.getLevel() * configValues.getSprintLevelMultiplier();
        return configValues.getDefaultSprint() * (1f + levelBonus);
    }

    private float calculateSprintCooldown(HorseData horseData) {
        float cooldownReduction = calculateCooldownReduction(horseData);
        return configValues.getDefaultSprintCooldown() * (1f - cooldownReduction);
    }
}