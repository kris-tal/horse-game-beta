package connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import services.listeners.GameListener;
import services.listeners.LobbyListener;
import services.managers.*;

@ExtendWith(MockitoExtension.class)
public class ConnectionManagerTest {
    @Mock
    private LobbyListener mockLobbyListener;
    @Mock
    private GameListener mockGameListener;

    private ConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        connectionManager = new ConnectionManager();
        connectionManager.setLobbyListener(mockLobbyListener);
        connectionManager.setGameListener(mockGameListener);
    }

    @Test
    void testSetLobbyListener() {
        LobbyListener newListener = mock(LobbyListener.class);
        connectionManager.setLobbyListener(newListener);

        assertDoesNotThrow(() -> connectionManager.setLobbyListener(newListener));
    }

    @Test
    void testSetGameListener() {
        GameListener newListener = mock(GameListener.class);
        connectionManager.setGameListener(newListener);

        assertDoesNotThrow(() -> connectionManager.setGameListener(newListener));
    }

    @Test
    void testClearListeners() {
        connectionManager.clearListeners();

        assertDoesNotThrow(() -> connectionManager.clearListeners());
    }

    @Test
    void testInitialConnectionState() {
        assertFalse(connectionManager.isConnected());
        assertNull(connectionManager.getCurrentRoom());
        assertNull(connectionManager.getSocket());
    }

    @Test
    void testConnectToRoomWithInvalidTokenNotThrows() {
        boolean result = connectionManager.connectToRoom("", "");

        assertTrue(result);
        assertFalse(connectionManager.isConnected());
    }

    @Test
    void testEmitProgressUpdateWithoutConnection() {
        assertDoesNotThrow(() ->
            connectionManager.emitProgressUpdate("testToken", 50)
        );
    }

    @Test
    void testCloseConnection() {
        assertDoesNotThrow(() -> connectionManager.closeConnection());

        assertFalse(connectionManager.isConnected());
        assertNull(connectionManager.getCurrentRoom());
        assertNull(connectionManager.getSocket());
    }
}


