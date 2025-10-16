package services.auth;

import com.google.gson.Gson;
import data.auth.AuthCode;
import data.auth.AuthResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

public class AuthServiceImpl implements AuthService {

    private final HttpClient client;
    private String authToken;

    public static final String BASE_URL = System.getProperty(
            "backend.baseUrl",
            System.getenv().getOrDefault("BACKEND_BASE_URL", "http://127.0.0.1:4000")
    );
    public static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);


    private final Gson gson;

    public AuthServiceImpl() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(REQUEST_TIMEOUT)
                .build();
        this.authToken = null;
        this.gson = new Gson();
    }

    @Override
    public AuthResult login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return new AuthResult(AuthCode.INVALID_INPUT, null);
        }

        try {
            String requestBody = gson.toJson(new LoginRequest(username, password));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/login"))
                    .header("Content-Type", "application/json")
                    .timeout(REQUEST_TIMEOUT)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                var jsonObject = gson.fromJson(response.body(), Map.class);
                if (jsonObject.containsKey("token")) {
                    authToken = (String) jsonObject.get("token");
                    return new AuthResult(AuthCode.SUCCESS, authToken);
                } else {
                    return new AuthResult(AuthCode.SERVER_ERROR, null);
                }
            } else if (response.statusCode() == 401) {
                return new AuthResult(AuthCode.WRONG_PASSWORD, null);
            } else if (response.statusCode() == 404) {
                return new AuthResult(AuthCode.USER_NOT_FOUND, null);
            } else {
                return new AuthResult(AuthCode.SERVER_ERROR, null);
            }

        } catch (HttpTimeoutException e) {
            return new AuthResult(AuthCode.TIMEOUT, null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new AuthResult(AuthCode.INTERRUPTED, null);
        } catch (Exception e) {
            return new AuthResult(AuthCode.SERVER_ERROR, null);
        }
    }

    @Override
    public AuthResult register(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return new AuthResult(AuthCode.INVALID_INPUT, null);
        }

        try {
            String requestBody = gson.toJson(new LoginRequest(username, password));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/signup"))
                    .header("Content-Type", "application/json")
                    .timeout(REQUEST_TIMEOUT)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 201 ->
                        new AuthResult(AuthCode.SUCCESS, (String) gson.fromJson(response.body(), Map.class).get("token"));
                case 409 -> new AuthResult(AuthCode.USERNAME_TAKEN, null);
                case 400 -> new AuthResult(AuthCode.INVALID_INPUT, null);
                default -> new AuthResult(AuthCode.SERVER_ERROR, null);
            };

        } catch (HttpTimeoutException e) {
            return new AuthResult(AuthCode.TIMEOUT, null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new AuthResult(AuthCode.INTERRUPTED, null);
        } catch (Exception e) {
            return new AuthResult(AuthCode.SERVER_ERROR, null);
        }
    }

    public String getAuthToken() {
        return authToken;
    }

    public void logout() {
        this.authToken = null;
    }

    private static class LoginRequest {
        String username;
        String password;
        String email;

        LoginRequest(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }

        LoginRequest(String username, String password) {
            this(username, password, null);
        }
    }

}
