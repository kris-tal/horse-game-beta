package services.managers;

import services.requests.ProtectedRequestsService;

public interface SessionManagerPort {
    void startSession(String token);

    void endSession();

    boolean isLoggedIn();

    String getToken();

    ProtectedRequestsService getProtectedRequestsService();
}