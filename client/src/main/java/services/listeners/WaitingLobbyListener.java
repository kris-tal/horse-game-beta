package services.listeners;

public interface WaitingLobbyListener {
    void onLobbyStarted(String data);
    void onLeaveLobby();
}