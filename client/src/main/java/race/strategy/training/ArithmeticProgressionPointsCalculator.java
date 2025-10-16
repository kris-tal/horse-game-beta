package race.strategy.training;


public class ArithmeticProgressionPointsCalculator implements TrainingPointsCalculator {
    private static final int MAX_POINTS = 10000;
    
    @Override
    public int calculatePointsForDistance(int distanceKm) {
        if (distanceKm <= 0) return 0;
        return distanceKm; // 1 pkt for 1km, 2 pkt for 2km, etc.
    }
}


