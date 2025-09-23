package services.managers;

import services.requests.ProtectedRequestsService;
import services.requests.ProtectedRequestsServiceImpl;

public class SessionManager {
    private static SessionManager instance;
    private String token;
    private ProtectedRequestsService protectedRequestsService;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void startSession(String token) {
        this.token = token;
        this.protectedRequestsService = new ProtectedRequestsServiceImpl(token);
    }

    public void endSession() {
        this.token = null;
        this.protectedRequestsService = null;
    }

    public String getToken() {
        return token;
    }

    public ProtectedRequestsService getProtectedRequestsService() {
        return protectedRequestsService;
    }

    public boolean isLoggedIn() {
        return token != null;
    }


}

