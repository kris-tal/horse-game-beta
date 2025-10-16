import data.race.GameMap;
import org.junit.jupiter.api.Test;
import race.MapObject;
import animations.BaseHorseActor;
import race.camera.CameraSystem;
import race.collision.CollisionSystem;
import race.coordination.GameLoop;
import race.factory.GameFactory;
import race.factory.GameWorldFactory;
import race.factory.HorseActorFactory;
import race.factory.PlayerFactory;
import race.factory.SystemsFactory;
import race.input.InputHandler;
import race.map.MapGenerator;
import race.config.GameConfigFactory;
import race.config.BaseGameConfig;
import race.config.ConfigModule;
import race.RaceWorld;
import race.objects.EmptyObject;
import data.race.EquippedHorseService;
import data.horse.HorseData;
import data.horse.HorseType;
import data.horse.HorseTypeRegistry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RaceWorldCreationTest {

    @Test
    void builder_buildsFactory_andCreatesWorld() {
        MapGenerator mapGenerator = mock(MapGenerator.class);
        InputHandler inputHandler = mock(InputHandler.class);

        int lanes = 3;
        int length = 20;
        MapObject[][] data = new MapObject[lanes][length];
        for (int i = 0; i < lanes; i++) {
            for (int j = 0; j < length; j++) {
                data[i][j] = new EmptyObject();
            }
        }
        when(mapGenerator.generateMap(anyInt(), anyInt())).thenReturn(new GameMap(lanes, length, data));//kiedy zrobi sie mapowanie to zwroci mape z danymi


        EquippedHorseService equippedHorseService = mock(EquippedHorseService.class);
        HorseData testHorse = mock(HorseData.class);
        when(testHorse.getHorseType()).thenReturn(HorseTypeRegistry.getByKey("boring_horse"));
        when(testHorse.getTrainingPoints()).thenReturn(0);
        when(testHorse.getLevel()).thenReturn(0);
        when(equippedHorseService.getEquippedHorse()).thenReturn(testHorse);
        
        GameConfigFactory configFactory = ConfigModule.createGameConfigFactory(equippedHorseService);
        BaseGameConfig config = configFactory.createSingleplayerRaceConfig();

        SystemsFactory testSystems = new SystemsFactory() {
            @Override public CameraSystem createCameraSystem(GameMap map, int visibleCols) { return mock(CameraSystem.class); }
            @Override public CollisionSystem createCollisionSystem() { return mock(CollisionSystem.class); }
            @Override public GameLoop createGameLoop(InputHandler ih, CameraSystem cs, CollisionSystem c, race.rendering.RendererSystem r) { return mock(GameLoop.class); }
            @Override public race.state.GameStateManager createGameStateManager() { return mock(race.state.GameStateManager.class); }
        };

        HorseActorFactory testHorseActor = player -> mock(BaseHorseActor.class);

        GameWorldFactory factory = GameFactory.builder()
                .withMapGenerator(mapGenerator)
                .withInputHandler(inputHandler)
                .withConfig(config)
                .withSystemsFactory(testSystems)
                .withHorseActorFactory(testHorseActor)
                .build();

        RaceWorld world = factory.createWorld();

        assertNotNull(world);
        assertNotNull(world.getMap());
        assertNotNull(world.getPlayer());
        assertTrue(world.getMap().getLanes() > 0);
    }
}


