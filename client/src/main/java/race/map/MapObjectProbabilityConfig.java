package race.map;

import race.MapObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapObjectProbabilityConfig {

    private final Map<Double, Class<? extends MapObject>> probabilities = new LinkedHashMap<>();

    public void addProbability(double probability, Class<? extends MapObject> objectClass) {
        probabilities.put(probability, objectClass);
    }

    public Map<Double, Class<? extends MapObject>> getProbabilities() {
        return probabilities;
    }
}
