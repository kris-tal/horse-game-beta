package race.config;

import data.race.EquippedHorseService;

public class ConfigModule {
    
    private static GameConfigValues instance;
    
    public static GameConfigValues getConfigValues() {
        if (instance == null) {
            instance = new JsonGameConfigValues();
        }
        return instance;
    }
    
    public static GameConfigFactory createGameConfigFactory(EquippedHorseService horseService) {
        return new GameConfigFactory(horseService, getConfigValues());
    }

    public static void setConfigValues(GameConfigValues configValues) {
        instance = configValues;
    }
    
    public static void reset() {
        instance = null;
    }
}
