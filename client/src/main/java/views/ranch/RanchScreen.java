package views.ranch;

import animations.StrollingHorseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import core.HorseGame;
import data.horse.HorseData;
import data.horse.HorseType;
import services.horse.HorseService;
import services.horse.HorseServiceImpl;
import services.managers.LobbyManager;
import views.common.ConfirmationPanel;
import views.common.ListPanel;
import views.common.Panel;
import views.race.multiplayer.RaceMultiplayerScreen;
import views.race.singleplayer.RaceSingleplayerScreen;
import views.ranch.race.*;
import views.ranch.shop.ShopPanel;
import views.common.MenuContext;
import services.managers.ResourceManager;

import java.util.List;

public class RanchScreen implements Screen, RanchView, JoinLobbyPanel.JoinLobbyListener {
    private final HorseGame game;
    private final RanchPresenter presenter;
    private final MenuContext menuContext;
    private Stage stage;

    private RaceMenuPanel raceMenuPanel;
    private HorseSelectionPanel horseSelectionPanel;
    private LobbyMenuPanel lobbyMenuPanel;
    private WaitingLobbyPanel createLobbyPanel;
    private JoinLobbyPanel joinLobbyPanel;
    private ShopPanel shopPanel;

    private Array<StrollingHorseActor> horses = new Array<StrollingHorseActor>();
    private Rectangle fence;

    private RaceMenuPanel.RaceMenuListener listener = new MockMenuListener();

    public RanchScreen(HorseGame game) {
        this.game = game;
        this.menuContext = new MenuContext();
        this.presenter = new RanchPresenter(this, menuContext);
    }

    @Override
    public void show() {
        fence = new Rectangle(100, 100, 1550, 600);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = ResourceManager.uiSkin;
        HorseService horseService = new HorseServiceImpl();
        List<HorseData> ownedHorses = horseService.getUserHorses();

        for (HorseData horse : ownedHorses) {
            HorseType horseType = HorseType.fromId(horse.getName());

            // pass fence bounds to StrollingHorseActor
            StrollingHorseActor strollingHorseActor = new StrollingHorseActor(
                    horseType,
                    fence.x,
                    fence.x + fence.width,
                    fence.y,
                    fence.y + fence.height
            );

            // place horse at random starting position
            float x = MathUtils.random(fence.x, fence.x + fence.width);
            float y = MathUtils.random(fence.y, fence.y + fence.height);
            strollingHorseActor.setPosition(x, y);

            horses.add(strollingHorseActor);
            stage.addActor(strollingHorseActor);
        }

        Texture bgTexture = ResourceManager.ranchScreenBgTexture;
        Image background = new Image(bgTexture);
        background.setFillParent(true);
        stage.addActor(background);
        background.toBack();

        RanchStatusBar statusBar = getStatusBar(skin);
        stage.addActor(statusBar);

        float panelWidth = Gdx.graphics.getWidth() / 2f;
        float panelHeight = Gdx.graphics.getHeight() / 2f;

        raceMenuPanel = new RaceMenuPanel(panelWidth, panelHeight);
        horseSelectionPanel = new HorseSelectionPanel(panelWidth, panelHeight);
        lobbyMenuPanel = new LobbyMenuPanel(panelWidth, panelHeight);
        lobbyMenuPanel.setListener(mode -> {
            menuContext.setLobbyMode(mode);
            switch (mode) {
                case CREATE -> {
                    LobbyManager.getInstance().createLobby();
                    presenter.onCreateLobbyClicked(createLobbyPanel);
                }
                case JOIN -> presenter.onJoinLobbyClicked(joinLobbyPanel);
            }
        });
        createLobbyPanel = new WaitingLobbyPanel(panelWidth, panelHeight);
        joinLobbyPanel = new JoinLobbyPanel(panelWidth, panelHeight);
        joinLobbyPanel.setListener(this);
        shopPanel = new ShopPanel(panelWidth, panelHeight);
        shopPanel.setOnPurchaseComplete(result -> {
            Gdx.app.log("Shop", result.toString());
            statusBar.updateMoney(result.money);
        });
        raceMenuPanel.setListener(listener);
        raceMenuPanel.setListener(new RaceMenuPanel.RaceMenuListener() {
            @Override
            public void onModeSelected(MenuContext.RaceMode mode) {
                switch (mode) {
                    case SINGLEPLAYER -> {
                        presenter.onSingleplayerClicked();
                    }
                    case MULTIPLAYER -> {
                        presenter.onMultiplayerClicked(lobbyMenuPanel);
                    }
                }
            }
        });


        stage.addActor(raceMenuPanel);
        stage.addActor(horseSelectionPanel);
        stage.addActor(lobbyMenuPanel);
        stage.addActor(createLobbyPanel);
        stage.addActor(joinLobbyPanel);
        stage.addActor(shopPanel);

        hideAllPanels();
    }

    private RanchStatusBar getStatusBar(Skin skin) {
        RanchStatusBar statusBar = new RanchStatusBar(game, skin);
        statusBar.setListener(action -> {
            switch (action) {
                case SHOP:
                    presenter.onShopButtonClicked(shopPanel);
                    break;
                case RACE:
                    presenter.onRaceButtonClicked(raceMenuPanel);
                    break;
                case RANKING:
                    presenter.onRankingButtonClicked(null);
                    break;
            }
        });
        return statusBar;
    }

    //RanchView methods

    public void showPanel(Panel panel) {
        hideAllPanels();
        panel.show(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (panel instanceof ListPanel listPanel) {
            stage.setScrollFocus(listPanel.getScrollPane());
        }
    }

    public void hideAllPanels() {
        raceMenuPanel.hide();
        horseSelectionPanel.hide();
        lobbyMenuPanel.hide();
        createLobbyPanel.hide();
        joinLobbyPanel.hide();
        shopPanel.hide();
    }

    @Override
    public void navigateToRace() {
        switch (menuContext.getRaceMode()) {
            case SINGLEPLAYER -> game.setScreen(new RaceSingleplayerScreen(game));
            case MULTIPLAYER -> game.setScreen(new RaceMultiplayerScreen(game));
        }
    }


    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            showExitConfirmation();
        }

        stage.act(delta);

        horses.sort((a, b) -> Float.compare(b.getY(), a.getY()));

        for (int i = 0; i < horses.size; i++) {
            horses.get(i).setZIndex(i + 1);
        }

        stage.draw();
    }


    @Override
    public void onLobbyJoined() {
        presenter.onCreateLobbyClicked(createLobbyPanel);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    private void onRaceMenuButtonClicked() {
        showPanel(raceMenuPanel);
    }

    private void onSingleplayerChosen() {
        showPanel(horseSelectionPanel);
    }

    private void onMultiplayerChosen() {
        showPanel(horseSelectionPanel);
        // after horse selection:
        // showPanel(lobbyPanel);
    }

    private void showExitConfirmation() {
        ConfirmationPanel confirmationPanel = ConfirmationPanel.getInstance(400, 200, "Exit?", () -> {
            Gdx.app.log("RanchScreen", "confirmed exit");
            Gdx.app.exit();
        });
        stage.addActor(confirmationPanel);
        showPanel(confirmationPanel);
    }

    public void dispose() {
    }

}
