package services.lobby;

import data.race.GameMap;

public interface LobbyService {
    String createLobby();
    boolean joinLobby(String room);
    void startLobby(String room, GameMap map);
    void updatePosition(int progress);
    void closeConnection();
}
