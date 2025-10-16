package views.training.presenter;

import data.race.EquippedHorseService;
import race.config.BaseGameConfig;
import race.factory.GameFactory;
import race.strategy.training.ArithmeticProgressionPointsCalculator;
import race.strategy.training.TrainingPointsCalculator;
import services.horse.HorseService;
import services.horse.HorseServiceImpl;
import services.managers.SessionManagerPort;
import services.training.TrainingService;
import training.contract.TrainingDataController;
import training.contract.TrainingGameController;
import training.contract.TrainingPresenter;
import training.contract.TrainingUIController;
import training.presentation.TrainingDataControllerImpl;
import training.presentation.TrainingGameControllerImpl;
import training.presentation.TrainingUIControllerImpl;

public class TrainingPresenterFactory {

    public static TrainingPresenter createTrainingPresenter(
            GameFactory gameFactory,
            BaseGameConfig trainingConfig,
            TrainingService trainingService,
            SessionManagerPort sessionManager,
            EquippedHorseService equippedHorseService
    ) {

        TrainingPointsCalculator pointsCalculator = new ArithmeticProgressionPointsCalculator();
        HorseService horseService = new HorseServiceImpl(sessionManager);

        TrainingGameController gameController =
                new TrainingGameControllerImpl(gameFactory, trainingConfig);

        TrainingDataController dataController =
                new TrainingDataControllerImpl(trainingService, horseService, pointsCalculator, equippedHorseService);

        TrainingUIController uiController = new TrainingUIControllerImpl(dataController);

        return new TrainingPresenterImpl(gameController, dataController, uiController);
    }
}
