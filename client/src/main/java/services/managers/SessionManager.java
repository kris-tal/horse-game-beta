package services.managers;

import services.requests.ProtectedRequestsService;
import services.requests.ProtectedRequestsServiceImpl;

import java.util.Objects;
import java.util.function.Function;

public final class SessionManager implements SessionManagerPort {
    private final Function<String, ProtectedRequestsService> prsFactory;

    private volatile String token;
    private volatile ProtectedRequestsService protectedRequestsService;

    public SessionManager() {
        this(ProtectedRequestsServiceImpl::new);
    }

    public SessionManager(Function<String, ProtectedRequestsService> prsFactory) {
        this.prsFactory = Objects.requireNonNull(prsFactory, "prsFactory");
    }

    @Override
    public synchronized void startSession(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("token must not be null/blank");
        }
        this.token = token;
        this.protectedRequestsService = prsFactory.apply(token);
    }

    @Override
    public synchronized void endSession() {
        this.token = null;
        this.protectedRequestsService = null;
    }

    @Override
    public boolean isLoggedIn() {
        return token != null;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public ProtectedRequestsService getProtectedRequestsService() {
        if (protectedRequestsService == null) {
            throw new IllegalStateException("User not logged in");
        }
        return protectedRequestsService;
    }
}