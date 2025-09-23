package services.lobby;

public interface LobbyService {
    public String createLobby();
    public boolean joinLobby(String room);
    public boolean startLobby();
    public void updatePosition(int progress);
    public void closeConnection();
}
