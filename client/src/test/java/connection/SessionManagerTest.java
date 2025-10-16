package connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.managers.SessionManager;
import services.requests.ProtectedRequestsService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SessionManagerTest {

    @Mock
    private ProtectedRequestsService mockProtectedRequestsService;

    private SessionManager sessionManager;

    @BeforeEach
    void setUp() {
        sessionManager = new SessionManager(token -> mockProtectedRequestsService);
    }

    @Test
    void testInitialState() {
        assertFalse(sessionManager.isLoggedIn());
        assertNull(sessionManager.getToken());

        assertThrows(IllegalStateException.class,
                () -> sessionManager.getProtectedRequestsService());
    }

    @Test
    void testStartSessionWithValidToken() {
        String token = "validToken123";

        sessionManager.startSession(token);

        assertTrue(sessionManager.isLoggedIn());
        assertEquals(token, sessionManager.getToken());
        assertEquals(mockProtectedRequestsService, sessionManager.getProtectedRequestsService());
    }

    @Test
    void testStartSessionWithNullToken() {
        assertThrows(IllegalArgumentException.class,
                () -> sessionManager.startSession(null));
    }

    @Test
    void testStartSessionWithBlankToken() {
        assertThrows(IllegalArgumentException.class,
                () -> sessionManager.startSession(""));

        assertThrows(IllegalArgumentException.class,
                () -> sessionManager.startSession("   "));
    }

    @Test
    void testEndSession() {
        sessionManager.startSession("testToken");
        assertTrue(sessionManager.isLoggedIn());
        sessionManager.endSession();

        assertFalse(sessionManager.isLoggedIn());
        assertNull(sessionManager.getToken());

        assertThrows(IllegalStateException.class,
                () -> sessionManager.getProtectedRequestsService());
    }

    @Test
    void testMultipleSessionStarts() {
        sessionManager.startSession("token1");
        assertEquals("token1", sessionManager.getToken());
        sessionManager.startSession("token2");
        assertEquals("token2", sessionManager.getToken());
        assertTrue(sessionManager.isLoggedIn());
    }

    @Test
    void testConstructorWithNullFactory() {
        assertThrows(NullPointerException.class,
                () -> new SessionManager(null));
    }

    @Test
    void testDefaultConstructor() {
        SessionManager defaultSessionManager = new SessionManager();

        assertFalse(defaultSessionManager.isLoggedIn());
        assertNull(defaultSessionManager.getToken());
    }
}
