package services.managers;

import io.socket.client.Socket;
import services.listeners.GameListener;
import services.listeners.LobbyListener;

public interface ConnectionManagerPort {
    void setLobbyListener(LobbyListener listener);
    void setGameListener(GameListener listener);
    void clearListeners();
    boolean connectToRoom(String room, String token);
    void emitProgressUpdate(String token, int progress);
    void closeConnection();
    boolean isConnected();
    String getCurrentRoom();
    Socket getSocket();
}
