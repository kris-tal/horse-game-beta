package views.ranch;

import views.common.context.MenuContext;
import views.common.ui.Panel;
import views.race.menu.JoinLobbyPanel;
import views.race.menu.WaitingLobbyPanel;


public class RanchPresenter {
    private final RanchView view;
    private final MenuContext menuContext;

    public RanchPresenter(RanchView view, MenuContext menuContext) {
        this.view = view;
        this.menuContext = menuContext;
    }

    public void onRaceButtonClicked(Panel panel) {
        view.hideAllPanels();
        view.showPanel(panel);
    }

    public void onShopButtonClicked(Panel panel) {
        view.hideAllPanels();
        view.showPanel(panel);
    }

    public void onSingleplayerClicked() {
        menuContext.setRaceMode(MenuContext.RaceMode.SINGLEPLAYER);
        view.hideAllPanels();
        view.navigateToRace("");
    }

    public void onMultiplayerClicked(Panel panel) {
        menuContext.setRaceMode(MenuContext.RaceMode.MULTIPLAYER);
        view.hideAllPanels();
        view.showPanel(panel);
    }

    public void onCreateLobbyClicked(WaitingLobbyPanel panel) {
        menuContext.setLobbyMode(MenuContext.LobbyMode.CREATE);
        view.hideAllPanels();
        panel.createLobby();
        view.showPanel(panel);
    }

    public void onJoinLobbyClicked(JoinLobbyPanel panel) {
        menuContext.setLobbyMode(MenuContext.LobbyMode.JOIN);
        view.hideAllPanels();
        view.showPanel(panel);
    }

    public void onExitPressed() {
        view.showExitConfirmation();
    }
}
