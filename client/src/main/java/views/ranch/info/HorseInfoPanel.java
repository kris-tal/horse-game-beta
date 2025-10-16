// java
package views.ranch.info;

import animations.IdleHorseActor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import data.horse.HorseData;
import data.race.EquippedHorseService;
import views.common.ui.Panel;
import views.common.ui.UIFactory;

public class HorseInfoPanel extends Panel {
    private final HorseData horseData;
    private final EquippedHorseService equippedHorseService;

    public HorseInfoPanel(HorseData horseData, EquippedHorseService equippedHorseService) {
        super(400, 600);
        this.horseData = horseData;
        this.equippedHorseService = equippedHorseService;
        super.build();
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected void addContent(float width, float height) {
        //equippedHorseService.ensureDefaultEquipped(horseData);

        Label nameLabel = UIFactory.createMediumLabel(horseData.getHorseType().getName());
        nameLabel.setWrap(true);
        nameLabel.setAlignment(Align.center);
        content.add(nameLabel).width(width - 2 * smallPadding).row();

        IdleHorseActor idleHorseActor = new IdleHorseActor(horseData.getHorseType(), 3);
        content.add(idleHorseActor).padTop(defaultPadding).row();

        Label levelLabel = UIFactory.createMediumLabel("sprint level: " + horseData.getLevel());
        content.add(levelLabel).padTop(defaultPadding).row();

        Label trainingPointsLabel = UIFactory.createMediumLabel("training level: " + horseData.getTrainingPoints());
        content.add(trainingPointsLabel).padTop(defaultPadding).row();

        final TextButton equipButton = UIFactory.createLargeTextButton("equip");

        if (equippedHorseService.isEquipped(horseData)) {
            equipButton.setText("equipped");
            equipButton.setDisabled(true);
            equipButton.setColor(0.7f, 0.7f, 0.7f, 1f);
        } else {
            equipButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    equippedHorseService.equipHorse(horseData);
                    equipButton.setText("equipped");
                    equipButton.setDisabled(true);
                    equipButton.setColor(0.7f, 0.7f, 0.7f, 1f);
                }
            });
        }

        content.add(equipButton).padTop(bigPadding).row();
    }
}