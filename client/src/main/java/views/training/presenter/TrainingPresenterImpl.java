package views.training.presenter;

import data.training.TrainingData;
import data.training.TrainingResponse;
import race.Player;
import race.RaceWorld;
import training.contract.TrainingDataController;
import training.contract.TrainingGameController;
import training.contract.TrainingPresenter;
import training.contract.TrainingUIController;

public class TrainingPresenterImpl implements TrainingPresenter {
    private final TrainingGameController gameController;
    private final TrainingDataController dataController;
    private final TrainingUIController uiController;

    private boolean initialized = false;

    public TrainingPresenterImpl(
            TrainingGameController gameController,
            TrainingDataController dataController,
            TrainingUIController uiController) {
        this.gameController = gameController;
        this.dataController = dataController;
        this.uiController = uiController;
    }

    public void init() {
        if (initialized) return;

        dataController.loadHorseData();
        dataController.resetSession();
        gameController.initializeGame();
        initialized = true;
    }

    public void update(float delta) {
        if (!initialized) return;

        gameController.updateGame(delta);
        checkDistanceForPoints();
        uiController.updateUI();

    }

    private void checkDistanceForPoints() {
        RaceWorld world = gameController.getGameWorld();
        if (world == null) return;

        Player player = world.getPlayer();
        float playerCol = player.getCol();
        int currentDistance = (int) playerCol * 10; // Convert to meters
        int distanceKm = currentDistance / 1000;

        if (distanceKm > 0) {
            int pointsEarned = dataController.calculatePointsForDistance(distanceKm);
        }
    }

    public void saveTrainingDataBeforeExit(int horseId) {
        RaceWorld world = gameController.getGameWorld();
        if (world == null) return;

        Player player = world.getPlayer();
        int distance = (int) player.getCol() * 10;
        int sessionPoints = dataController.getSessionPointsEarned();

        TrainingData trainingData = createTrainingData(distance, sessionPoints, false);
        trainingData.horseId = horseId;
        TrainingResponse response = dataController.saveTrainingData(trainingData);
    }

    public void completeTraining(int horseId) {
        RaceWorld world = gameController.getGameWorld();
        if (world == null) return;

        int distance = (int) world.getPlayer().getCol() * 10;
        int sessionPoints = dataController.getSessionPointsEarned();

        TrainingData trainingData = createTrainingData(distance, sessionPoints, true);
        trainingData.horseId = horseId;
        TrainingResponse response = dataController.saveTrainingData(trainingData);

        uiController.showTrainingCompletion(true, sessionPoints, distance);
    }

    private TrainingData createTrainingData(int distance, int points, boolean success) {
        RaceWorld world = gameController.getGameWorld();
        int coins = world != null ? world.getPlayer().getCoins() : 0;

        TrainingData data = new TrainingData();
        data.distance = distance;
        data.success = success;
        data.longestRunDistance = Math.max(distance, dataController.getLongestRunDistance());
        data.pointsEarned = points;
        data.money = coins;
        return data;
    }

    public String getLongestRunDisplayText() {
        return uiController.getLongestRunDisplayText();
    }

    public String getTrainingPointsDisplayText() {
        return uiController.getTrainingPointsDisplayText();
    }

    public RaceWorld getWorld() {
        return gameController.getGameWorld();
    }

    public boolean isRaceFinished() {
        return gameController.isTrainingCompleted();
    }

    public boolean isPlayerDead() {
        RaceWorld world = gameController.getGameWorld();
        return world != null && !world.getPlayer().isAlive();
    }

    public int getDistance() {
        RaceWorld world = gameController.getGameWorld();
        return world != null ? (int) world.getPlayer().getCol() * 10 : 0;
    }

    public int getTrainingPoints() {
        return dataController.getTotalPointsEarned();
    }

    public float getSprintCooldownReduction() {
        return dataController.getTotalPointsEarned() * 0.0001f;
    }

    public String getDisplayLongestRun() {
        return String.valueOf(dataController.getLongestRunDistance());
    }

    public int getCoins() {
        RaceWorld world = gameController.getGameWorld();
        return world != null ? world.getPlayer().getCoins() : 0;
    }
}