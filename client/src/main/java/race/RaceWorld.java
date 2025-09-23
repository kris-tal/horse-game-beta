package race;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import data.race.GameMap;
import race.VisualComponent.HorseVisualComponent;
import race.coordination.GameLoop;

public class RaceWorld {
    private final GameMap map;
    private final Player player;
    private final HorseVisualComponent horseVisual;
    private final GameLoop gameLoop;

    public RaceWorld(GameMap map, Player player, HorseVisualComponent horseVisual, 
                    GameLoop gameLoop) {
        this.map = map;
        this.player = player;
        this.horseVisual = horseVisual;
        this.gameLoop = gameLoop;
    }

    public void update(float delta) {
        if (!player.isAlive()) {
            return;
        }
        gameLoop.update(player, horseVisual, map, delta);
    }

    public void render(SpriteBatch batch) {
        gameLoop.render(batch, map, player, horseVisual);
    }
    public Player getPlayer() { return player; }
    public GameMap getMap() { return map; }
    public HorseVisualComponent getHorseVisual() { return horseVisual; }
}
