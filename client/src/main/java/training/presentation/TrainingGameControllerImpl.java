package training.presentation;

import race.RaceWorld;
import race.config.BaseGameConfig;
import race.factory.GameFactory;
import training.contract.TrainingGameController;

public class TrainingGameControllerImpl implements TrainingGameController {
    private final GameFactory gameFactory;
    private final BaseGameConfig trainingConfig;

    private RaceWorld world;
    private boolean trainingCompleted = false;

    public TrainingGameControllerImpl(GameFactory gameFactory, BaseGameConfig trainingConfig) {
        this.gameFactory = gameFactory;
        this.trainingConfig = trainingConfig;
    }

    @Override
    public void initializeGame() {
        world = gameFactory.createWorld();
        world.getGameStateManager().reset();
        trainingCompleted = false;
    }

    @Override
    public void updateGame(float delta) {
        if (world != null && !trainingCompleted) {
            world.update(delta);

            if (world.getGameStateManager().isRaceFinished()) {
                trainingCompleted = true;
            }
        }
    }

    @Override
    public RaceWorld getGameWorld() {
        return world;
    }

    @Override
    public boolean isTrainingCompleted() {
        return trainingCompleted;
    }
}
