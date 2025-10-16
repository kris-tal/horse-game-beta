package views.race.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import views.common.ui.ListPanel;

public class HorseSelectionPanel extends ListPanel {
    public HorseSelectionPanel(float width, float height) {
        super(width, height);
    }

    @Override
    protected String getTitle() {
        return "select your horse";
    }

    @Override
    protected void addContent(float width, float height) {
        super.addContent(width, height);

    }

    @Override
    protected void populateRow(Table row, Object item) {

    }
}
