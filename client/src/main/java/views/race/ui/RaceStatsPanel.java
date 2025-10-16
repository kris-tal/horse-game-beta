package views.race.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import views.common.ui.Panel;
import views.common.ui.UIFactory;

public class RaceStatsPanel extends Panel {
    private Label distanceLabel;
    private Label coinsLabel;

    public RaceStatsPanel(float width, float height) {
        super(width, height);
        build();
    }

    @Override
    protected String getTitle() {
        return "RACE RESULTS";
    }

    @Override
    protected void addContent(float width, float height) {
        distanceLabel = UIFactory.createMediumLabel("distance: 0m");
        coinsLabel = UIFactory.createMediumLabel("coins: 0");

        content.add(distanceLabel).padBottom(defaultPadding).row();
        content.add(coinsLabel).padBottom(defaultPadding).row();
    }

    public void updateStats(int distance, int coins) {
        distanceLabel.setText("Distance: " + distance + "m");
        coinsLabel.setText("Coins: " + coins);
    }
}
