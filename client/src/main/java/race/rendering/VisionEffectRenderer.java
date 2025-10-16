package race.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import race.Player;
import services.managers.ResourceManager;

public class VisionEffectRenderer implements Renderer {

    private final Player player;

    public VisionEffectRenderer(Player player) {
        this.player = player;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (player.isVisionLimited()) {
            batch.draw(ResourceManager.visionLimitedTexture, 0, 0,
                    Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }
}
