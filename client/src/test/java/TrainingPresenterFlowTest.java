

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import race.config.GameConfigFactory;
import race.config.BaseGameConfig;
import race.config.ConfigModule;
import animations.BaseHorseActor;
import race.camera.CameraSystem;
import race.collision.CollisionSystem;
import race.coordination.GameLoop;
import race.factory.*;
import race.input.InputHandler;
import race.map.MapGenerator;
import services.horse.HorseService;
import services.training.TrainingService;
import training.contract.TrainingPresenter;
import views.training.presenter.TrainingPresenterFactory;
import services.managers.SessionManagerPort;
import services.requests.ProtectedRequestsService;
import data.race.EquippedHorseService;
import data.horse.HorseData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingPresenterFlowTest {

    private MapGenerator mapGenerator;
    private InputHandler inputHandler;
    private TrainingService trainingService;
    private HorseService horseService;
    private SessionManagerPort sessionManager;
    private EquippedHorseService equippedHorseService;

    @BeforeEach
    void setup() {
        mapGenerator = mock(MapGenerator.class);
        inputHandler = mock(InputHandler.class);
        trainingService = mock(TrainingService.class);
        horseService = mock(HorseService.class);
        sessionManager = mock(SessionManagerPort.class);
        equippedHorseService = mock(EquippedHorseService.class);

        when(sessionManager.isLoggedIn()).thenReturn(true);
        when(sessionManager.getProtectedRequestsService()).thenReturn(mock(ProtectedRequestsService.class));
    }

    @Test
    void presenter_initializes_and_updates() {
        HorseData testHorseData = mock(HorseData.class);
        when(testHorseData.getHorseType()).thenReturn(data.horse.HorseTypeRegistry.getByKey("boring_horse"));
        when(testHorseData.getTrainingPoints()).thenReturn(0);
        when(testHorseData.getLevel()).thenReturn(0);
        when(equippedHorseService.getEquippedHorse()).thenReturn(testHorseData);
        
        GameConfigFactory configFactory = ConfigModule.createGameConfigFactory(equippedHorseService);
        BaseGameConfig config = configFactory.createTrainingConfig();
        SystemsFactory testSystems = new SystemsFactory() {
            @Override
            public CameraSystem createCameraSystem(data.race.GameMap map, int visibleCols) {
                return mock(CameraSystem.class);
            }


            @Override
            public CollisionSystem createCollisionSystem() {
                return mock(CollisionSystem.class);
            }

            @Override
            public GameLoop createGameLoop(race.input.InputHandler ih, CameraSystem cs, CollisionSystem c, race.rendering.RendererSystem r) {
                return mock(GameLoop.class);
            }

            @Override
            public race.state.GameStateManager createGameStateManager() {
                return mock(race.state.GameStateManager.class);
            }
        };
        HorseActorFactory testHorseActor = player -> mock(BaseHorseActor.class);

        GameFactory gf = new GameFactory(mapGenerator, inputHandler, config, new StandardPlayerFactory(), testHorseActor, testSystems);

        TrainingPresenter presenter = TrainingPresenterFactory.createTrainingPresenter(
                gf,
                config,
                trainingService,
                sessionManager,
                equippedHorseService
        );

        presenter.init();
        presenter.update(0.016f);

        assertNotNull(presenter);
    }
}