package views.race.menu;

import services.lobby.LobbyService;
import views.common.context.MenuContext;

public class MockMenuListener implements RaceMenuPanel.RaceMenuListener {
    public LobbyService lobbyService = null; //new LobbyServiceImpl();
    public String code;

    @Override
    public void onModeSelected(MenuContext.RaceMode raceMode) {
        if (raceMode.equals(MenuContext.RaceMode.SINGLEPLAYER)) {
            new Thread(() -> code = lobbyService.createLobby()).start();
        } else {
            lobbyService.joinLobby(code);
        }
    }

}
