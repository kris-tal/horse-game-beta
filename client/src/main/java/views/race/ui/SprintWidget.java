package views.race.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import race.Player;
import views.common.ui.UIFactory;

public class SprintWidget extends Group {

    private final Player player;
    private final ProgressBar sprintBar;
    private final Label pressSpaceLabel;

    private boolean waitingForSpace = false;

    public SprintWidget(Player player, float width, float height) {
        this.player = player;

        sprintBar = UIFactory.createProgressBar(0f, player.getSprintDuration(), 0.01f, player.getHorseType().getColor());
        sprintBar.setSize(width, height);

        pressSpaceLabel = UIFactory.createLabel("PRESS SPACE");
        pressSpaceLabel.setVisible(false);
        pressSpaceLabel.setPosition(width / 2f - pressSpaceLabel.getWidth() / 2f,
                height / 2f - pressSpaceLabel.getHeight() / 2f);

        addActor(sprintBar);
        addActor(pressSpaceLabel);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!waitingForSpace) {
            if (player.canSprint()) {
                sprintBar.setValue(player.getSprintDuration()); 
                pressSpaceLabel.setVisible(true);
            } else if (player.isSprinting()) {
                sprintBar.setValue(player.getSprintTimeLeft());
                pressSpaceLabel.setVisible(false);
            } else if (player.isInSprintCooldown()) {
                sprintBar.setValue(player.getSprintDuration() * (1f - player.getSprintCooldownProgress()));
                pressSpaceLabel.setVisible(false);
            }
        } else {
            sprintBar.setValue(player.getSprintTimeLeft());
        }

        if (pressSpaceLabel.isVisible() && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.startSprint();
            waitingForSpace = true;
            pressSpaceLabel.setVisible(false);
        }

        if (waitingForSpace && !player.isSprinting()) {
            waitingForSpace = false;
        }
    }
}
