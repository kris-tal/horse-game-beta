package views.race.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import core.HorseGame;
import data.lobby.PlayerInfoClient;
import data.race.EquippedHorseService;
import data.race.GameMap;
import race.RaceWorld;
import race.config.ConfigModule;
import race.config.GameConfigFactory;
import services.managers.ConnectionManagerPort;
import services.managers.GameManager;
import services.managers.SessionManagerPort;
import services.profile.ProfileService;
import services.profile.ProfileServiceImpl;
import views.common.base.RaceScreenTemplate;
import views.common.ui.UIFactory;
import views.race.ui.RaceMultiplayerStatsPanel;
import views.race.ui.SprintWidget;
import views.ranch.RanchScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Multiplayer race game loop screen
public class RaceMultiplayerScreen extends RaceScreenTemplate {
    private final RaceMultiplayerPresenter presenter;
    private Label distanceLabel;
    private Label coinLabel;
    private RaceMultiplayerStatsPanel statsPanel;
    private SprintWidget sprintWidget;
    private final GameManager gameManager;
    private Table scoreboardTable;
    // Constants for the race progress bar
    private final float BAR_WIDTH = 300f;
    private final float BAR_HEIGHT = 18f;
    private final int FINISH_DISTANCE;
    // misc
    private final String localUsername;

    public RaceMultiplayerScreen(HorseGame game, SessionManagerPort sessionManager, ConnectionManagerPort connectionManager, EquippedHorseService equippedHorseService, GameManager gameManager, GameMap map) {
        super(game, sessionManager, connectionManager, equippedHorseService);
        this.gameManager = gameManager;

        GameConfigFactory configFactory = ConfigModule.createGameConfigFactory(equippedHorseService);
        race.config.BaseGameConfig config = configFactory.createMultiplayerRaceConfig();
        race.map.MapGenerator mapGenerator = new race.map.RandomMapGenerator();
        race.input.InputHandler inputHandler = new race.input.KeyboardInputHandler();

        race.factory.GameFactory raceFactory = new race.factory.GameFactory(mapGenerator, inputHandler, config, map);

        presenter = new RaceMultiplayerPresenter(raceFactory, config, gameManager);
        presenter.init();
        FINISH_DISTANCE = presenter.getWorld().getMap().getLength();

        ProfileService profileService = new ProfileServiceImpl(sessionManager);
        localUsername = profileService.getUserData().getUsername();

        // DEBUG: Start the race
        gameManager.startGame();
    }

    @Override
    public void initializeUI() {

        distanceLabel = UIFactory.createLabel("Distance: 0m");
        coinLabel = UIFactory.createLabel("Coins: 0");

        //distanceLabel.setStyle(skin.get("default", Label.LabelStyle.class));
        //coinLabel.setStyle(skin.get("default", Label.LabelStyle.class));

        uiStage.addActor(distanceLabel);
        uiStage.addActor(coinLabel);

        statsPanel = new RaceMultiplayerStatsPanel(600, 400);
        statsPanel.setX((Gdx.graphics.getWidth() - statsPanel.getWidth()) / 2f);
        statsPanel.setY((Gdx.graphics.getHeight() - statsPanel.getHeight()) / 2f);
        statsPanel.setVisible(false);

        uiStage.addActor(statsPanel);

        scoreboardTable = new Table();
        scoreboardTable.setWidth(BAR_WIDTH + 200);
        scoreboardTable.setPosition(Gdx.graphics.getWidth() - scoreboardTable.getWidth() - 20f, Gdx.graphics.getHeight() - 120f);
        scoreboardTable.align(Align.topRight);
        uiStage.addActor(scoreboardTable);
    }

    @Override
    protected RaceWorld getGameWorld() {
        return presenter.getWorld();
    }

    @Override
    protected boolean isGameFinished() {
        return presenter.isGameFinished();
    }

    @Override
    protected boolean isPlayerDead() {
        return presenter.isPlayerDead();
    }

    @Override
    protected void handleGameCompletion() {
        statsPanel.updateStats(gameManager.getWinner(), presenter.getCoins());
        statsPanel.setVisible(true);

        if (!onGameFinished) {
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

    @Override
    protected void updateGame(float delta) {
        presenter.update(delta);
    }

    @Override
    protected void updateUI(float delta) {
        try {
            distanceLabel.setText("Distance: " + presenter.getDistance() + "m");
            coinLabel.setText("Coins: " + presenter.getCoins());

            if (presenter.isPlayerDead()) {
                distanceLabel.setText("Race Failed - Horse Hit Obstacle!");
                coinLabel.setText("Returning to ranch...");
            }

            if (sprintWidget != null && presenter.getWorld() != null && presenter.getWorld().getPlayer() != null) {
                sprintWidget.act(delta);
            }
        } catch (Exception e) {
            // Handle errors silently
        }

        updateScoreboard(delta);
    }

    private void updateScoreboard(float delta) {
        // snapshot to avoid concurrent writes
        Map<String, PlayerInfoClient> snapshot = gameManager.getCurrentPlayers();
        if (snapshot == null || snapshot.isEmpty()) {
            scoreboardTable.clear();
            return;
        }

        List<PlayerInfoClient> players = new ArrayList<>(snapshot.values());
        players.sort((a, b) -> Integer.compare(b.getProgress(), a.getProgress()));

        // rebuild rows each frame
        scoreboardTable.clear();
        for (int i = 0; i < players.size(); i++) {
            PlayerInfoClient p = players.get(i);
            String name = p.getUsername();
            int distance = p.getProgress();

            // name and distance label
            Label nameLabel = UIFactory.createLabel((i + 1) + ". " + name);
            nameLabel.setColor(p.getUsername().equals(localUsername) ? Color.GOLD : Color.WHITE);
            Label distLabel = UIFactory.createLabel(distance + "m");

            // progress bar widget
            float progress = Math.min(FINISH_DISTANCE, (float) distance);
            ProgressBar bar = UIFactory.createProgressBar(0, FINISH_DISTANCE, 1, Color.GOLD);
            bar.setValue(progress);
            Table row = new Table();
            row.add(nameLabel).width(120).left().padRight(8f);
            row.add(bar).width(BAR_WIDTH).height(BAR_HEIGHT).padRight(8f);
            row.add(distLabel).width(60).right();

            scoreboardTable.add(row).padBottom(6f);
            scoreboardTable.row();
        }
    }

    private int getFinishDistance() {
        try {
            return presenter.getWorld().getMap().getLength();
        } catch (Exception ignored) {
            return FINISH_DISTANCE;
        }
    }

    @Override
    public void show() {
        super.show();

        float bw = 500f, bh = 30f;
        if (presenter.getWorld() != null && presenter.getWorld().getPlayer() != null) {
            sprintWidget = new SprintWidget(presenter.getWorld().getPlayer(), bw, bh);
            sprintWidget.setPosition((Gdx.graphics.getWidth() - bw) / 2f, 30f);
            uiStage.addActor(sprintWidget);
        }
    }
}
