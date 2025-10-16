package views.race.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import views.common.ui.Panel;
import views.common.ui.UIFactory;

public class RaceMultiplayerStatsPanel extends Panel {
    private Label winnerLabel;
    private Label coinsLabel;

    public RaceMultiplayerStatsPanel(float width, float height) {
        super(width, height);
        build();
    }

    @Override
    protected String getTitle() {
        return "RACE FINISHED";
    }

    @Override
    protected void addContent(float width, float height) {
        winnerLabel = UIFactory.createMediumLabel("");
        coinsLabel = UIFactory.createMediumLabel("");

        content.add(winnerLabel).padBottom(defaultPadding).row();
        content.add(coinsLabel).padBottom(defaultPadding).row();
    }

    public void updateStats(String winnerUsername, int coins) {
        winnerLabel.setText(winnerUsername + " won the race!");
        coinsLabel.setText("Coins: " + coins);
    }
}
