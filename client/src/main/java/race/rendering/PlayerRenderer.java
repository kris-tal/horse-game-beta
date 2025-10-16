package race.rendering;

import animations.BaseHorseActor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import race.Player;
import race.camera.CameraSystem;

public class PlayerRenderer implements Renderer {

    private final Player player;
    private final BaseHorseActor horseActor;
    private final CameraSystem camera;

    public PlayerRenderer(Player player, BaseHorseActor horseActor, CameraSystem camera) {
        this.player = player;
        this.horseActor = horseActor;
        this.camera = camera;
    }

    @Override
    public void render(SpriteBatch batch) {
        float px = camera.getLeftOffset() + camera.getCellSize() * 0.5f;
        float py = camera.getScreenY(player.getLane());

        horseActor.setPosition(px, py);
        horseActor.setSize(camera.getCellSize(), camera.getCellSize());
        horseActor.draw(batch, 1f);
    }
}
