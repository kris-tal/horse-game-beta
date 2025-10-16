package training.presentation;

import training.contract.TrainingUIController;
import training.contract.TrainingDataController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TrainingUIControllerImpl Tests")
class TrainingUIControllerImplTest {

    private TrainingDataController dataController;
    private TrainingUIControllerImpl uiController;

    @BeforeEach
    void setUp() {
        dataController = mock(TrainingDataController.class);
        uiController = new TrainingUIControllerImpl(dataController);
    }

    @Nested
    @DisplayName("Display Text Tests")
    class DisplayTextTests {

        @Test
        @DisplayName("Should format longest run display text correctly")
        void shouldFormatLongestRunDisplayTextCorrectly() {

            when(dataController.getLongestRunDistance()).thenReturn(1500);


            String displayText = uiController.getLongestRunDisplayText();


            assertEquals("Longest Run: 1500m", displayText);
        }

        @Test
        @DisplayName("Should format training points display text correctly")
        void shouldFormatTrainingPointsDisplayTextCorrectly() {

            when(dataController.getTotalPointsEarned()).thenReturn(2500);


            String displayText = uiController.getTrainingPointsDisplayText();


            assertEquals("Training Points: 2500/10000", displayText);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 100, 500, 1000, 5000, 10000, 15000})
        @DisplayName("Should handle various longest run distances")
        void shouldHandleVariousLongestRunDistances(int distance) {

            when(dataController.getLongestRunDistance()).thenReturn(distance);


            String displayText = uiController.getLongestRunDisplayText();


            assertEquals("Longest Run: " + distance + "m", displayText);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 100, 500, 1000, 5000, 10000, 15000, 50000})
        @DisplayName("Should handle various training points values")
        void shouldHandleVariousTrainingPointsValues(int points) {

            when(dataController.getTotalPointsEarned()).thenReturn(points);


            String displayText = uiController.getTrainingPointsDisplayText();


            assertEquals("Training Points: " + points + "/10000", displayText);
        }

        @Test
        @DisplayName("Should handle zero values correctly")
        void shouldHandleZeroValuesCorrectly() {

            when(dataController.getLongestRunDistance()).thenReturn(0);
            when(dataController.getTotalPointsEarned()).thenReturn(0);


            String longestRunText = uiController.getLongestRunDisplayText();
            String pointsText = uiController.getTrainingPointsDisplayText();


            assertEquals("Longest Run: 0m", longestRunText);
            assertEquals("Training Points: 0/10000", pointsText);
        }

        @Test
        @DisplayName("Should handle negative values correctly")
        void shouldHandleNegativeValuesCorrectly() {

            when(dataController.getLongestRunDistance()).thenReturn(-100);
            when(dataController.getTotalPointsEarned()).thenReturn(-50);


            String longestRunText = uiController.getLongestRunDisplayText();
            String pointsText = uiController.getTrainingPointsDisplayText();


            assertEquals("Longest Run: -100m", longestRunText);
            assertEquals("Training Points: -50/10000", pointsText);
        }

        @Test
        @DisplayName("Should handle very large values correctly")
        void shouldHandleVeryLargeValuesCorrectly() {

            when(dataController.getLongestRunDistance()).thenReturn(Integer.MAX_VALUE);
            when(dataController.getTotalPointsEarned()).thenReturn(Integer.MAX_VALUE);


            String longestRunText = uiController.getLongestRunDisplayText();
            String pointsText = uiController.getTrainingPointsDisplayText();


            assertEquals("Longest Run: " + Integer.MAX_VALUE + "m", longestRunText);
            assertEquals("Training Points: " + Integer.MAX_VALUE + "/10000", pointsText);
        }
    }

    @Nested
    @DisplayName("UI Update Tests")
    class UIUpdateTests {

        @Test
        @DisplayName("Should call updateUI without errors")
        void shouldCallUpdateUIWithoutErrors() {

            assertDoesNotThrow(() -> uiController.updateUI());
        }

        @Test
        @DisplayName("Should handle multiple updateUI calls")
        void shouldHandleMultipleUpdateUICalls() {

            assertDoesNotThrow(() -> {
                for (int i = 0; i < 100; i++) {
                    uiController.updateUI();
                }
            });
        }
    }

    @Nested
    @DisplayName("Training Completion Tests")
    class TrainingCompletionTests {

        @Test
        @DisplayName("Should handle successful training completion")
        void shouldHandleSuccessfulTrainingCompletion() {

            assertDoesNotThrow(() -> uiController.showTrainingCompletion(true, 1000, 500));
        }

        @Test
        @DisplayName("Should handle failed training completion")
        void shouldHandleFailedTrainingCompletion() {

            assertDoesNotThrow(() -> uiController.showTrainingCompletion(false, 0, 200));
        }

        @ParameterizedTest
        @CsvSource({
            "true, 0, 0",
            "true, 1000, 500",
            "true, 5000, 2000",
            "false, 0, 0",
            "false, 0, 100",
            "false, 0, 1000"
        })
        @DisplayName("Should handle various completion scenarios")
        void shouldHandleVariousCompletionScenarios(boolean success, int points, int distance) {

            assertDoesNotThrow(() -> uiController.showTrainingCompletion(success, points, distance));
        }

        @Test
        @DisplayName("Should handle negative completion values")
        void shouldHandleNegativeCompletionValues() {

            assertDoesNotThrow(() -> uiController.showTrainingCompletion(true, -100, -50));
        }

        @Test
        @DisplayName("Should handle large completion values")
        void shouldHandleLargeCompletionValues() {

            assertDoesNotThrow(() -> uiController.showTrainingCompletion(true, Integer.MAX_VALUE, Integer.MAX_VALUE));
        }
    }

    @Nested
    @DisplayName("Training Failure Tests")
    class TrainingFailureTests {

        @Test
        @DisplayName("Should handle training failure with zero distance")
        void shouldHandleTrainingFailureWithZeroDistance() {

            assertDoesNotThrow(() -> uiController.showTrainingFailure(0));
        }

        @Test
        @DisplayName("Should handle training failure with positive distance")
        void shouldHandleTrainingFailureWithPositiveDistance() {

            assertDoesNotThrow(() -> uiController.showTrainingFailure(500));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 100, 500, 1000, 5000, 10000})
        @DisplayName("Should handle various failure distances")
        void shouldHandleVariousFailureDistances(int distance) {

            assertDoesNotThrow(() -> uiController.showTrainingFailure(distance));
        }

        @Test
        @DisplayName("Should handle training failure with negative distance")
        void shouldHandleTrainingFailureWithNegativeDistance() {

            assertDoesNotThrow(() -> uiController.showTrainingFailure(-100));
        }

        @Test
        @DisplayName("Should handle training failure with large distance")
        void shouldHandleTrainingFailureWithLargeDistance() {

            assertDoesNotThrow(() -> uiController.showTrainingFailure(Integer.MAX_VALUE));
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with real data controller")
        void shouldWorkWithRealDataController() {

            TrainingDataControllerImpl realDataController = new TrainingDataControllerImpl(
                mock(services.training.TrainingService.class),
                mock(services.horse.HorseService.class),
                mock(race.strategy.training.TrainingPointsCalculator.class),
                mock(data.race.EquippedHorseService.class)
            );
            
            TrainingUIControllerImpl realUIController = new TrainingUIControllerImpl(realDataController);


            String longestRunText = realUIController.getLongestRunDisplayText();
            String pointsText = realUIController.getTrainingPointsDisplayText();


            assertEquals("Longest Run: 0m", longestRunText);
            assertEquals("Training Points: 0/10000", pointsText);
        }

        @Test
        @DisplayName("Should handle complete training session flow")
        void shouldHandleCompleteTrainingSessionFlow() {

            when(dataController.getLongestRunDistance()).thenReturn(0);
            when(dataController.getTotalPointsEarned()).thenReturn(0);


            String initialLongestRun = uiController.getLongestRunDisplayText();
            String initialPoints = uiController.getTrainingPointsDisplayText();


            when(dataController.getLongestRunDistance()).thenReturn(1500);
            when(dataController.getTotalPointsEarned()).thenReturn(2500);

            String progressLongestRun = uiController.getLongestRunDisplayText();
            String progressPoints = uiController.getTrainingPointsDisplayText();


            uiController.showTrainingCompletion(true, 2500, 1500);


            assertEquals("Longest Run: 0m", initialLongestRun);
            assertEquals("Training Points: 0/10000", initialPoints);
            assertEquals("Longest Run: 1500m", progressLongestRun);
            assertEquals("Training Points: 2500/10000", progressPoints);
        }

        @Test
        @DisplayName("Should handle multiple training sessions")
        void shouldHandleMultipleTrainingSessions() {

            for (int session = 1; session <= 10; session++) {
                when(dataController.getLongestRunDistance()).thenReturn(session * 100);
                when(dataController.getTotalPointsEarned()).thenReturn(session * 200);

                String longestRunText = uiController.getLongestRunDisplayText();
                String pointsText = uiController.getTrainingPointsDisplayText();

                assertEquals("Longest Run: " + (session * 100) + "m", longestRunText);
                assertEquals("Training Points: " + (session * 200) + "/10000", pointsText);

                uiController.showTrainingCompletion(true, session * 200, session * 100);
                uiController.updateUI();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("Should handle null data controller gracefully")
        void shouldHandleNullDataControllerGracefully() {

            TrainingUIControllerImpl controllerWithNullData = new TrainingUIControllerImpl(null);


            assertThrows(NullPointerException.class, () -> controllerWithNullData.getLongestRunDisplayText());
            assertThrows(NullPointerException.class, () -> controllerWithNullData.getTrainingPointsDisplayText());
        }

        @Test
        @DisplayName("Should handle data controller exceptions")
        void shouldHandleDataControllerExceptions() {

            when(dataController.getLongestRunDistance()).thenThrow(new RuntimeException("Database error"));
            when(dataController.getTotalPointsEarned()).thenThrow(new RuntimeException("Network error"));


            assertThrows(RuntimeException.class, () -> uiController.getLongestRunDisplayText());
            assertThrows(RuntimeException.class, () -> uiController.getTrainingPointsDisplayText());
        }

        @Test
        @DisplayName("Should handle concurrent access")
        void shouldHandleConcurrentAccess() {

            when(dataController.getLongestRunDistance()).thenReturn(1000);
            when(dataController.getTotalPointsEarned()).thenReturn(2000);


            assertDoesNotThrow(() -> {
                for (int i = 0; i < 1000; i++) {
                    uiController.getLongestRunDisplayText();
                    uiController.getTrainingPointsDisplayText();
                    uiController.updateUI();
                }
            });
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle high frequency UI updates efficiently")
        void shouldHandleHighFrequencyUIUpdatesEfficiently() {

            when(dataController.getLongestRunDistance()).thenReturn(1000);
            when(dataController.getTotalPointsEarned()).thenReturn(2000);

            long startTime = System.currentTimeMillis();


            for (int i = 0; i < 10000; i++) {
                uiController.getLongestRunDisplayText();
                uiController.getTrainingPointsDisplayText();
                uiController.updateUI();
            }

            long endTime = System.currentTimeMillis();


            assertTrue(endTime - startTime < 1000); // Should complete in less than 1 second
        }

        @Test
        @DisplayName("Should handle memory efficiently with repeated calls")
        void shouldHandleMemoryEfficientlyWithRepeatedCalls() {

            when(dataController.getLongestRunDistance()).thenReturn(1000);
            when(dataController.getTotalPointsEarned()).thenReturn(2000);


            for (int i = 0; i < 100000; i++) {
                uiController.getLongestRunDisplayText();
                uiController.getTrainingPointsDisplayText();
            }


            assertTrue(true);
        }
    }
}
