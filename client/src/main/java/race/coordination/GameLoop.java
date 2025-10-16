package race.coordination;

import animations.BaseHorseActor;
import data.race.GameMap;
import race.Player;
import race.camera.CameraSystem;
import race.collision.CollisionSystem;
import race.input.InputHandler;
import race.rendering.RendererSystem;

public class GameLoop {
    private final InputHandler inputHandler;
    private final CameraSystem cameraSystem;
    private final CollisionSystem collisionSystem;
    private final RendererSystem rendererSystem;

    public GameLoop(InputHandler inputHandler, CameraSystem cameraSystem,
                    CollisionSystem collisionSystem, RendererSystem rendererSystem) {
        this.inputHandler = inputHandler;
        this.cameraSystem = cameraSystem;
        this.collisionSystem = collisionSystem;
        this.rendererSystem = rendererSystem;
    }

    public void update(Player player, BaseHorseActor horseActor, GameMap map, float delta) {
        if (!player.isAlive()) return;

        cameraSystem.update(player, map);

        inputHandler.processInput(player, delta);

        player.update(delta);
        horseActor.act(delta);
        horseActor.setMoving(true);

        collisionSystem.checkCollisions(player, map);
    }

    public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        rendererSystem.render(batch);
    }
}
