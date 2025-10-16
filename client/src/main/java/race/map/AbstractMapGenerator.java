package race.map;

import data.race.GameMap;
import race.MapObject;
import race.objects.EmptyObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractMapGenerator implements MapGenerator {

    private final MapObjectGenerationStrategy strategy;
    private final Random random;

    protected AbstractMapGenerator(MapObjectGenerationStrategy strategy, Random random) {
        this.strategy = strategy;
        this.random = random;
    }

    @Override
    public GameMap generateMap(int lanes, int length) {
        MapObject[][] mapData = new MapObject[lanes][length];

        // Wypełnianie mapy zgodnie ze strategią
        for (int lane = 0; lane < lanes; lane++) {
            for (int col = 0; col < length; col++) {
                if (col <= 2) {
                    mapData[lane][col] = new EmptyObject();
                } else {
                    mapData[lane][col] = strategy.generateObject(lane, col, random);
                }
            }
        }
        ensureSafePath(mapData, lanes, length);

        return new GameMap(lanes, length, mapData);
    }

    private void ensureSafePath(MapObject[][] mapData, int lanes, int length) {
        for (int col = 2; col < length; col++) {
            int prev = col - 2, mid = col - 1, curr = col;

            List<Integer> safe12 = getSafeLanes(mapData, lanes, prev, mid);
            List<Integer> safe23 = getSafeLanes(mapData, lanes, mid, curr);

            if (!hasConnectedSafePath(safe12, safe23)) {
                int bridgeLane = lanes / 2;
                mapData[bridgeLane][mid] = new EmptyObject();
            }
        }
    }

    private List<Integer> getSafeLanes(MapObject[][] mapData, int lanes, int c1, int c2) {
        List<Integer> safe = new ArrayList<>();
        for (int lane = 0; lane < lanes; lane++) {
            if (!mapData[lane][c1].isFatal() && !mapData[lane][c2].isFatal()) {
                safe.add(lane);
            }
        }
        return safe;
    }

    private boolean hasConnectedSafePath(List<Integer> safe12, List<Integer> safe23) {
        for (int l1 : safe12) {
            for (int l2 : safe23) {
                if (Math.abs(l1 - l2) <= 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
