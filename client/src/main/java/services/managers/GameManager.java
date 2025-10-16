package services.managers;

import data.lobby.LobbyInfo;
import data.lobby.PlayerInfoClient;
import data.race.PlayerProgressResponse;
import services.listeners.GameListener;

import java.util.HashMap;
import java.util.List;

/*
 *  Instantiating this class will set it as a listener to ConnectionManager
 *  You need two arguments, sessionManager and connectionManager
 */
public class GameManager implements GameListener {
    private final SessionManagerPort sessionManager;
    private final ConnectionManagerPort connectionManager;
    private HashMap<String, PlayerInfoClient> currentPlayers;
    private boolean gameInProgress = false;
    private String winner;

    public GameManager(SessionManagerPort sessionManager, ConnectionManagerPort connectionManager, LobbyInfo lobbyInfo) {
        this.sessionManager = sessionManager;
        this.connectionManager = connectionManager;
        this.connectionManager.setGameListener(this);
        this.currentPlayers = lobbyInfo.getPlayers();
    }

    public void setCurrentPlayers(HashMap<String, PlayerInfoClient> players) {
        currentPlayers = players;
    }

    public void startGame() {
        gameInProgress = true;
        winner = null;
    }

    public void endGame() {
        gameInProgress = false;
        currentPlayers = null;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public String getWinner() {
        return winner;
    }

    public HashMap<String, PlayerInfoClient> getCurrentPlayers() {
        return currentPlayers;
    }

    public void updatePosition(int newPosition) {
        connectionManager.emitProgressUpdate(sessionManager.getToken(), newPosition);
    }

    @Override
    public void onProgressUpdate(List<PlayerProgressResponse.PlayerProgress> playersProgress) {
        for (PlayerProgressResponse.PlayerProgress p : playersProgress) {
            if (currentPlayers.containsKey(p.getUsername())) {
                currentPlayers.get(p.getUsername()).setProgress(p.getProgress());
            }
        }
    }

    @Override
    public void onGameWon(String winnerUsername) {
        this.winner = winnerUsername;
        gameInProgress = false;
        System.out.println("Game won by: " + winnerUsername);
    }

    @Override
    public void onGameClosed() {
        gameInProgress = false;
        currentPlayers = null;
        winner = null;
        System.out.println("Game was closed/disconnected");
    }
}