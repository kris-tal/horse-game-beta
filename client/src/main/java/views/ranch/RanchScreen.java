package views.ranch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import core.HorseGame;
import data.horse.HorseData;
import data.race.EquippedHorseService;
import data.race.GameMap;
import services.listeners.JoinLobbyListener;
import services.managers.*;
import services.ranch.RanchHorsesRefresher;
import views.common.context.MenuContext;
import views.common.ui.ConfirmationPanel;
import views.race.menu.JoinLobbyPanel;
import views.race.menu.LobbyMenuPanel;
import views.race.menu.RaceMenuPanel;
import views.race.menu.WaitingLobbyPanel;
import views.ranch.horses.HorseActorFactory;
import views.ranch.horses.HorseActors;
import views.ranch.info.HorseInfoPanel;
import views.ranch.shop.ShopPanel;

public class RanchScreen implements Screen, RanchView, JoinLobbyListener {
    private ConnectionManagerPort connectionManager;
    private final LobbyManager lobbyManager;
    private final HorseGame game;
    private final RanchPresenter presenter;
    private final MenuContext menuContext;
    private final SessionManagerPort sessionManager;
    private final EquippedHorseService equippedHorseService;

    private Stage stage;
    private RanchPanelsManager panelManager;

    private Rectangle fence;
    private HorseActors horseActors;
    private RanchHorsesRefresher refresher;

    private RaceMenuPanel raceMenuPanel;
    private LobbyMenuPanel lobbyMenuPanel;
    private WaitingLobbyPanel createLobbyPanel;
    private JoinLobbyPanel joinLobbyPanel;
    private ShopPanel shopPanel;
    private HorseInfoPanel horseInfoPanel;
    private ConfirmationPanel exitPanel;

    private RanchStatusBar statusBar;
    private final java.util.Map<Integer, com.badlogic.gdx.math.Vector2> horseIdToPosition = new java.util.HashMap<>();

    public RanchScreen(HorseGame game, SessionManagerPort sessionManager, ConnectionManagerPort connectionManager, EquippedHorseService equippedHorseService) {
        this.game = game;
        this.sessionManager = sessionManager;
        this.connectionManager = connectionManager;
        this.menuContext = new MenuContext();
        this.presenter = new RanchPresenter(this, menuContext);
        this.lobbyManager = new LobbyManager(sessionManager, connectionManager);
        this.equippedHorseService = equippedHorseService;
        equippedHorseService.ensureEquippedHorse();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        panelManager = new RanchPanelsManager(stage);

        setupBackground();
        setupHorses();
        setupPanels();
        setupStatusBar();
        if (refresher != null) {
            refresher.refreshAsync(horseActors);
        }
    }

    private void setupBackground() {
        Texture bgTexture = ResourceManager.ranchScreenBgTexture;
        Image background = new Image(bgTexture);
        background.setFillParent(true);
        stage.addActor(background);
        background.toBack();
    }

    private void setupHorses() {
        fence = new Rectangle(100, 100, 1550, 600);
        HorseActorFactory factory = new HorseActorFactory(fence, this::openHorseInfoPanel);
        horseActors = new HorseActors(stage, fence, horseIdToPosition, factory);
        refresher = new RanchHorsesRefresher(sessionManager, equippedHorseService);
        refresher.initialLoad(horseActors);
    }

    private void openHorseInfoPanel(HorseData current) {
        horseInfoPanel = new views.ranch.info.HorseInfoPanel(current, equippedHorseService);
        panelManager.registerPanel(horseInfoPanel);
        showPanel(horseInfoPanel);
    }

    private void setupPanels() {
        float panelWidth = Gdx.graphics.getWidth() / 2f;
        float panelHeight = Gdx.graphics.getHeight() / 2f;

        raceMenuPanel = new RaceMenuPanel(panelWidth, panelHeight);
        lobbyMenuPanel = new LobbyMenuPanel(panelWidth, panelHeight);
        createLobbyPanel = new WaitingLobbyPanel(panelWidth, panelHeight, lobbyManager);
        joinLobbyPanel = new JoinLobbyPanel(panelWidth, panelHeight, lobbyManager);
        shopPanel = new ShopPanel(panelWidth, panelHeight, sessionManager);

        panelManager.registerPanel(raceMenuPanel);
        panelManager.registerPanel(lobbyMenuPanel);
        panelManager.registerPanel(createLobbyPanel);
        panelManager.registerPanel(joinLobbyPanel);
        panelManager.registerPanel(shopPanel);

        raceMenuPanel.setListener(mode -> {
            switch (mode) {
                case SINGLEPLAYER -> presenter.onSingleplayerClicked();
                case MULTIPLAYER -> presenter.onMultiplayerClicked(lobbyMenuPanel);
            }
        });

        lobbyMenuPanel.setListener(mode -> {
            switch (mode) {
                case CREATE -> presenter.onCreateLobbyClicked(createLobbyPanel);
                case JOIN -> presenter.onJoinLobbyClicked(joinLobbyPanel);
            }
        });

        joinLobbyPanel.setListener(this);

        createLobbyPanel.setListener(new services.listeners.WaitingLobbyListener() {
            @Override
            public void onLobbyStarted(String data) {
                navigateToRace(data);
            }

            @Override
            public void onLeaveLobby() {
                hideAllPanels();
            }
        });

        createLobbyPanel.setOnShowCallback(() -> showPanel(createLobbyPanel));

        shopPanel.setOnPurchaseComplete(result -> {
            statusBar.updateMoney(result.getMoney());
            refresher.refreshAsync(horseActors);
        });
    }

    private void setupStatusBar() {
        statusBar = new RanchStatusBar(game, sessionManager);
        statusBar.pad(50f, 70f, 0f, 100f);
        stage.addActor(statusBar);

        statusBar.setListener(action -> {
            switch (action) {
                case SHOP -> presenter.onShopButtonClicked(shopPanel);
                case RACE -> presenter.onRaceButtonClicked(raceMenuPanel);
            }
        });
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            showExitConfirmation();
        }
        stage.act(delta);
        stage.draw();
        horseActors.updateZOrder();
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

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void showPanel(views.common.ui.Panel panel) {
        panelManager.showPanel(panel);
    }

    @Override
    public void hideAllPanels() {
        panelManager.hideAllPanels();
    }

    @Override
    public void navigateToRace(String data) {
        switch (menuContext.getRaceMode()) {
            case SINGLEPLAYER -> {
                race.map.TrainingMapGenerator mapGenerator = new race.map.TrainingMapGenerator();
                race.input.KeyboardInputHandler inputHandler = new race.input.KeyboardInputHandler();
                views.training.manager.TrainingManager trainingManager = new views.training.manager.TrainingManager(mapGenerator, inputHandler, sessionManager, connectionManager, equippedHorseService);
                views.training.screen.TrainingScreen trainingScreen = trainingManager.createTrainingScreen(game);
                game.setScreen(trainingScreen);
            }
            case MULTIPLAYER -> {
                GameMap map = GameMap.deserializeGameMap(data);
                GameManager gameManager = new GameManager(sessionManager, connectionManager, lobbyManager.getCurrentLobby());
                game.setScreen(new views.race.multiplayer.RaceMultiplayerScreen(game, sessionManager, connectionManager, equippedHorseService, gameManager, map));
            }
        }
    }

    @Override
    public void onLobbyJoined() {
        presenter.onCreateLobbyClicked(createLobbyPanel);
    }

    @Override
    public void showExitConfirmation() {
        if (exitPanel == null) {
            exitPanel = ConfirmationPanel.getInstance(400, 200, "exit?", Gdx.app::exit);
            panelManager.registerPanel(exitPanel);
        }
        panelManager.showPanel(exitPanel);
    }
}