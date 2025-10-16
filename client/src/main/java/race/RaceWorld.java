package race;

import animations.BaseHorseActor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import data.race.GameMap;
import race.coordination.GameLoop;
import race.state.GameStateManager;

public class RaceWorld {
    private GameMap map;
    private final Player player;
    private final BaseHorseActor horseActor;
    private final GameLoop gameLoop;
    private final GameStateManager gameStateManager;

    public RaceWorld(GameMap map, Player player, BaseHorseActor horseActor,
                     GameLoop gameLoop, GameStateManager gameStateManager) {
        this.map = map;
        this.player = player;
        this.horseActor = horseActor;
        this.gameLoop = gameLoop;
        this.gameStateManager = gameStateManager;
    }

    public void update(float delta) {
        if (!player.isAlive()) {
            return;
        }

        try {
            gameLoop.update(player, horseActor, map, delta);
            gameStateManager.update(player, map);
        } catch (Exception e) {
            // Handle errors silently but we can change it
        }
    }

    public void render(SpriteBatch batch) {
        try {
            gameLoop.render(batch);
        } catch (Exception e) {
            // Handle errors silently but we can change it
        }
    }

    public Player getPlayer() {
        return player;
    }

    public GameMap getMap() {
        return map;
    }

    public BaseHorseActor getHorseActor() {
        return horseActor;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }
}
