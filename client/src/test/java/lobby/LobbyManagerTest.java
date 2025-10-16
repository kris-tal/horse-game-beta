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
import services.managers.LobbyManager;
import services.managers.SessionManagerPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LobbyManagerTest {

    @Mock
    private SessionManagerPort mockSessionManager;

    @Mock
    private ConnectionManagerPort mockConnectionManager;

    @Mock
    private LobbyManager.LobbyStateListener mockStateListener;

    @Mock
    private LobbyManager.LobbyConnectionListener mockConnectionListener;

    private LobbyManager lobbyManager;

    @BeforeEach
    void setUp() {
        lobbyManager = new LobbyManager(mockSessionManager, mockConnectionManager);
        lobbyManager.setStateListener(mockStateListener);
        lobbyManager.setConnectionListener(mockConnectionListener);
    }

    @Test
    void testLobbyManagerInitialization() {
        assertNotNull(lobbyManager);
        assertNull(lobbyManager.getCurrentLobby());


        verify(mockConnectionManager).setLobbyListener(lobbyManager);
    }


    @Test
    void testCreateLobbyWhenAlreadyInLobby() {
        lobbyManager.onLobbyJoined(true);
        LobbyInfo testLobby = new LobbyInfo("TEST123");
        lobbyManager.setCurrentLobby(testLobby);

        assertDoesNotThrow(() -> lobbyManager.createLobby());
    }

    @Test
    void testJoinLobby() {
        String lobbyCode = "TEST123";

        assertDoesNotThrow(() -> lobbyManager.joinLobby(lobbyCode));
    }

    @Test
    void testJoinLobbyWhenAlreadyInLobby() {
        LobbyInfo testLobby = new LobbyInfo("TEST123");
        lobbyManager.setCurrentLobby(testLobby);

        lobbyManager.joinLobby("ANOTHER123");

        verify(mockConnectionManager, times(1)).closeConnection();
    }

    @Test
    void testStartLobbyWithoutCurrentLobby() {

        assertDoesNotThrow(() -> lobbyManager.startLobby());
    }

    @Test
    void testLeaveLobby() {
        lobbyManager.leaveLobby();

        assertNull(lobbyManager.getCurrentLobby());
        verify(mockConnectionManager).closeConnection();
    }

    @Test
    void testOnPlayerListChanged() {
        HashMap<String, PlayerInfoClient> players = new HashMap<>();
        PlayerInfoClient player = new PlayerInfoClient();
        player.setUsername("testPlayer");
        players.put("testPlayer", player);

        LobbyInfo testLobby = new LobbyInfo("TEST123");
        lobbyManager.setCurrentLobby(testLobby);

        lobbyManager.onPlayerListChanged(players);

        verify(mockStateListener).onPlayersUpdated();
    }

    @Test
    void testOnPlayerListChangedWithoutLobby() {
        HashMap<String, PlayerInfoClient> players = new HashMap<>();


        assertDoesNotThrow(() -> lobbyManager.onPlayerListChanged(players));


        verify(mockStateListener, never()).onPlayersUpdated();
    }

    @Test
    void testOnLobbyStarted() {
        lobbyManager.onLobbyStarted("");

        verify(mockStateListener).onLobbyStarted("");
    }

    @Test
    void testOnLobbyClosed() {
        lobbyManager.onLobbyClosed();

        assertNull(lobbyManager.getCurrentLobby());
        verify(mockStateListener).onLobbyClosed();
    }

    @Test
    void testOnLobbyJoinedSuccess() {
        lobbyManager.onLobbyJoined(true);

        verify(mockConnectionListener).onLobbyJoined(true);
        verify(mockStateListener).onLobbyInitialized();
    }

    @Test
    void testOnLobbyJoinedFailure() {
        lobbyManager.onLobbyJoined(false);

        verify(mockConnectionListener).onLobbyJoined(false);
        assertNull(lobbyManager.getCurrentLobby());
    }

    @Test
    void testOnLobbyInitialized() {
        lobbyManager.onLobbyInitialized();

        verify(mockStateListener).onLobbyInitialized();
    }

    @Test
    void testUpdateProgressWithNullLobby() {
        PlayerProgressResponse progress = mock(PlayerProgressResponse.class);


        assertDoesNotThrow(() -> lobbyManager.updateProgress(progress));


        verify(mockStateListener, never()).onPlayersUpdated();
    }

    @Test
    void testUpdateProgressWithValidData() {
        HashMap<String, PlayerInfoClient> players = new HashMap<>();
        PlayerInfoClient player = new PlayerInfoClient();
        player.setUsername("testPlayer");
        player.setProgress(0);
        players.put("testPlayer", player);

        LobbyInfo testLobby = new LobbyInfo("TEST123");
        testLobby.setPlayers(players);


        PlayerProgressResponse progress = new PlayerProgressResponse();
        List<PlayerProgressResponse.PlayerProgress> progressList = new ArrayList<>();

        PlayerProgressResponse.PlayerProgress playerProgress =
                new PlayerProgressResponse.PlayerProgress();
        playerProgress.setUsername("testPlayer");
        playerProgress.setProgress(50);
        progressList.add(playerProgress);
        progress.setDistances(progressList);
        lobbyManager.setCurrentLobby(testLobby);

        assertDoesNotThrow(() -> lobbyManager.updateProgress(progress));
        assertEquals(lobbyManager.getCurrentLobby().getPlayer("testPlayer").getProgress(), playerProgress.getProgress());
    }
}
