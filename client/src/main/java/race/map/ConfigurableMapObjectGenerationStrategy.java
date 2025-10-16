package race.map;

import race.MapObject;
import race.objects.EmptyObject;

import java.util.Map;
import java.util.Random;

public class ConfigurableMapObjectGenerationStrategy implements MapObjectGenerationStrategy {

    private final Map<Double, Class<? extends MapObject>> probabilityMap;
    private final SpecialCaseFunction specialCaseFunction;

    @FunctionalInterface
    public interface SpecialCaseFunction {
        MapObject apply(int lane, int col, Random random);
    }

    public ConfigurableMapObjectGenerationStrategy(
            MapObjectProbabilityConfig config,
            SpecialCaseFunction specialCaseFunction) {
        this.probabilityMap = config.getProbabilities();
        this.specialCaseFunction = specialCaseFunction;
    }

    @Override
    public MapObject generateObject(int lane, int col, Random random){
        if (specialCaseFunction != null) {
            MapObject special = specialCaseFunction.apply(lane, col, random);
            if (special != null) return special;
        }

        double r = random.nextDouble();
        for (Map.Entry<Double, Class<? extends MapObject>> entry : probabilityMap.entrySet()) {
            if (r < entry.getKey()) {
                try {
                    return entry.getValue().getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new EmptyObject();
    }
}
