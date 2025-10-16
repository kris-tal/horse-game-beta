package race.factory;

import animations.BaseHorseActor;
import data.race.GameMap;
import race.Player;
import race.RaceWorld;
import race.camera.CameraSystem;
import race.collision.CollisionSystem;
import race.config.BaseGameConfig;
import race.coordination.GameLoop;
import race.input.InputHandler;
import race.map.MapGenerator;
import race.rendering.MapRenderer;
import race.rendering.PlayerRenderer;
import race.rendering.RendererSystem;
import race.rendering.VisionEffectRenderer;
import race.state.GameStateManager;

import java.util.Arrays;

public abstract class BaseGameFactory implements GameWorldFactory {
    private GameMap map;
    protected final MapGenerator mapGenerator;
    protected final InputHandler inputHandler;
    protected final BaseGameConfig config;
    protected final PlayerFactory playerFactory;
    protected final HorseActorFactory horseActorFactory;
    protected final SystemsFactory systemsFactory;

    protected BaseGameFactory(MapGenerator mapGenerator, InputHandler inputHandler, BaseGameConfig config,
                              PlayerFactory playerFactory, HorseActorFactory horseActorFactory, SystemsFactory systemsFactory) {
        this.mapGenerator = mapGenerator;
        this.inputHandler = inputHandler;
        this.config = config;
        this.playerFactory = playerFactory;
        this.horseActorFactory = horseActorFactory;
        this.systemsFactory = systemsFactory;
        this.map = null;
    }

    protected BaseGameFactory(MapGenerator mapGenerator, InputHandler inputHandler, BaseGameConfig config,
                              PlayerFactory playerFactory, HorseActorFactory horseActorFactory, SystemsFactory systemsFactory, GameMap map) {
        this.mapGenerator = mapGenerator;
        this.inputHandler = inputHandler;
        this.config = config;
        this.playerFactory = playerFactory;
        this.horseActorFactory = horseActorFactory;
        this.systemsFactory = systemsFactory;
        this.map = map;
    }

    public final RaceWorld createWorld() {
        if (map == null) map = generateMap();

        Player player = createPlayer();

        BaseHorseActor horseActor = createHorseActor(player);

        CameraSystem cameraSystem = createCameraSystem(map);
        RendererSystem rendererSystem = createRendererSystem(map, player, horseActor, cameraSystem);
        CollisionSystem collisionSystem = createCollisionSystem();
        GameLoop gameLoop = createGameLoop(cameraSystem, collisionSystem, rendererSystem);
        GameStateManager gameStateManager = createGameStateManager();

        return new RaceWorld(map, player, horseActor, gameLoop, gameStateManager);
    }

    protected abstract GameMap generateMap();

    protected Player createPlayer() {
        return playerFactory.create(config);
    }

    protected BaseHorseActor createHorseActor(Player player) {
        return horseActorFactory.create(player);
    }

    protected int getVisibleCols() {
        return config.getVisibleCols();
    }

    protected CameraSystem createCameraSystem(GameMap map) {
        return systemsFactory.createCameraSystem(map, getVisibleCols());
    }

    protected RendererSystem createRendererSystem(GameMap map, Player player, BaseHorseActor horseActor, CameraSystem cameraSystem) {
        MapRenderer mapRenderer = new MapRenderer(map, cameraSystem);
        PlayerRenderer playerRenderer = new PlayerRenderer(player, horseActor, cameraSystem);
        VisionEffectRenderer visionEffectRenderer = new VisionEffectRenderer(player);
        return new RendererSystem(Arrays.asList(mapRenderer, playerRenderer, visionEffectRenderer));
    }

    protected CollisionSystem createCollisionSystem() {
        return systemsFactory.createCollisionSystem();
    }

    protected GameLoop createGameLoop(CameraSystem cameraSystem, CollisionSystem collisionSystem, RendererSystem rendererSystem) {
        return systemsFactory.createGameLoop(inputHandler, cameraSystem, collisionSystem, rendererSystem);
    }

    protected GameStateManager createGameStateManager() {
        return systemsFactory.createGameStateManager();
    }

}
