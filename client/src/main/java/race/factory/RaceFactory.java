package race.factory;

import animations.RaceHorseActor;
import data.race.GameMap;
import race.Player;
import race.RaceWorld;
import race.VisualComponent.HorseVisualComponent;
import race.camera.CameraSystem;
import race.collision.CollisionSystem;
import race.coordination.GameLoop;
import race.config.RaceConfig;
import race.input.InputHandler;
import race.map.MapGenerator;
import race.rendering.RendererSystem;

public class RaceFactory {
    private final MapGenerator mapGenerator;
    private final InputHandler inputHandler;

    public RaceFactory(MapGenerator mapGenerator, InputHandler inputHandler) {
        this.mapGenerator = mapGenerator;
        this.inputHandler = inputHandler;
    }

    public RaceWorld createRaceWorld(RaceConfig config) {
        GameMap map = mapGenerator.generateMap(config.getLanes(), config.getLength());
        Player player = new Player(config.getHorseType(), config.getLanes() / 2, config.getSpeed());
        player.makeSprintAvailable();

        RaceHorseActor raceHorseActor = new RaceHorseActor(config.getHorseType());
        HorseVisualComponent horseVisual = new HorseVisualComponent(config.getHorseType(), raceHorseActor);
        CameraSystem cameraSystem = new CameraSystem(map, config.getVisibleCols());
        RendererSystem rendererSystem = new RendererSystem(cameraSystem);
        CollisionSystem collisionSystem = new CollisionSystem();
        GameLoop gameLoop = new GameLoop(inputHandler, cameraSystem, collisionSystem, rendererSystem);

        return new RaceWorld(map, player, horseVisual, gameLoop);
    }
}
