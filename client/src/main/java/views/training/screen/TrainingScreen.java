// java
package views.training.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import core.HorseGame;
import data.horse.HorseData;
import data.race.EquippedHorseService;
import race.RaceWorld;
import services.horse.HorseService;
import services.managers.ConnectionManagerPort;
import services.managers.SessionManagerPort;
import services.training.TrainingService;
import training.contract.TrainingPresenter;
import views.common.base.RaceScreenTemplate;
import views.race.ui.SprintWidget;
import views.ranch.RanchScreen;
import views.training.ui.TrainingStatsPanel;
import views.training.ui.TrainingStatusBar;

public class TrainingScreen extends RaceScreenTemplate {
    private final TrainingPresenter presenter;
    private final TrainingService trainingService;
    private final HorseService horseService;
    private final SessionManagerPort sessionManager;
    private final ConnectionManagerPort connectionManager;
    private final EquippedHorseService equippedHorseService;

    private float elapsedSeconds = 0f;
    private static final float SPEEDUP_RATE_PER_SECOND = 0.02f;
    private static final float MAX_SPEED_MULTIPLIER = 4f;

    private SprintWidget sprintWidget;
    private TrainingStatsPanel statsPanel;
    private TrainingStatusBar statusBar;

    public TrainingScreen(HorseGame game,
                          TrainingPresenter presenter,
                          TrainingService trainingService,
                          HorseService horseService,
                          SessionManagerPort sessionManager,
                          ConnectionManagerPort connectionManager,
                          EquippedHorseService equippedHorseService) {
        super(game, sessionManager, connectionManager, equippedHorseService);
        this.presenter = presenter;
        this.trainingService = trainingService;
        this.horseService = horseService;
        this.sessionManager = sessionManager;
        this.equippedHorseService = equippedHorseService;
        this.connectionManager = connectionManager;
    }

    @Override
    protected void initializeUI() {
        Table root = new Table();
        root.setFillParent(true);
        root.top().left();
        root.pad(60f, 60f, 0f, 0f);

        statusBar = new TrainingStatusBar();
        root.add(statusBar).left().top();

        uiStage.addActor(root);

        statsPanel = new TrainingStatsPanel(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        statsPanel.setX((Gdx.graphics.getWidth() - statsPanel.getWidth()) / 2f);
        statsPanel.setY((Gdx.graphics.getHeight() - statsPanel.getHeight()) / 2f);
        statsPanel.setVisible(false);

        uiStage.addActor(statsPanel);
    }

    @Override
    protected RaceWorld getGameWorld() {
        return presenter.getWorld();
    }

    public TrainingPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected boolean isGameFinished() {
        return presenter.isRaceFinished();
    }

    @Override
    protected boolean isPlayerDead() {
        return presenter.isPlayerDead();
    }

    @Override
    protected void handleGameCompletion() {
        statsPanel.setVisible(true);
        try {
            int distance = presenter.getDistance();
            int currentLongest = Integer.parseInt(presenter.getDisplayLongestRun());
            boolean isNewRecord = distance > currentLongest;
            int longestToShow = Math.max(distance, currentLongest);
            int coins = presenter.getCoins();
            statsPanel.updateStats(distance, longestToShow, coins, isNewRecord);
        } catch (Exception ignored) {
        }

        if (!onGameFinished) {
            if (presenter.isPlayerDead()) {
                HorseData horse = resolveHorse();
                if (horse != null) {
                    presenter.saveTrainingDataBeforeExit(horse.getId());
                }
                onGameFinished = true;
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        Gdx.app.postRunnable(() -> game.setScreen(new RanchScreen(game, sessionManager, connectionManager, equippedHorseService)));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            } else {
                HorseData horse = resolveHorse();
                if (horse != null) {
                    presenter.completeTraining(horse.getId());
                }
                onGameFinished = true;
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        Gdx.app.postRunnable(() -> game.setScreen(new RanchScreen(game, sessionManager, connectionManager, equippedHorseService)));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
        }
    }

    @Override
    protected void updateGame(float delta) {
        elapsedSeconds += delta;

        float multiplier = 1f + elapsedSeconds * SPEEDUP_RATE_PER_SECOND;
        if (multiplier > MAX_SPEED_MULTIPLIER) {
            multiplier = MAX_SPEED_MULTIPLIER;
        }

        float effectiveDelta = delta * multiplier;
        presenter.update(effectiveDelta);
    }

    @Override
    protected void updateUI(float delta) {
        try {
            if (statusBar != null) {
                statusBar.setDistance(presenter.getDistance());
                statusBar.setTrainingPoints(presenter.getTrainingPoints());
                try {
                    statusBar.setLongestRun(Integer.parseInt(presenter.getDisplayLongestRun()));
                } catch (Exception ignored) {
                }
                statusBar.setCoins(presenter.getCoins());
            }

            if (sprintWidget != null && presenter.getWorld() != null && presenter.getWorld().getPlayer() != null) {
                sprintWidget.act(delta);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void show() {
        super.show();
        presenter.init();

        elapsedSeconds = 0f;

        float bw = 500f, bh = 30f;
        if (presenter.getWorld() != null && presenter.getWorld().getPlayer() != null) {
            sprintWidget = new SprintWidget(presenter.getWorld().getPlayer(), bw, bh);
            sprintWidget.setPosition((Gdx.graphics.getWidth() - bw) / 2f, 30f);
            uiStage.addActor(sprintWidget);
        }
    }

    @Override
    protected void handleEscapeKey() {
        HorseData horse = resolveHorse();
        if (horse != null) {
            presenter.saveTrainingDataBeforeExit(horse.getId());
        }
        game.setScreen(new RanchScreen(game, sessionManager, connectionManager, equippedHorseService));
    }

    protected HorseData resolveHorse() {
        return equippedHorseService.getEquippedHorse();
    }
}