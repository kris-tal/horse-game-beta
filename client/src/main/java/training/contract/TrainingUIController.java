package training.contract;

public interface TrainingUIController {
   
    String getLongestRunDisplayText();
    
    String getTrainingPointsDisplayText();
    
    void updateUI();
    
    void showTrainingCompletion(boolean success, int points, int distance);
    
    void showTrainingFailure(int distance);
}
