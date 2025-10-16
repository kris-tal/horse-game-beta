package lobby;

import data.lobby.LobbyInfo;
import data.lobby.PlayerInfoClient;
import data.race.PlayerProgressResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.managers.ConnectionManagerPort;
import services.managers.GameManager;
import services.managers.LobbyManager;
import services.managers.SessionManagerPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LobbyGameIntegrationTest {

    @Mock
    private SessionManagerPort mockSessionManager;

    @Mock
    private ConnectionManagerPort mockConnectionManager;

    @Mock
    private LobbyManager.LobbyStateListener mockStateListener;


    private LobbyManager lobbyManager;
    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        lobbyManager = new LobbyManager(mockSessionManager, mockConnectionManager);
        lobbyManager.setStateListener(mockStateListener);
        lobbyManager.setCurrentLobby(new LobbyInfo("ABC123"));
    }

    @Test
    void testLobbyToGameTransition() {
        HashMap<String, PlayerInfoClient> players = new HashMap<>();
        PlayerInfoClient player1 = new PlayerInfoClient();
        player1.setUsername("player1");
        PlayerInfoClient player2 = new PlayerInfoClient();
        player2.setUsername("player2");
        players.put("player1", player1);
        players.put("player2", player2);

        lobbyManager.onLobbyJoined(true);
        lobbyManager.onPlayerListChanged(players);
        lobbyManager.onLobbyStarted("");

        verify(mockStateListener).onPlayersUpdated();
        verify(mockStateListener).onLobbyStarted("");

        LobbyInfo lobbyInfo = lobbyManager.getCurrentLobby();
        if (lobbyInfo != null) {
            gameManager = new GameManager(mockSessionManager, mockConnectionManager, lobbyInfo);

            verify(mockConnectionManager, times(1)).setGameListener(any());
        }
    }

    @Test
    void testGameProgressUpdateFlow() {

        HashMap<String, PlayerInfoClient> players = new HashMap<>();
        PlayerInfoClient player1 = new PlayerInfoClient();
        player1.setUsername("player1");
        player1.setProgress(0);
        players.put("player1", player1);

        LobbyInfo mockLobbyInfo = new LobbyInfo("TEST123");
        mockLobbyInfo.setPlayers(players);

        gameManager = new GameManager(mockSessionManager, mockConnectionManager, mockLobbyInfo);
        gameManager.startGame();

        List<PlayerProgressResponse.PlayerProgress> progressList = new ArrayList<>();
        PlayerProgressResponse.PlayerProgress progress = new PlayerProgressResponse.PlayerProgress();
        progress.setUsername("player1");
        progress.setProgress(75);
        progressList.add(progress);

        gameManager.onProgressUpdate(progressList);

        assertEquals(75, gameManager.getCurrentPlayers().get("player1").getProgress());

        gameManager.onGameWon("player1");

        assertEquals("player1", gameManager.getWinner());
        assertFalse(gameManager.isGameInProgress());
    }

    @Test
    void testConnectionLossScenario() {
        lobbyManager.onLobbyJoined(true);

        lobbyManager.onLobbyClosed();

        verify(mockStateListener).onLobbyClosed();
        assertNull(lobbyManager.getCurrentLobby());

        HashMap<String, PlayerInfoClient> players = new HashMap<>();
        LobbyInfo mockLobbyInfo = new LobbyInfo("TEST123");
        mockLobbyInfo.setPlayers(players);

        gameManager = new GameManager(mockSessionManager, mockConnectionManager, mockLobbyInfo);
        gameManager.startGame();

        gameManager.onGameClosed();

        assertFalse(gameManager.isGameInProgress());
        assertNull(gameManager.getCurrentPlayers());
        assertNull(gameManager.getWinner());
    }
}
