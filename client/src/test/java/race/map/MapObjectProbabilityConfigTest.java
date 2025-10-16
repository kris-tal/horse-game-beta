package race.map;

import race.MapObject;
import race.objects.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy dla MapObjectProbabilityConfig.
 * Testuje konfigurację prawdopodobieństw generowania obstacles.
 */
@DisplayName("MapObjectProbabilityConfig Tests")
class MapObjectProbabilityConfigTest {

    private MapObjectProbabilityConfig config;

    @BeforeEach
    void setUp() {
        config = new MapObjectProbabilityConfig();
    }

    @Nested
    @DisplayName("Basic Configuration Tests")
    class BasicConfigurationTests {

        @Test
        @DisplayName("Should add probability correctly")
        void shouldAddProbabilityCorrectly() {

            double probability = 0.3;
            Class<? extends MapObject> objectClass = DeathStoneObject.class;


            config.addProbability(probability, objectClass);


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();
            assertEquals(1, probabilities.size());
            assertEquals(objectClass, probabilities.get(probability));
        }

        @Test
        @DisplayName("Should add multiple probabilities")
        void shouldAddMultipleProbabilities() {

            config.addProbability(0.1, DeathStoneObject.class);
            config.addProbability(0.2, PuddleObject.class);
            config.addProbability(0.3, BushObject.class);


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();


            assertEquals(3, probabilities.size());
            assertEquals(DeathStoneObject.class, probabilities.get(0.1));
            assertEquals(PuddleObject.class, probabilities.get(0.2));
            assertEquals(BushObject.class, probabilities.get(0.3));
        }

        @Test
        @DisplayName("Should handle duplicate probabilities")
        void shouldHandleDuplicateProbabilities() {

            config.addProbability(0.1, DeathStoneObject.class);
            config.addProbability(0.1, PuddleObject.class);


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();


            assertEquals(1, probabilities.size());
            assertEquals(PuddleObject.class, probabilities.get(0.1)); // Last one wins
        }

        @Test
        @DisplayName("Should handle zero probability")
        void shouldHandleZeroProbability() {

            config.addProbability(0.0, DeathStoneObject.class);


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();


            assertEquals(1, probabilities.size());
            assertEquals(DeathStoneObject.class, probabilities.get(0.0));
        }

        @Test
        @DisplayName("Should handle probability of 1.0")
        void shouldHandleProbabilityOfOne() {

            config.addProbability(1.0, DeathStoneObject.class);


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();


            assertEquals(1, probabilities.size());
            assertEquals(DeathStoneObject.class, probabilities.get(1.0));
        }

        @Test
        @DisplayName("Should handle negative probability")
        void shouldHandleNegativeProbability() {

            config.addProbability(-0.1, DeathStoneObject.class);


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();


            assertEquals(1, probabilities.size());
            assertEquals(DeathStoneObject.class, probabilities.get(-0.1));
        }

        @Test
        @DisplayName("Should handle probability greater than 1")
        void shouldHandleProbabilityGreaterThanOne() {

            config.addProbability(1.5, DeathStoneObject.class);


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();


            assertEquals(1, probabilities.size());
            assertEquals(DeathStoneObject.class, probabilities.get(1.5));
        }
    }

    @Nested
    @DisplayName("Obstacle Type Tests")
    class ObstacleTypeTests {

        @Test
        @DisplayName("Should handle all obstacle types")
        void shouldHandleAllObstacleTypes() {

            config.addProbability(0.1, DeathStoneObject.class);
            config.addProbability(0.2, ElectricBarrierObject.class);
            config.addProbability(0.3, PuddleObject.class);
            config.addProbability(0.4, BushObject.class);
            config.addProbability(0.5, MetaObject.class);
            config.addProbability(0.6, EmptyObject.class);
            config.addProbability(0.7, CoinObject.class);


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();
            assertEquals(7, probabilities.size());
            assertEquals(DeathStoneObject.class, probabilities.get(0.1));
            assertEquals(ElectricBarrierObject.class, probabilities.get(0.2));
            assertEquals(PuddleObject.class, probabilities.get(0.3));
            assertEquals(BushObject.class, probabilities.get(0.4));
            assertEquals(MetaObject.class, probabilities.get(0.5));
            assertEquals(EmptyObject.class, probabilities.get(0.6));
            assertEquals(CoinObject.class, probabilities.get(0.7));
        }

        @Test
        @DisplayName("Should handle realistic obstacle distribution")
        void shouldHandleRealisticObstacleDistribution() {

            config.addProbability(0.05, DeathStoneObject.class);      // 5% fatal obstacles
            config.addProbability(0.1, ElectricBarrierObject.class);   // 10% barriers
            config.addProbability(0.15, PuddleObject.class);           // 15% puddles
            config.addProbability(0.2, BushObject.class);             // 20% bushes
            config.addProbability(0.12, MetaObject.class);             // 12% meta objects
            config.addProbability(0.13, CoinObject.class);             // 13% coins


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();
            assertEquals(6, probabilities.size());
            assertEquals(DeathStoneObject.class, probabilities.get(0.05));
            assertEquals(ElectricBarrierObject.class, probabilities.get(0.1));
            assertEquals(PuddleObject.class, probabilities.get(0.15));
            assertEquals(BushObject.class, probabilities.get(0.2));
            assertEquals(MetaObject.class, probabilities.get(0.12));
            assertEquals(CoinObject.class, probabilities.get(0.13));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty configuration")
        void shouldHandleEmptyConfiguration() {

            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();


            assertTrue(probabilities.isEmpty());
        }

        @Test
        @DisplayName("Should handle very small probability")
        void shouldHandleVerySmallProbability() {

            config.addProbability(0.001, DeathStoneObject.class);


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();


            assertEquals(1, probabilities.size());
            assertEquals(DeathStoneObject.class, probabilities.get(0.001));
        }

        @Test
        @DisplayName("Should handle very large probability")
        void shouldHandleVeryLargeProbability() {

            config.addProbability(100.0, DeathStoneObject.class);


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();


            assertEquals(1, probabilities.size());
            assertEquals(DeathStoneObject.class, probabilities.get(100.0));
        }

        @Test
        @DisplayName("Should handle many probabilities")
        void shouldHandleManyProbabilities() {

            for (int i = 0; i < 100; i++) {
                config.addProbability(i * 0.01, DeathStoneObject.class);
            }


            Map<Double, Class<? extends MapObject>> probabilities = config.getProbabilities();
            assertEquals(100, probabilities.size());
        }
    }
}
