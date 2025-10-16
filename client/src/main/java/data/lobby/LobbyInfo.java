package data.lobby;

import java.util.HashMap;

public class LobbyInfo {
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
