// java
package training.presentation;

import data.horse.HorseData;
import data.race.EquippedHorseService;
import data.training.TrainingData;
import data.training.TrainingResponse;
import race.strategy.training.TrainingPointsCalculator;
import services.horse.HorseService;
import services.training.TrainingService;
import training.contract.TrainingDataController;

public class TrainingDataControllerImpl implements TrainingDataController {
    private final TrainingService trainingService;
    private final HorseService horseService;
    private final TrainingPointsCalculator pointsCalculator;
    private final EquippedHorseService equippedHorseService;

    private int trainingPoints = 0;
    private int longestRunDistance = 0;
    private int pointsEarnedInThisSession = 0;
    private int lastDistanceCheckpoint = 0;


    public TrainingDataControllerImpl(TrainingService trainingService, HorseService horseService,
                                      TrainingPointsCalculator pointsCalculator, EquippedHorseService equippedHorseService) {
        this.trainingService = trainingService;
        this.horseService = horseService;
        this.pointsCalculator = pointsCalculator;
        this.equippedHorseService = equippedHorseService;
    }

    @Override
    public int calculatePointsForDistance(int distanceKm) {
        if (distanceKm > lastDistanceCheckpoint) {
            int totalPointsEarned = 0;
            for (int km = lastDistanceCheckpoint + 1; km <= distanceKm; km++) {
                int pointsForThisKm = pointsCalculator.calculatePointsForDistance(km);
                totalPointsEarned += pointsForThisKm;
            }
            pointsEarnedInThisSession += totalPointsEarned;
            lastDistanceCheckpoint = distanceKm;
            return totalPointsEarned;
        }
        return 0;
    }

    @Override
    public int getTotalPointsEarned() {
        return trainingPoints + pointsEarnedInThisSession;
    }

    public int getSessionPointsEarned() {
        return pointsEarnedInThisSession;
    }

    public int getDatabaseTrainingPoints() {
        return trainingPoints;
    }

    @Override
    public int getLongestRunDistance() {
        return longestRunDistance;
    }

    @Override
    public TrainingResponse saveTrainingData(TrainingData trainingData) {
        try {
            if (trainingData.distance > longestRunDistance) {
                longestRunDistance = trainingData.distance;
            }
            TrainingResponse response = trainingService.sendTrainingData(trainingData);

            if (response.isSuccess()) {
                trainingPoints += pointsEarnedInThisSession;
                refreshHorseDataFromServer();
            }

            resetSession();

            return response;
        } catch (Exception e) {
            return new TrainingResponse("{\"success\": false, \"msg\": \"Failed to save training data: " + e.getMessage() + "\", \"money\": 0, \"training_points\": 0, \"level\": 0, \"longest_run_distance\": 0}");
        }
    }

    @Override
    public void loadHorseData() {
        try {
            HorseData horse = equippedHorseService.getEquippedHorse();
            trainingPoints = horse.getTrainingPoints();
            longestRunDistance = horse.getLongestRunDistance();
            resetSession();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void resetSession() {
        pointsEarnedInThisSession = 0;
        lastDistanceCheckpoint = 0;
    }

    private void refreshHorseDataFromServer() {
        try {
            HorseData currentHorse = equippedHorseService.getEquippedHorse();
            if (currentHorse != null) {
                HorseData updatedHorse = horseService.getHorseDataById(currentHorse.getId());
                if (updatedHorse != null) {
                    equippedHorseService.equipHorse(updatedHorse);
                    trainingPoints = updatedHorse.getTrainingPoints();
                    longestRunDistance = updatedHorse.getLongestRunDistance();
                }
            }
        } catch (Exception ignored) {
        }
    }
}