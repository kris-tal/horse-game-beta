package training.contract;

import data.training.TrainingData;
import data.training.TrainingResponse;

public interface TrainingDataController {
    int calculatePointsForDistance(int distanceKm);

    int getTotalPointsEarned();

    int getSessionPointsEarned();

    int getLongestRunDistance();

    TrainingResponse saveTrainingData(TrainingData trainingData);

    void loadHorseData();

    void resetSession();
}
