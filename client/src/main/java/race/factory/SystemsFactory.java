package race.factory;

import data.race.GameMap;
import race.camera.CameraSystem;
import race.collision.CollisionSystem;
import race.coordination.GameLoop;
import race.input.InputHandler;
import race.rendering.RendererSystem;
import race.state.GameStateManager;

public interface SystemsFactory {
    CameraSystem createCameraSystem(GameMap map, int visibleCols);

    CollisionSystem createCollisionSystem();

    GameLoop createGameLoop(InputHandler inputHandler, CameraSystem cameraSystem, CollisionSystem collisionSystem, RendererSystem rendererSystem);

    GameStateManager createGameStateManager();
}


