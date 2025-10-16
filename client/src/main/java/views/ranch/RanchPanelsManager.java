package views.ranch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import views.common.ui.ListPanel;
import views.common.ui.Panel;

import java.util.ArrayList;
import java.util.List;

public class RanchPanelsManager {
    private final Stage stage;
    private final List<Panel> panels = new ArrayList<>();

    public RanchPanelsManager(Stage stage) {
        this.stage = stage;
    }

    public void registerPanel(Panel panel) {
        panels.add(panel);
        stage.addActor(panel);
        panel.hide();
    }

    public void showPanel(Panel panel) {
        hideAllPanels();
        panel.show(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        panel.toFront();

        if (panel instanceof ListPanel listPanel) {
            stage.setScrollFocus(listPanel.getScrollPane());
        }
    }

    public void hideAllPanels() {
        for (Panel panel : panels) {
            panel.hide();
        }
    }
}
