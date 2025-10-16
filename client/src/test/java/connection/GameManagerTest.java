package connection;

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
import services.managers.SessionManagerPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameManagerTest {
    @Mock
    private SessionManagerPort mockSessionManager;
    @Mock
    private ConnectionManagerPort mockConnectionManager;
    @Mock
    private LobbyInfo mockLobbyInfo;
    private GameManager gameManager;
    private HashMap<String, PlayerInfoClient> testPlayers;
    @BeforeEach
    void setUp() {
        testPlayers = new HashMap<>();
        PlayerInfoClient player1 = new PlayerInfoClient();
        player1.setUsername("player1");
        player1.setProgress(0);
        PlayerInfoClient player2 = new PlayerInfoClient();
        player2.setUsername("player2");
        player2.setProgress(0);

        testPlayers.put("player1", player1);
        testPlayers.put("player2", player2);

        when(mockLobbyInfo.getPlayers()).thenReturn(testPlayers);

        gameManager = new GameManager(mockSessionManager, mockConnectionManager, mockLobbyInfo);
    }

    @Test
    void testGameManagerInitialization() {
        assertNotNull(gameManager);
        assertFalse(gameManager.isGameInProgress());
        assertNull(gameManager.getWinner());
        assertEquals(testPlayers, gameManager.getCurrentPlayers());
        verify(mockConnectionManager).setGameListener(gameManager);
    }

    @Test
    void testStartGame() {
        gameManager.startGame();

        assertTrue(gameManager.isGameInProgress());
        assertNull(gameManager.getWinner());
    }

    @Test
    void testEndGame() {
        gameManager.startGame();
        gameManager.endGame();

        assertFalse(gameManager.isGameInProgress());
        assertNull(gameManager.getCurrentPlayers());
    }

    @Test
    void testSetCurrentPlayers() {
        HashMap<String, PlayerInfoClient> newPlayers = new HashMap<>();
        PlayerInfoClient newPlayer = new PlayerInfoClient();
        newPlayer.setUsername("newPlayer");
        newPlayers.put("newPlayer", newPlayer);

        gameManager.setCurrentPlayers(newPlayers);

        assertEquals(newPlayers, gameManager.getCurrentPlayers());
    }

    @Test
    void testUpdatePosition() {
        int newPosition = 75;

        when(mockSessionManager.getToken()).thenReturn("testToken");
        gameManager.updatePosition(newPosition);

        verify(mockConnectionManager).emitProgressUpdate("testToken", newPosition);
    }

    @Test
    void testOnProgressUpdate() {
        List<PlayerProgressResponse.PlayerProgress> progressList = new ArrayList<>();

        PlayerProgressResponse.PlayerProgress progress1 = new PlayerProgressResponse.PlayerProgress();
        progress1.setUsername("player1");
        progress1.setProgress(25);

        PlayerProgressResponse.PlayerProgress progress2 = new PlayerProgressResponse.PlayerProgress();
        progress2.setUsername("player2");
        progress2.setProgress(50);

        progressList.add(progress1);
        progressList.add(progress2);

        gameManager.onProgressUpdate(progressList);

        assertEquals(25, gameManager.getCurrentPlayers().get("player1").getProgress());
        assertEquals(50, gameManager.getCurrentPlayers().get("player2").getProgress());
    }

    @Test
    void testOnProgressUpdateWithUnknownPlayer() {
        List<PlayerProgressResponse.PlayerProgress> progressList = new ArrayList<>();

        PlayerProgressResponse.PlayerProgress progress = new PlayerProgressResponse.PlayerProgress();
        progress.setUsername("unknownPlayer");
        progress.setProgress(25);
        progressList.add(progress);

        assertDoesNotThrow(() -> gameManager.onProgressUpdate(progressList));
    }

    @Test
    void testOnGameClosed() {
        gameManager.startGame();

        gameManager.onGameClosed();

        assertFalse(gameManager.isGameInProgress());
        assertNull(gameManager.getCurrentPlayers());
        assertNull(gameManager.getWinner());
    }
}
