package race.map;

import race.MapObject;

import java.util.Random;

public interface MapObjectGenerationStrategy {
    MapObject generateObject(int lane, int col, Random random);
}










