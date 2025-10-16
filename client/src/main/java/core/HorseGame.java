package core;

import com.badlogic.gdx.Game;
import services.managers.ConnectionManagerPort;
import services.managers.SessionManagerPort;
import services.media.AudioService;
import views.login.LoginScreen;

public class HorseGame extends Game {
    private ConnectionManagerPort connectionManager;
    private SessionManagerPort sessionManager;
    private AudioService audio;
    private LoginScreen loginScreen;

    public HorseGame(SessionManagerPort sessionManager, ConnectionManagerPort connectionManager, AudioService audio) {
        this.sessionManager = sessionManager;
        this.connectionManager = connectionManager;
        this.audio = audio;
        loginScreen = new LoginScreen(this, connectionManager, audio);
    }

    @Override
    public void create() {
        setScreen(loginScreen);
    }

    @Override
    public void dispose() {
        if (loginScreen != null) loginScreen.dispose();
    }
}
