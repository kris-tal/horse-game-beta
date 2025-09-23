package views.race.singleplayer;

import race.*;
import race.config.RaceConfig;
import race.factory.RaceFactory;
import race.state.GameStateManager;

public class RaceSingleplayerPresenter {
    private RaceWorld world;
    private final GameStateManager gameStateManager;
    private final RaceFactory raceFactory;
    private final RaceConfig raceConfig;

    public RaceSingleplayerPresenter(RaceFactory raceFactory, RaceConfig raceConfig) {
        this.raceFactory = raceFactory;
        this.raceConfig = raceConfig;
        this.gameStateManager = new GameStateManager();
    }

    public void init() {
        world = raceFactory.createRaceWorld(raceConfig);
        gameStateManager.reset();
    }

    public void update(float delta) {
        if (gameStateManager.isRaceFinished()) return;

        world.update(delta);
        gameStateManager.update(world.getPlayer(), world.getMap());
    }

    public RaceWorld getWorld() {
        return world;
    }

    public int getDistance() {
        return (int) (10 * world.getPlayer().getCol());
    }

    public int getCoins() {
        return world.getPlayer().getCoins();
    }

    public boolean isRaceFinished() {
        return gameStateManager.isRaceFinished();
    }

    public boolean isPlayerDead() {
        return gameStateManager.isPlayerDead();
    }

    public boolean isRaceCompleted() {
        return gameStateManager.isRaceCompleted();
    }
}
