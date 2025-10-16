package presentation.common;

import race.Player;
import race.RaceWorld;
import race.config.BaseGameConfig;
import race.factory.GameFactory;
import race.state.GameStateManager;

public abstract class RacePresenterTemplate {
    protected RaceWorld world;
    protected final GameStateManager gameStateManager;
    protected final GameFactory gameFactory;
    protected final BaseGameConfig gameConfig;
    protected boolean initialized = false;

    public RacePresenterTemplate(GameFactory gameFactory, BaseGameConfig gameConfig) {
        this.gameFactory = gameFactory;
        this.gameConfig = gameConfig;
        this.gameStateManager = new GameStateManager();
    }

    public void init() {
        world = gameFactory.createWorld();
        gameStateManager.reset();
        initialized = true;
    }

    public abstract void update(float delta);

    public RaceWorld getWorld() {
        return world;
    }

    public boolean isGameFinished() {
        return gameStateManager.isRaceFinished();
    }

    public boolean isPlayerDead() {
        return world != null && !world.getPlayer().isAlive();
    }

    public int getDistance() {
        return world != null ? (int) world.getPlayer().getCol() * 10 : 0;
    }

    public Player getPlayer() {
        return world != null ? world.getPlayer() : null;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public BaseGameConfig getGameConfig() {
        return gameConfig;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
