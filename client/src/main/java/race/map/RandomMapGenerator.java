package race.map;

import race.objects.*;

import java.util.Random;

public class RandomMapGenerator extends AbstractMapGenerator {
    public RandomMapGenerator() {
        super(
                new ConfigurableMapObjectGenerationStrategy(
                        createRaceConfig(),
                        (lane, col, random) -> {
                            if (col == 301) return new MetaObject();
                            if (col > 301) return new EmptyObject();
                            return null;
                        }
                ),
                new Random()
        );
    }

    private static MapObjectProbabilityConfig createRaceConfig() {
        MapObjectProbabilityConfig config = new MapObjectProbabilityConfig();
        config.addProbability(0.01, DeathStoneObject.class);
        config.addProbability(0.01, BoostObject.class);
        config.addProbability(0.05, PuddleObject.class);
        config.addProbability(0.09, BushObject.class);
        config.addProbability(0.11, ElectricBarrierObject.class);
        return config;
    }
}
