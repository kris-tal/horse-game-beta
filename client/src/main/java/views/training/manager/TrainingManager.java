// java
package views.training.manager;

import core.HorseGame;
import data.race.EquippedHorseService;
import race.input.InputHandler;
import race.map.MapGenerator;
import services.horse.HorseService;
import services.horse.HorseServiceImpl;
import services.managers.ConnectionManagerPort;
import services.managers.SessionManagerPort;
import services.training.TrainingService;
import services.training.TrainingServiceImpl;
import training.contract.TrainingPresenter;
import views.training.factory.TrainingScreenFactory;
import views.training.screen.TrainingScreen;

public class TrainingManager {
    private final TrainingService trainingService;
    private final HorseService horseService;
    private final TrainingScreenFactory screenFactory;
    private final SessionManagerPort sessionManager;
    private final ConnectionManagerPort connectionManager;
    private final EquippedHorseService equippedHorseService;

    private TrainingPresenter currentPresenter;

    public TrainingManager(MapGenerator mapGenerator, InputHandler inputHandler, SessionManagerPort sessionManager, ConnectionManagerPort connectionManager, EquippedHorseService equippedHorseService) {
        this.sessionManager = sessionManager;
        this.connectionManager = connectionManager;
        this.equippedHorseService = equippedHorseService;
        this.trainingService = new TrainingServiceImpl(sessionManager);
        this.horseService = new HorseServiceImpl(sessionManager);
        this.screenFactory = new TrainingScreenFactory(
                mapGenerator, inputHandler, trainingService, horseService
        );
    }

    public TrainingScreen createTrainingScreen(HorseGame game) {
        TrainingScreen screen = screenFactory.createDefaultTrainingScreen(game, sessionManager, connectionManager, equippedHorseService);
        currentPresenter = screen.getPresenter();
        return screen;
    }
}