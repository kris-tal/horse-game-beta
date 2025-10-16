package training.presentation;

import race.RaceWorld;
import race.factory.GameFactory;
import race.config.BaseGameConfig;
import race.state.GameStateManager;
import training.contract.TrainingGameController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TrainingGameControllerImpl Tests")
class TrainingGameControllerImplTest {

    private GameFactory gameFactory;
    private BaseGameConfig trainingConfig;
    private TrainingGameControllerImpl controller;
    private RaceWorld mockWorld;
    private GameStateManager mockGameStateManager;

    @BeforeEach
    void setUp() {
        gameFactory = mock(GameFactory.class);
        trainingConfig = mock(BaseGameConfig.class);
        mockWorld = mock(RaceWorld.class);
        mockGameStateManager = mock(GameStateManager.class);
        
        when(gameFactory.createWorld()).thenReturn(mockWorld);
        when(mockWorld.getGameStateManager()).thenReturn(mockGameStateManager);
        
        controller = new TrainingGameControllerImpl(gameFactory, trainingConfig);
    }

    @Nested
    @DisplayName("Game Initialization Tests")
    class GameInitializationTests {

        @Test
        @DisplayName("Should initialize game correctly")
        void shouldInitializeGameCorrectly() {
            controller.initializeGame();

            assertNotNull(controller.getGameWorld());
            assertFalse(controller.isTrainingCompleted());
            verify(gameFactory).createWorld();
            verify(mockWorld).getGameStateManager();
            verify(mockGameStateManager).reset();
        }

        @Test
        @DisplayName("Should reset training completion state on initialization")
        void shouldResetTrainingCompletionStateOnInitialization() {
            controller.initializeGame();
            when(mockGameStateManager.isRaceFinished()).thenReturn(true);
            controller.updateGame(0.016f);
            controller.initializeGame();
            assertFalse(controller.isTrainingCompleted());
        }

        @Test
        @DisplayName("Should handle multiple initializations")
        void shouldHandleMultipleInitializations() {
            controller.initializeGame();
            controller.initializeGame();
            controller.initializeGame();

            assertNotNull(controller.getGameWorld());
            assertFalse(controller.isTrainingCompleted());
            verify(gameFactory, times(3)).createWorld();
            verify(mockGameStateManager, times(3)).reset();
        }
    }

    @Nested
    @DisplayName("Game Update Tests")
    class GameUpdateTests {

        @Test
        @DisplayName("Should update world when training not completed")
        void shouldUpdateWorldWhenTrainingNotCompleted() {
            controller.initializeGame();
            when(mockGameStateManager.isRaceFinished()).thenReturn(false);

            controller.updateGame(0.016f);
            verify(mockWorld).update(0.016f);
            assertFalse(controller.isTrainingCompleted());
        }

        @Test
        @DisplayName("Should not update world when training completed")
        void shouldNotUpdateWorldWhenTrainingCompleted() {
            controller.initializeGame();
            when(mockGameStateManager.isRaceFinished()).thenReturn(true);
            controller.updateGame(0.016f);
            controller.updateGame(0.016f);

            verify(mockWorld, times(1)).update(0.016f);
            assertTrue(controller.isTrainingCompleted());
        }

        @Test
        @DisplayName("Should not update when world is null")
        void shouldNotUpdateWhenWorldIsNull() {
            controller.updateGame(0.016f);

            verify(mockWorld, never()).update(anyFloat());
            assertFalse(controller.isTrainingCompleted());
        }

        @ParameterizedTest
        @ValueSource(floats = {0.016f, 0.033f, 0.05f, 1.0f, 2.5f})
        @DisplayName("Should handle various delta time values")
        void shouldHandleVariousDeltaTimeValues(float delta) {
            controller.initializeGame();
            when(mockGameStateManager.isRaceFinished()).thenReturn(false);

            controller.updateGame(delta);

            verify(mockWorld).update(delta);
        }

        @Test
        @DisplayName("Should detect training completion correctly")
        void shouldDetectTrainingCompletionCorrectly() {
            controller.initializeGame();
            when(mockGameStateManager.isRaceFinished()).thenReturn(false);

            controller.updateGame(0.016f);
            assertFalse(controller.isTrainingCompleted());

            when(mockGameStateManager.isRaceFinished()).thenReturn(true);
            controller.updateGame(0.016f);

            assertTrue(controller.isTrainingCompleted());
        }

        @Test
        @DisplayName("Should handle rapid state changes")
        void shouldHandleRapidStateChanges() {
            controller.initializeGame();

            for (int i = 0; i < 100; i++) {
                when(mockGameStateManager.isRaceFinished()).thenReturn(i >= 50);
                controller.updateGame(0.016f);
            }

            assertTrue(controller.isTrainingCompleted());
            verify(mockWorld, times(51)).update(0.016f);
        }
    }

    @Nested
    @DisplayName("Game State Tests")
    class GameStateTests {

        @Test
        @DisplayName("Should return correct game world")
        void shouldReturnCorrectGameWorld() {
            controller.initializeGame();

            RaceWorld world = controller.getGameWorld();

            assertSame(mockWorld, world);
        }

        @Test
        @DisplayName("Should return null world before initialization")
        void shouldReturnNullWorldBeforeInitialization() {
            RaceWorld world = controller.getGameWorld();

            assertNull(world);
        }

        @Test
        @DisplayName("Should maintain training completion state")
        void shouldMaintainTrainingCompletionState() {
            controller.initializeGame();
            when(mockGameStateManager.isRaceFinished()).thenReturn(true);

            controller.updateGame(0.016f);

            assertTrue(controller.isTrainingCompleted());

            controller.updateGame(0.016f);
            controller.updateGame(0.016f);
            assertTrue(controller.isTrainingCompleted());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("Should handle null game factory")
        void shouldHandleNullGameFactory() {
            TrainingGameControllerImpl controllerWithNullFactory = 
                new TrainingGameControllerImpl(null, trainingConfig);

            assertThrows(NullPointerException.class, () -> controllerWithNullFactory.initializeGame());
        }

        @Test
        @DisplayName("Should handle world creation failure")
        void shouldHandleWorldCreationFailure() {
            when(gameFactory.createWorld()).thenThrow(new RuntimeException("World creation failed"));

            assertThrows(RuntimeException.class, () -> controller.initializeGame());
        }

        @Test
        @DisplayName("Should handle game state manager null")
        void shouldHandleGameStateManagerNull() {
            when(mockWorld.getGameStateManager()).thenReturn(null);
            assertThrows(NullPointerException.class, () -> controller.initializeGame());
        }



        @Test
        @DisplayName("Should handle very large delta time")
        void shouldHandleVeryLargeDeltaTime() {
            controller.initializeGame();
            when(mockGameStateManager.isRaceFinished()).thenReturn(false);
            controller.updateGame(1000.0f);

            verify(mockWorld).update(1000.0f);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should complete full training cycle")
        void shouldCompleteFullTrainingCycle() {
            controller.initializeGame();
            when(mockGameStateManager.isRaceFinished()).thenReturn(false);

            for (int i = 0; i < 10; i++) {
                controller.updateGame(0.016f);
            }

            when(mockGameStateManager.isRaceFinished()).thenReturn(true);
            controller.updateGame(0.016f);

            assertTrue(controller.isTrainingCompleted());
            assertNotNull(controller.getGameWorld());
            verify(mockWorld, times(11)).update(0.016f);
        }

        @Test
        @DisplayName("Should handle multiple training sessions")
        void shouldHandleMultipleTrainingSessions() {
            for (int session = 0; session < 3; session++) {
                controller.initializeGame();
                when(mockGameStateManager.isRaceFinished()).thenReturn(false);

                for (int i = 0; i < 5; i++) {
                    controller.updateGame(0.016f);
                }

                when(mockGameStateManager.isRaceFinished()).thenReturn(true);
                controller.updateGame(0.016f);
                
                assertTrue(controller.isTrainingCompleted());
            }

            verify(gameFactory, times(3)).createWorld();
            verify(mockGameStateManager, times(3)).reset();
        }

        @Test
        @DisplayName("Should maintain state consistency during rapid updates")
        void shouldMaintainStateConsistencyDuringRapidUpdates() {
            controller.initializeGame();
            when(mockGameStateManager.isRaceFinished()).thenReturn(false);
            for (int i = 0; i < 1000; i++) {
                if (i == 500) {
                    when(mockGameStateManager.isRaceFinished()).thenReturn(true);
                }
                controller.updateGame(0.001f);
            }
            assertTrue(controller.isTrainingCompleted());
            verify(mockWorld, times(501)).update(0.001f);
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle high frequency updates efficiently")
        void shouldHandleHighFrequencyUpdatesEfficiently() {
            controller.initializeGame();
            when(mockGameStateManager.isRaceFinished()).thenReturn(false);

            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                controller.updateGame(0.001f);
            }

            long endTime = System.currentTimeMillis();
            assertTrue(endTime - startTime < 1000);
            verify(mockWorld, times(10000)).update(0.001f);
        }

        @Test
        @DisplayName("Should handle continuous updates without memory leaks")
        void shouldHandleContinuousUpdatesWithoutMemoryLeaks() {
            controller.initializeGame();
            when(mockGameStateManager.isRaceFinished()).thenReturn(false);

            for (int i = 0; i < 100000; i++) {
                controller.updateGame(0.001f);
            }

            assertFalse(controller.isTrainingCompleted());
            verify(mockWorld, times(100000)).update(0.001f);
        }
    }
}
