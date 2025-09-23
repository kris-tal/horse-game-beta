package views.race.singleplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import core.HorseGame;
import data.horse.HorseData;
import data.training.TrainingData;
import data.training.TrainingResponse;
import race.RaceWorld;
import services.horse.HorseService;
import services.horse.HorseServiceImpl;
import services.managers.ResourceManager;
import services.training.TrainingService;
import services.training.TrainingServiceImpl;
import views.common.UIFactory;
import views.race.ui.RaceStatsPanel;
import views.race.ui.SprintWidget;
import views.ranch.RanchScreen;

public class RaceSingleplayerScreen implements Screen {
    private final HorseGame game;
    private final RaceSingleplayerPresenter presenter;

    private Stage uiStage, bgStage;
    private SpriteBatch batch;
    private Label distanceLabel;
    private Label coinLabel;
    private RaceStatsPanel statsPanel;
    private boolean statsPanelShown = false;
    private boolean onRaceFinished = false;
    private final TrainingService trainingService;
    private final HorseService horseService;

    public RaceSingleplayerScreen(HorseGame game) {
        this.game = game;

        race.config.RaceConfig config = race.config.RaceConfig.createDefault();//tutaj se ustalamy nasz config na razie jestesmy basic i nudni
        race.map.MapGenerator mapGenerator = new race.map.RandomMapGenerator();
        race.input.InputHandler inputHandler = new race.input.KeyboardInputHandler();
        race.factory.RaceFactory raceFactory = new race.factory.RaceFactory(mapGenerator, inputHandler);
        
        this.presenter = new RaceSingleplayerPresenter(raceFactory, config);
        trainingService = new TrainingServiceImpl();
        horseService = new HorseServiceImpl();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        bgStage = new Stage(new ScreenViewport());
        uiStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(uiStage);

        Image background = new Image(ResourceManager.raceScreenBgTexture);
        background.setFillParent(true);
        bgStage.addActor(background);

        distanceLabel = UIFactory.createMediumLabel("distance: 0m");
        distanceLabel.setPosition(30, Gdx.graphics.getHeight() - 50);
        uiStage.addActor(distanceLabel);

        coinLabel = UIFactory.createMediumLabel("coins: 0");
        coinLabel.setPosition(distanceLabel.getX() + 300, Gdx.graphics.getHeight() - 50);
        uiStage.addActor(coinLabel);

        statsPanel = new RaceStatsPanel(600, 400);
        statsPanel.setX((Gdx.graphics.getWidth() - statsPanel.getWidth()) / 2f);
        statsPanel.setY((Gdx.graphics.getHeight() - statsPanel.getHeight()) / 2f);
        uiStage.addActor(statsPanel);

        statsPanel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!statsPanel.isOpen()) {
                    game.setScreen(new RanchScreen(game));
                }
            }
        });

        presenter.init();

        float bw = 500f, bh = 30f;
        final SprintWidget sprintWidget = new SprintWidget(presenter.getWorld().getPlayer(), bw, bh);
        sprintWidget.setPosition((Gdx.graphics.getWidth() - bw) / 2f, 30f);
        uiStage.addActor(sprintWidget);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        race.input.InputManager inputManager = new race.input.InputManager();
        if (inputManager.processGlobalInput()) {
            return;
        }

        presenter.update(delta);

        if (presenter.isRaceFinished() && !statsPanelShown) {
            statsPanel.updateStats(presenter.getDistance(), presenter.getCoins());
            statsPanel.setVisible(true);
            statsPanelShown = true;
            if(!onRaceFinished) {
                HorseData horse = horseService.getUserHorses().getFirst(); // For now selection of a horse not implemented
                //Gdx.app.log("Training", "Using horse:" + horse.toString());
                TrainingData result = new TrainingData();
                result.distance = presenter.getDistance();
                result.money = presenter.getCoins();
                result.horseId = horse.getId();

                TrainingResponse response = trainingService.performTraining(result);
                //Gdx.app.log("Training", response.toString());
                onRaceFinished = true;
            }
            return;
        }
        onRaceFinished = false;
        bgStage.draw();

        batch.begin();
        RaceWorld world = presenter.getWorld();
        world.render(batch);
        batch.end();



        distanceLabel.setText("Distance: " + presenter.getDistance() + "m");
        coinLabel.setText("Coins: " + presenter.getCoins());
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        bgStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
    }
}
