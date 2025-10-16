package services.training;

import data.training.TrainingData;
import data.training.TrainingResponse;

public interface TrainingService {

    TrainingResponse sendTrainingData(TrainingData data);
}
