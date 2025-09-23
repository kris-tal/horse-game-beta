package views.ranch;

import views.common.Panel;

public interface RanchView {
    void showPanel(Panel panel);

    void hideAllPanels();

    void navigateToRace();
}
