// java
package views.training.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import views.common.ui.UIFactory;
import views.common.ui.UIStyles;

public class TrainingStatusBar extends Table {

    private final Label titleLabel;
    private final Label distanceLabel;
    private final Label trainingPointsLabel;
    private final Label longestRunLabel;
    private final Label coinsLabel;

    public TrainingStatusBar() {
        super();
        setBackground(new NinePatchDrawable(UIStyles.getDefaultBackgroundPatch()));
        pad(10).padLeft(12).padRight(12);
        align(Align.left);

        titleLabel = UIFactory.createMediumLabel("TRAINING");
        distanceLabel = UIFactory.createMediumLabel("distance: 0m");
        trainingPointsLabel = UIFactory.createMediumLabel("training points: 0/10000");
        longestRunLabel = UIFactory.createMediumLabel("longest Run: 0m");
        coinsLabel = UIFactory.createMediumLabel("coins: 0");

        add(titleLabel).left().padBottom(6);
        row();
        add(distanceLabel).left().padBottom(4);
        row();
        add(trainingPointsLabel).left().padBottom(4);
        row();
        add(longestRunLabel).left().padBottom(4);
        row();

        Table coinsRow = new Table();
        coinsRow.add(coinsLabel).left();
        add(coinsRow).left();

        setTransform(true);
        setOrigin(Align.topLeft);

        pack();
    }

    public void setDistance(int meters) {
        distanceLabel.setText("Distance: " + meters + "m");
        invalidateHierarchy();
        pack();
    }

    public void setTrainingPoints(int points) {
        trainingPointsLabel.setText("Training Points: " + points + "/10000");
        invalidateHierarchy();
        pack();
    }

    public void setLongestRun(int meters) {
        longestRunLabel.setText("Longest Run: " + meters + "m");
        invalidateHierarchy();
        pack();
    }

    public void setCoins(int coins) {
        coinsLabel.setText("Coins: " + coins);
        invalidateHierarchy();
        pack();
    }
}