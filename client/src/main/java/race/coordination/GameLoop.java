package race.coordination;

import race.Player;
import race.VisualComponent.HorseVisualComponent;
import race.camera.CameraSystem;
import race.collision.CollisionSystem;
import race.input.InputHandler;
import race.rendering.RendererSystem;
import data.race.GameMap;


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

    public void update(Player player, HorseVisualComponent horseVisual, GameMap map, float delta) {
        if (!player.isAlive()) return;

        cameraSystem.update(player, map);

        inputHandler.processInput(player, delta);

        player.update(delta);
        horseVisual.update(delta);
        horseVisual.setMoving(true);

        collisionSystem.checkCollisions(player, map);
    }

    public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch batch, GameMap map, 
                      Player player, HorseVisualComponent horseVisual) {
        rendererSystem.render(batch, map, player, horseVisual);
    }
}
