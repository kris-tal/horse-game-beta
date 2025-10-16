package race.map;

import data.race.GameMap;
import race.MapObject;
import race.objects.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Safe Path Generation Algorithm Tests")
class SafePathGenerationTest {

    private RandomMapGenerator mapGenerator;
    private TrainingMapGenerator trainingMapGenerator;

    @BeforeEach
    void setUp() {
        mapGenerator = new RandomMapGenerator();
        trainingMapGenerator = new TrainingMapGenerator();
    }

    @Nested
    @DisplayName("Basic Safe Path Tests")
    class BasicSafePathTests {

        @Test
        @DisplayName("Should ensure safe path exists in generated map")
        void shouldEnsureSafePathExistsInGeneratedMap() {
            GameMap map = mapGenerator.generateMap(3, 50);
            
            assertTrue(hasSafePath(map), "Map should have a safe path from start to end");
        }

        @Test
        @DisplayName("Should ensure safe path exists in training map")
        void shouldEnsureSafePathExistsInTrainingMap() {
            GameMap map = trainingMapGenerator.generateMap(3, 100);
            
            assertTrue(hasSafePath(map), "Training map should have a safe path from start to end");
        }

        @Test
        @DisplayName("Should handle different map sizes")
        void shouldHandleDifferentMapSizes() {
            int[] lengths = {10, 50, 100};
            int[] lanes = {3, 5};
            
            for (int laneCount : lanes) {
                for (int length : lengths) {
                    GameMap map = mapGenerator.generateMap(laneCount, length);
                    assertTrue(hasSafePath(map), 
                        String.format("Map with %d lanes and %d length should have safe path", laneCount, length));
                }
            }
        }
    }

    @Nested
    @DisplayName("Safe Path Algorithm Logic Tests")
    class SafePathAlgorithmLogicTests {

        @Test
        @DisplayName("Should handle edge case with single lane")
        void shouldHandleEdgeCaseWithSingleLane() {
            GameMap map = mapGenerator.generateMap(1, 50);
            
            assertTrue(hasSafePath(map), "Single lane map should have safe path");
        }

        @Test
        @DisplayName("Should handle edge case with two lanes")
        void shouldHandleEdgeCaseWithTwoLanes() {
            GameMap map = mapGenerator.generateMap(2, 50);
            
            assertTrue(hasSafePath(map), "Two lane map should have safe path");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should generate safe paths efficiently")
        void shouldGenerateSafePathsEfficiently() {
            long startTime = System.currentTimeMillis();
            
            for (int i = 0; i < 10; i++) {
                GameMap map = mapGenerator.generateMap(3, 50);
                assertTrue(hasSafePath(map));
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            assertTrue(duration < 2000, "Should generate 10 maps with safe paths in less than 2 seconds");
        }
    }

    private boolean hasSafePath(GameMap map) {
        int lanes = map.getLanes();
        int length = map.getLength();
        
        for (int startLane = 0; startLane < lanes; startLane++) {
            if (canReachEnd(map, startLane, 0, new boolean[lanes][length])) {
                return true;
            }
        }
        return false;
    }

    private boolean canReachEnd(GameMap map, int currentLane, int currentCol, boolean[][] visited) {
        if (currentCol >= map.getLength()) {
            return true;
        }
        
        if (visited[currentLane][currentCol]) {
            return false;
        }
        
        visited[currentLane][currentCol] = true;
        
        MapObject currentObj = map.getObject(currentLane, currentCol);
        if (currentObj.isFatal()) {
            return false;
        }
        
        for (int nextLane = Math.max(0, currentLane - 1); 
             nextLane <= Math.min(map.getLanes() - 1, currentLane + 1); 
             nextLane++) {
            if (canReachEnd(map, nextLane, currentCol + 1, visited)) {
                return true;
            }
        }
        
        return false;
    }
}
