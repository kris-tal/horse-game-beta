package views.race.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import views.common.ui.UIFactory;

public class ProgressBarWidget extends Group {
    private final Label nameLabel;
    private final ProgressBar bar;
    private final Label valueLabel;

    private final float barWidth;
    private final float barHeight;

    public ProgressBarWidget(float barWidth, float barHeight, Skin skin, float initialPercent) {
        this.barWidth = barWidth;
        this.barHeight = barHeight;

        nameLabel = UIFactory.createLabel("");
        nameLabel.setAlignment(Align.left);

        bar = UIFactory.createProgressBar(0f, 1f, 0.01f, Color.WHITE);
        bar.setSize(barWidth, barHeight);
        bar.setValue(clamp01(initialPercent));

        valueLabel = UIFactory.createLabel("");
        valueLabel.setAlignment(Align.right);

        float nameWidth = 120f;
        float valueWidth = 60f;
        float totalWidth = nameWidth + 8f + barWidth + 8f + valueWidth;
        setSize(totalWidth, Math.max(barHeight, Math.max(nameLabel.getHeight(), valueLabel.getHeight())));

        nameLabel.setPosition(0f, (getHeight() - nameLabel.getHeight()) / 2f);
        bar.setPosition(nameWidth + 8f, (getHeight() - barHeight) / 2f);
        valueLabel.setPosition(nameWidth + 8f + barWidth + 8f, (getHeight() - valueLabel.getHeight()) / 2f);

        addActor(nameLabel);
        addActor(bar);
        addActor(valueLabel);
    }

    private float clamp01(float v) {
        return Math.max(0f, Math.min(1f, v));
    }

    public void setName(String name) {
        nameLabel.setText(name);
    }

    public void setProgress(float percent) {
        bar.setValue(clamp01(percent));
    }

    public void setValueText(String txt) {
        valueLabel.setText(txt);
    }

    public void setBarColor(Color color) {
        bar.setColor(color);
    }
}