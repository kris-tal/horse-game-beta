package training.presentation;

import data.training.TrainingData;
import data.training.TrainingResponse;
import data.horse.HorseData;
import data.race.EquippedHorseService;
import services.training.TrainingService;
import services.horse.HorseService;
import race.strategy.training.TrainingPointsCalculator;
import training.contract.TrainingDataController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TrainingDataControllerImpl Tests")
class TrainingDataControllerImplTest {

    private TrainingService trainingService;
    private HorseService horseService;
    private TrainingPointsCalculator pointsCalculator;
    private EquippedHorseService equippedHorseService;
    private TrainingDataControllerImpl controller;

    @BeforeEach
    void setUp() {
        trainingService = mock(TrainingService.class);
        horseService = mock(HorseService.class);
        pointsCalculator = mock(TrainingPointsCalculator.class);
        equippedHorseService = mock(EquippedHorseService.class);
        
        controller = new TrainingDataControllerImpl(
            trainingService, horseService, pointsCalculator, equippedHorseService
        );
    }

    @Nested
    @DisplayName("Points Calculation Tests")
    class PointsCalculationTests {

        @Test
        @DisplayName("Should calculate cumulative points correctly")
        void shouldCalculateCumulativePointsCorrectly() {
            when(pointsCalculator.calculatePointsForDistance(1)).thenReturn(10);
            when(pointsCalculator.calculatePointsForDistance(2)).thenReturn(20);
            when(pointsCalculator.calculatePointsForDistance(3)).thenReturn(30);

            controller.calculatePointsForDistance(1);
            controller.calculatePointsForDistance(2);
            controller.calculatePointsForDistance(3);

            assertEquals(60, controller.getTotalPointsEarned()); 
        }
    }

    @Nested
    @DisplayName("Training Data Saving Tests")
    class TrainingDataSavingTests {

        @Test
        @DisplayName("Should save training data successfully")
        void shouldSaveTrainingDataSuccessfully() {
            controller.loadHorseData();
            TrainingData trainingData = new TrainingData();
            trainingData.horseId = 1;
            trainingData.distance = 100;
            trainingData.money = 50;
            TrainingResponse successResponse = new TrainingResponse("{\"success\": true, \"message\": \"Saved\"}");
            
            when(trainingService.sendTrainingData(trainingData)).thenReturn(successResponse);
            
            HorseData mockHorse = mock(HorseData.class);
            when(mockHorse.getId()).thenReturn(1);
            when(equippedHorseService.getEquippedHorse()).thenReturn(mockHorse);
            when(horseService.getHorseDataById(1)).thenReturn(mockHorse);

            TrainingResponse response = controller.saveTrainingData(trainingData);

            assertTrue(response.isSuccess());
            verify(trainingService).sendTrainingData(trainingData);
            verify(equippedHorseService).equipHorse(mockHorse);
        }



        @Test
        @DisplayName("Should handle training service failure")
        void shouldHandleTrainingServiceFailure() {
            controller.loadHorseData(); 
            TrainingData trainingData = new TrainingData();
            trainingData.horseId = 1;
            trainingData.distance = 100;
            trainingData.money = 50;
            TrainingResponse failureResponse = new TrainingResponse("{\"success\": false, \"msg\": \"Server error\"}");
            
            when(trainingService.sendTrainingData(trainingData)).thenReturn(failureResponse);

            TrainingResponse response = controller.saveTrainingData(trainingData);

            assertFalse(response.isSuccess());
            verify(trainingService).sendTrainingData(trainingData);
            verify(equippedHorseService, never()).equipHorse(any());
        }

        @Test
        @DisplayName("Should handle exception during save")
        void shouldHandleExceptionDuringSave() {
            controller.loadHorseData();
            TrainingData trainingData = new TrainingData();
            trainingData.horseId = 1;
            trainingData.distance = 100;
            trainingData.money = 50;
            when(trainingService.sendTrainingData(trainingData)).thenThrow(new RuntimeException("Network error"));

            TrainingResponse response = controller.saveTrainingData(trainingData);

            assertFalse(response.isSuccess());
            assertTrue(response.getMsg().contains("Failed to save training data"));
        }

    }

    @Nested
    @DisplayName("Horse Data Loading Tests")
    class HorseDataLoadingTests {

        @Test
        @DisplayName("Should load horse data successfully")
        void shouldLoadHorseDataSuccessfully() {
            HorseData horseData = mock(HorseData.class);
            when(horseData.getTrainingPoints()).thenReturn(500);
            when(horseData.getLongestRunDistance()).thenReturn(300);
            when(equippedHorseService.getEquippedHorse()).thenReturn(horseData);

            controller.loadHorseData();

            assertEquals(500, controller.getDatabaseTrainingPoints());
            assertEquals(300, controller.getLongestRunDistance());
            assertEquals(0, controller.getSessionPointsEarned());
        }

        @Test
        @DisplayName("Should handle exception during horse data loading")
        void shouldHandleExceptionDuringHorseDataLoading() {
            when(equippedHorseService.getEquippedHorse()).thenThrow(new RuntimeException("Database error"));

            assertDoesNotThrow(() -> controller.loadHorseData());
            assertEquals(0, controller.getDatabaseTrainingPoints());
            assertEquals(0, controller.getLongestRunDistance());
        }

    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("Should handle zero distance")
        void shouldHandleZeroDistance() {
            int points = controller.calculatePointsForDistance(0);

            assertEquals(0, points);
            assertEquals(0, controller.getSessionPointsEarned());
        }

        @Test
        @DisplayName("Should handle refresh failure gracefully")
        void shouldHandleRefreshFailureGracefully() {

            TrainingData trainingData = new TrainingData();
            trainingData.horseId = 1;
            trainingData.distance = 100;
            trainingData.money = 50;
            TrainingResponse successResponse = new TrainingResponse("{\"success\": true}");
            
            when(trainingService.sendTrainingData(trainingData)).thenReturn(successResponse);
            when(equippedHorseService.getEquippedHorse()).thenThrow(new RuntimeException("Refresh error"));

            assertDoesNotThrow(() -> controller.saveTrainingData(trainingData));
        }
    }


    @Nested
    @DisplayName("Performance and Stress Tests")
    class PerformanceAndStressTests {

        @Test
        @DisplayName("Should handle rapid distance updates")
        void shouldHandleRapidDistanceUpdates() {
            when(pointsCalculator.calculatePointsForDistance(anyInt())).thenReturn(1);

            for (int i = 1; i <= 1000; i++) {
                controller.calculatePointsForDistance(i);
            }

            assertEquals(1000, controller.getSessionPointsEarned());
            assertEquals(1000, controller.getTotalPointsEarned());
        }

    }
}
