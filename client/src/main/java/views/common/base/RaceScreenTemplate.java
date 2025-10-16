package views.common.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import core.HorseGame;
import data.horse.HorseData;
import data.race.EquippedHorseService;
import race.RaceWorld;
import race.input.InputManager;
import services.managers.ConnectionManagerPort;
import services.managers.ResourceManager;
import services.managers.SessionManagerPort;
import views.ranch.RanchScreen;

public abstract class RaceScreenTemplate implements Screen {
    protected final HorseGame game;
    protected final SpriteBatch batch;
    protected final Stage bgStage;
    protected final Stage uiStage;
    protected final Skin skin;
    protected final SessionManagerPort sessionManager;
    protected final ConnectionManagerPort connectionManager;
    protected final EquippedHorseService equippedHorseService;

    protected boolean statsPanelShown = false;
    protected boolean onGameFinished = false;

    public RaceScreenTemplate(HorseGame game, SessionManagerPort sessionManager, ConnectionManagerPort connectionManager, EquippedHorseService equippedHorseService) {
        this.game = game;
        this.sessionManager = sessionManager;
        this.connectionManager = connectionManager;
        this.equippedHorseService = equippedHorseService;
        this.batch = new SpriteBatch();
        this.bgStage = new Stage(new ScreenViewport());
        this.uiStage = new Stage(new ScreenViewport());
        this.skin = ResourceManager.uiSkin;

        initializeUI();
    }

    protected abstract void initializeUI();

    protected abstract RaceWorld getGameWorld();

    protected abstract boolean isGameFinished();

    protected abstract boolean isPlayerDead();

    protected abstract void handleGameCompletion();

    protected void handleEscapeKey() {
        game.setScreen(new RanchScreen(game, sessionManager, connectionManager, equippedHorseService));
    }

    protected HorseData resolveHorse() {
        return equippedHorseService.getEquippedHorse();
    }

    @Override
    public void show() {
        Image background = new Image(ResourceManager.raceScreenBgTexture);
        background.setFillParent(true);
        bgStage.addActor(background);
        background.toBack();

        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        InputManager inputManager = new InputManager();
        if (inputManager.processGlobalInput()) {
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            handleEscapeKey();
            return;
        }

        updateGame(delta);

        if (isGameFinished() && !statsPanelShown) {
            handleGameCompletion();
            statsPanelShown = true;
            return;
        }

        onGameFinished = false;

        try {
            bgStage.draw();

            batch.begin();
            RaceWorld world = getGameWorld();
            if (world != null) {
                world.render(batch);
            }
            batch.end();
        } catch (Exception e) {
            // swallow
        }

        updateUI(delta);
        uiStage.act(delta);
        uiStage.draw();
    }

    protected abstract void updateGame(float delta);

    protected abstract void updateUI(float delta);

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
        bgStage.getViewport().update(width, height, true);
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
        if (bgStage != null) bgStage.dispose();
        if (uiStage != null) uiStage.dispose();
        if (skin != null) skin.dispose();
    }
}
