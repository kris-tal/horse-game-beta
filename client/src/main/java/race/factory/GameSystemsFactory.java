package race.factory;

import data.race.GameMap;
import race.camera.CameraSystem;
import race.collision.CollisionSystem;
import race.coordination.GameLoop;
import race.input.InputHandler;
import race.rendering.RendererSystem;
import race.state.GameStateManager;

public class GameSystemsFactory implements SystemsFactory {
    @Override
    public CameraSystem createCameraSystem(GameMap map, int visibleCols) {
        return new CameraSystem(map, visibleCols);
    }


    @Override
    public CollisionSystem createCollisionSystem() {
        return new CollisionSystem();
    }

    @Override
    public GameLoop createGameLoop(InputHandler inputHandler, CameraSystem cameraSystem, CollisionSystem collisionSystem, RendererSystem rendererSystem) {
        return new GameLoop(inputHandler, cameraSystem, collisionSystem, rendererSystem);
    }

    @Override
    public GameStateManager createGameStateManager() {
        return new GameStateManager();
    }
}
