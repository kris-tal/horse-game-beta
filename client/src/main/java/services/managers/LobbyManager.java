package services.managers;

import com.badlogic.gdx.Gdx;
import data.lobby.PlayerInfoClient;
import services.lobby.LobbyService;
import services.lobby.LobbyServiceImpl;

import java.util.HashMap;
import java.util.List;

public class LobbyManager {
    private static LobbyManager instance;
    private final LobbyService service;
    private LobbyInfo currentLobby;
    private LobbyStateListener stateListener;
    private LobbyConnectionListener connectionListener;

    /* Interface responsible for callback from server responses,
     Callback is designed to communicate with WaitingLobbyPanel*/
    public interface LobbyStateListener {
        void onLobbyInitialized();
        void onPlayersUpdated();
        void onLobbyStarted();
        void onLobbyClosed();
    }

    /* Interface responsible for callback from server responses,
     Callback is designed to communicate with JoinLobbyPanel*/
    public interface LobbyConnectionListener {
        void onLobbyJoined(boolean success);
    }

    private LobbyManager() {
        service = new LobbyServiceImpl();
    }

    public static synchronized LobbyManager getInstance() {
        if (instance == null) {
            instance = new LobbyManager();
        }
        return instance;
    }

    public void createLobby() {
        leaveLobby();
        String code = service.createLobby();
        if (code != null) {
            currentLobby = new LobbyInfo(code);
            Gdx.app.log("Lobby", "Created lobby! " + code);
            service.joinLobby(code);
        }
    }

    public void joinLobby(String lobbyCode) {
        if (currentLobby != null) {
            service.closeConnection();
        }

        // Create lobby info immediately (optimistic)
        // This will be validated by the async connection result
        currentLobby = new LobbyInfo(lobbyCode);

        boolean success = service.joinLobby(lobbyCode);
        if (!success) {
            // If immediate failure, clear the lobby
            currentLobby = null;
            if (connectionListener != null) {
                connectionListener.onLobbyJoined(false);
            }
        }
        // If success, wait for async callback to confirm
    }

    public boolean startLobby() {
        if (currentLobby == null) {
            Gdx.app.log("Lobby", "No current lobby to start!");
            return false;
        }

        if (service.startLobby()) {
            Gdx.app.log("Lobby", "Started lobby successfully");
            return true;
        } else {
            Gdx.app.log("Lobby", "Failed to start lobby");
            return false;
        }
    }

    public LobbyInfo getCurrentLobby() {
        return currentLobby;
    }

    public void leaveLobby() {
        if (currentLobby != null) {
            currentLobby = null;
        }
        service.closeConnection();
    }

    public void updatePlayers(HashMap<String, PlayerInfoClient> newPlayers) {
        if (currentLobby != null) {
            currentLobby.setPlayers(newPlayers);
            if (stateListener != null) {
                stateListener.onPlayersUpdated();
            }
        }
    }

    public void onLobbyStarted() {
        if (stateListener != null) {
            stateListener.onLobbyStarted();
        }
    }

    public void onLobbyClosed() {
        // FIXED: Clear lobby when it's closed
        currentLobby = null;
        if (stateListener != null) {
            stateListener.onLobbyClosed();
        }
    }

    public void setStateListener(LobbyStateListener listener) {
        this.stateListener = listener;
    }

    public void setConnectionListener(LobbyConnectionListener listener) {
        this.connectionListener = listener;
    }

    public boolean isCurrentUserCreator() {
        if (currentLobby == null) return false;

        String username = ((LobbyServiceImpl) service).getCurrentUserUsername();
        if (username == null) return false;

        PlayerInfoClient player = currentLobby.getPlayer(username);
        if (player == null) return false;

        return player.isCreator();
    }

    public void onLobbyJoined(boolean success) {
        if (connectionListener != null) {
            connectionListener.onLobbyJoined(success);
        }

        if (success) {
            if (stateListener != null) {
                stateListener.onLobbyInitialized();
            }
            Gdx.app.log("Lobby", "Successfully joined lobby: " +
                    (currentLobby != null ? currentLobby.getLobbyCode() : "unknown"));
        } else {
            Gdx.app.error("Lobby", "Failed to join lobby - clearing lobby info");
            currentLobby = null;
        }
    }

    public static class LobbyInfo {
        private final String lobbyCode;
        private HashMap<String, PlayerInfoClient> players = new HashMap<>();

        public LobbyInfo(String lobbyCode) {
            this.lobbyCode = lobbyCode;
        }

        public String getLobbyCode() {
            return lobbyCode;
        }

        public HashMap<String, PlayerInfoClient> getPlayers() {
            return players;
        }

        public void setPlayers(HashMap<String, PlayerInfoClient> players) {
            this.players = players;
        }

        public int getPlayerCount() {
            return players.size();
        }

        public PlayerInfoClient getPlayer(String username) {
            return players.get(username);
        }
    }
}