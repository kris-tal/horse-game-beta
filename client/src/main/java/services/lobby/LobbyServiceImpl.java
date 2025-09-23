package services.lobby;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import data.lobby.LobbyCreationResponse;
import io.socket.client.IO;
import io.socket.client.Socket;
import services.managers.LobbyManager;
import services.managers.SessionManager;
import services.profile.ProfileServiceImpl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import static services.auth.AuthServiceImpl.BASE_URL;

public class LobbyServiceImpl implements LobbyService {
    String currentLobby;
    Socket sessionSocket;
    Gson gson;
    private final LobbyTracker lobbyTracker;
    private String currentUserUsername;
    private boolean isLobbyCreator = false;
    private boolean connected = false;

    public LobbyServiceImpl() {
        gson = new Gson();
        lobbyTracker = new LobbyTracker();
    }

    @Override
    public String createLobby() {
        HttpRequest request = SessionManager.getInstance()
                .getProtectedRequestsService()
                .ProtectedPost("/lobby", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = SessionManager.getInstance()
                    .getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String lobbyCode = gson.fromJson(response.body(), LobbyCreationResponse.class).getCode();
                isLobbyCreator = true;
                return lobbyCode;
            } else {
                Gdx.app.error("Lobby", "Failed to create lobby: " + response.body());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            Gdx.app.error("Lobby", "Error creating lobby: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean joinLobby(String room) {
        try {
            IO.Options options = new IO.Options();
            options.auth = new HashMap<>();
            options.auth.put("token", SessionManager.getInstance().getToken());
            options.auth.put("room", room);

            currentLobby = room;
            sessionSocket = IO.socket(new URI(BASE_URL), options);
            setupSocketEvents();
            sessionSocket.open();
            sessionSocket.connect();

            // Return true immediately - actual connection status handled via events
            Gdx.app.log("Lobby", "Attempting to join lobby: " + room);
            return true; // FIXED: Was returning false always

        } catch (Exception e) {
            Gdx.app.error("Lobby", "Error joining lobby: " + e.getMessage());
            currentLobby = null;
            return false;
        }
    }

    @Override
    public boolean startLobby() {
        if (currentLobby == null) {
            Gdx.app.log("Lobby", "No lobby to start!");
            return false;
        }

        if (!isLobbyCreator) {
            Gdx.app.log("Lobby", "Only lobby creator can start the game!");
            return false;
        }

        HttpRequest request = SessionManager.getInstance()
                .getProtectedRequestsService()
                .ProtectedPost("/lobby/" + currentLobby + "/start", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = SessionManager.getInstance()
                    .getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gdx.app.log("Lobby", "Lobby started successfully!");
                return true;
            } else {
                Gdx.app.log("Lobby", "Failed to start lobby: " + response.body());
                return false;
            }
        } catch (IOException | InterruptedException e) {
            Gdx.app.log("Lobby", "Error starting lobby: " + e.getMessage());
            return false;
        }
    }

    private void setupSocketEvents() {
        sessionSocket.on("player_joined", args -> {
            if (args.length > 1) {
                String data = args[1].toString();
                Gdx.app.log("Lobby", "Player joined: " + data);
                lobbyTracker.updateFromServer(data);
            } else {
                String data = args[0].toString();
                lobbyTracker.updateFromServer(data);
                Gdx.app.log("Lobby", "player_joined event with no payload");
            }
        });

        sessionSocket.on("progress_update", args -> {
            // String data = args[1].toString();
            // lobbyTracker.updateFromServer(data);
        });

        sessionSocket.on("player_left", args -> {
            if (args.length > 1) {
                String data = args[1].toString();
                Gdx.app.log("Lobby", "player_left primary: " + data);
                lobbyTracker.updateFromServer(data);
            } else {
                String data = args[0].toString();
                lobbyTracker.updateFromServer(data);
                Gdx.app.log("Lobby", "player_left secondary: " + data );
            }
        });

        sessionSocket.on("lobby_started", args -> {
            Gdx.app.log("Lobby", "Lobby has been started by creator!");
            LobbyManager.getInstance().onLobbyStarted();
        });

        sessionSocket.on("room_closed", args -> {
            LobbyManager.getInstance().onLobbyClosed();
        });

        sessionSocket.on("connect", args -> {
            try {
                currentUserUsername = new ProfileServiceImpl().getUserData().getUsername();
                Gdx.app.log("Lobby", "Connected to lobby successfully!");
                connected = true;

                Gdx.app.postRunnable(() -> {
                    LobbyManager.getInstance().onLobbyJoined(true);
                });
            } catch (Exception e) {
                Gdx.app.error("Lobby", "Error getting user data: " + e.getMessage());
                Gdx.app.postRunnable(() -> {
                    LobbyManager.getInstance().onLobbyJoined(false);
                });
            }
        });

        sessionSocket.on("disconnect", args -> {
            Gdx.app.log("Lobby", "Disconnected from lobby socket");
            connected = false;
            LobbyManager.getInstance().onLobbyClosed();
        });

        sessionSocket.on(Socket.EVENT_CONNECT_ERROR, args -> {
            Gdx.app.error("Lobby", "Connection error: " + args[0]);
            connected = false;

            Gdx.app.postRunnable(() -> {
                LobbyManager.getInstance().onLobbyJoined(false);
            });
        });
    }

    @Override
    public void updatePosition(int progress) {
        if (sessionSocket != null && sessionSocket.connected()) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", SessionManager.getInstance().getToken());
            data.put("room", currentLobby);
            data.put("progress", progress);

            sessionSocket.emit("progress_update", gson.toJson(data));
        }
    }

    @Override
    public void closeConnection() {
        if (sessionSocket != null) {
            sessionSocket.close();
        }
        sessionSocket = null;
        currentLobby = null;
        currentUserUsername = null;
        isLobbyCreator = false;
        connected = false;
    }

    public boolean isLobbyCreator() {
        return isLobbyCreator;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getCurrentUserUsername() {
        return currentUserUsername;
    }
}