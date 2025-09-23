package race.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import data.race.GameMap;
import race.Player;
import race.camera.CameraSystem;
import race.VisualComponent.HorseVisualComponent;
import services.managers.ResourceManager;

public class RendererSystem {
    private final CameraSystem camera;

    public RendererSystem(CameraSystem camera) {
        this.camera = camera;
    }

    public void render(SpriteBatch batch, GameMap map, Player player, HorseVisualComponent horseVisual) {
        renderMap(batch, map);
        renderPlayer(batch, player, horseVisual);
        renderVisionEffect(batch, player);
    }

    private void renderMap(SpriteBatch batch, GameMap map) {
        int startCol = (int) camera.getScrollX() - 1;
        int endCol = Math.min(startCol + camera.getVisibleCols() + 1, map.getLength()) + 1;

        for (int lane = 0; lane < map.getLanes(); lane++) {
            for (int col = startCol; col < endCol; col++) {
                var obj = map.getObject(lane, col);
                if (obj != null) {
                    float x = camera.getScreenX(col);
                    float y = camera.getScreenY(lane);
                    obj.render(batch, x, y, camera.getCellSize());
                }
            }
        }
    }

    private void renderPlayer(SpriteBatch batch, Player player, HorseVisualComponent horseVisual) {
        float px = camera.getLeftOffset() + camera.getCellSize() * 0.5f; // Center-left position
        float py = camera.getScreenY(player.getLane());
        
        horseVisual.render(batch, px, py, camera.getCellSize());
    }

    private void renderVisionEffect(SpriteBatch batch, Player player) {
        if (player.isVisionLimited()) {
            batch.draw(ResourceManager.visionLimitedTexture, 0, 0,
                    Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }
}
