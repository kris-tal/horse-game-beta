package race.map;

import data.race.GameMap;
import race.MapObject;
import race.objects.CoinObject;
import race.objects.EmptyObject;
import race.objects.DeathStoneObject;
import race.objects.BushObject;
import race.objects.ElectricBarrierObject;
import race.objects.PuddleObject;

import java.util.ArrayList;
import java.util.List;

public class RandomMapGenerator implements MapGenerator {

    @Override
    public GameMap generateMap(int lanes, int length) {
        MapObject[][] mapData = new MapObject[lanes][length];

        for (int lane = 0; lane < lanes; lane++) {
            for (int col = 0; col < length; col++) {
                mapData[lane][col] = generateObject(col);
            }
        }

        for (int col = 2; col < length; col++) {
            int prev = col - 2, mid = col - 1, curr = col;

            List<Integer> safe12 = new ArrayList<>();
            for (int lane = 0; lane < lanes; lane++) {
                if (!mapData[lane][prev].isFatal() && !mapData[lane][mid].isFatal()) {
                    safe12.add(lane);
                }
            }

            List<Integer> safe23 = new ArrayList<>();
            for (int lane = 0; lane < lanes; lane++) {
                if (!mapData[lane][mid].isFatal() && !mapData[lane][curr].isFatal()) {
                    safe23.add(lane);
                }
            }

            boolean ok = false;
            for (int l1 : safe12) {
                for (int l2 : safe23) {
                    if (Math.abs(l1 - l2) <= 1) {
                        ok = true;
                        break;
                    }
                }
                if (ok) break;
            }

            if (!ok) {
                int bridgeLane = lanes / 2;
                mapData[bridgeLane][mid] = new EmptyObject();
            }
        }

        return new GameMap(lanes, length, mapData);
    }

    private MapObject generateObject(int col) {
        if (col <= 2) {
            return new EmptyObject();
        }

        double r = Math.random();
        if (r < 0.03) { // DeathStone - najrzadszy
            return new DeathStoneObject();
        } else if (r < 0.07) { // Coin
            return new CoinObject();
        } else if (r < 0.1) { // Puddle
            return new PuddleObject();
        } else if (r < 0.15) { // Bush
            return new BushObject();
        } else if (r < 0.17) { // ElectricBarrier
            return new ElectricBarrierObject();
        } else { // reszta pusta
            return new EmptyObject();
        }
    }
}
