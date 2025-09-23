package views.ranch.race;

import com.badlogic.gdx.Gdx;
import services.lobby.LobbyService;
import services.lobby.LobbyServiceImpl;
import views.common.MenuContext;

public class MockMenuListener implements RaceMenuPanel.RaceMenuListener {
    public LobbyService lobbyService = new LobbyServiceImpl();
    public String code;

    @Override
    public void onModeSelected(MenuContext.RaceMode raceMode) {
        if (raceMode.equals(MenuContext.RaceMode.SINGLEPLAYER)) {
            new Thread(() -> {
                code = lobbyService.createLobby();
                Gdx.app.log("Lobby", code);
            }).start();
        } else {
            lobbyService.joinLobby(code);
        }
    }

}
