package views.training.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import views.common.ui.Panel;
import views.common.ui.UIFactory;

public class TrainingStatsPanel extends Panel {
    private Label distanceLabel;
    private Label longestRunLabel;
    private Label coinsLabel;
    private Label recordLabel;

    public TrainingStatsPanel(float width, float height) {
        super(width, height);
        build();
    }

    @Override
    protected String getTitle() {
        return "TRAINING RESULTS";
    }

    @Override
    protected void addContent(float width, float height) {
        distanceLabel = UIFactory.createMediumLabel("Distance: 0m");
        longestRunLabel = UIFactory.createMediumLabel("Longest Run: 0m");
        coinsLabel = UIFactory.createMediumLabel("Coins: 0");
        recordLabel = UIFactory.createMediumLabel("New record!");
        recordLabel.setColor(Color.RED);
        recordLabel.setVisible(false);

        content.add(distanceLabel).padBottom(defaultPadding).row();
        content.add(longestRunLabel).padBottom(defaultPadding).row();
        content.add(coinsLabel).padBottom(defaultPadding).row();
        content.add(recordLabel).padBottom(defaultPadding).row();
    }

    public void updateStats(int distance, int longestRun, boolean isNewRecord) {
        distanceLabel.setText("Distance: " + distance + "m");
        longestRunLabel.setText("Longest Run: " + longestRun + "m");
        recordLabel.setVisible(isNewRecord);
    }

    public void updateStats(int distance, int longestRun, int coins, boolean isNewRecord) {
        distanceLabel.setText("Distance: " + distance + "m");
        longestRunLabel.setText("Longest Run: " + longestRun + "m");
        coinsLabel.setText("Coins: " + coins);
        recordLabel.setVisible(isNewRecord);
    }
}


