package services.requests;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static services.auth.AuthServiceImpl.BASE_URL;
import static services.auth.AuthServiceImpl.REQUEST_TIMEOUT;

public class ProtectedRequestsServiceImpl implements ProtectedRequestsService {
    private final HttpClient client;
    private final String session;
    public ProtectedRequestsServiceImpl(String session) {
        this.session = session;
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(REQUEST_TIMEOUT)
                .build();
    }
    @Override
    public HttpRequest.Builder ProtectedGet(String path) {
        if(session == null) {
            throw new RuntimeException("User not logged in!");
        }
        return  HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + session)
                .timeout(REQUEST_TIMEOUT)
                .GET();
    }

    @Override
    public HttpRequest.Builder ProtectedPost(String path, HttpRequest.BodyPublisher body) {
        if(session == null) {
            throw new RuntimeException("User not logged in!");
        }
        return  HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + session)
                .timeout(REQUEST_TIMEOUT)
                .POST(body);
    }

    @Override
    public HttpClient GetHttpClient() {
        return client;
    }

    @Override
    public String GetSessionToken() {
        return session;
    }
}
