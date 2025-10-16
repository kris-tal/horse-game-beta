package training.contract;

import race.RaceWorld;

public interface TrainingGameController {
    void initializeGame();
    
    void updateGame(float delta);
    
    RaceWorld getGameWorld();
    
    boolean isTrainingCompleted();
}
