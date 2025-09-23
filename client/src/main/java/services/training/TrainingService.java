package services.training;

import data.training.TrainingData;
import data.training.TrainingResponse;

public interface TrainingService {
    public TrainingResponse performTraining(TrainingData data);
}
