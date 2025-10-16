package views.race.multiplayer;

import presentation.common.RacePresenterTemplate;
import race.config.BaseGameConfig;
import race.factory.GameFactory;
import services.managers.GameManager;

public class RaceMultiplayerPresenter extends RacePresenterTemplate {
    private final GameManager gameManager;
    private float progressSendTimer = 0f;
    private final float SENT_INTERVAL = 0.1f; // 100ms

    public RaceMultiplayerPresenter(GameFactory gameFactory, BaseGameConfig raceConfig, GameManager gameManager) {
        super(gameFactory, raceConfig);
        this.gameManager = gameManager;
    }

    public void startRace() {
        gameManager.startGame();
    }

    public void endRace() {
        gameManager.endGame();
    }

    @Override
    public void update(float delta) {
        // Check the gameManager for updates
        if (! gameManager.isGameInProgress())
            gameStateManager.completeRace();

        if (gameStateManager.isRaceFinished()) return;

        world.update(delta);
        gameStateManager.update(world.getPlayer(), world.getMap());

        progressSendTimer += delta;
        if (progressSendTimer >= SENT_INTERVAL) {
            progressSendTimer = 0f;
            int curDistance = getDistance();
            gameManager.updatePosition(curDistance);
        }
    }

    public int getCoins() {
        return world.getPlayer().getCoins();
    }
}
