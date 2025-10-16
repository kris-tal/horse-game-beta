// java
package services.lobby;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data.lobby.LobbyCreationResponse;
import data.race.GameMap;
import services.managers.ConnectionManagerPort;
import services.managers.SessionManagerPort;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LobbyServiceImpl implements LobbyService {
    private final SessionManagerPort sessionManager;
    private final ConnectionManagerPort connectionManager;
    private final Gson gson = new Gson();
    private String currentLobby;
    private boolean isLobbyCreator = false;

    public LobbyServiceImpl(SessionManagerPort sessionManager, ConnectionManagerPort connectionManager) {
        this.sessionManager = sessionManager;
        this.connectionManager = connectionManager;
    }

    @Override
    public String createLobby() {
        HttpRequest request = sessionManager.getProtectedRequestsService()
                .ProtectedPost("/lobby", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = sessionManager.getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String lobbyCode = gson.fromJson(response.body(), LobbyCreationResponse.class).getCode();
                isLobbyCreator = true;
                return lobbyCode;
            } else {
                return null;
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    @Override
    public boolean joinLobby(String room) {
        currentLobby = room;
        return connectionManager.connectToRoom(room, sessionManager.getToken());
    }

    @Override
    public void startLobby(String room, GameMap map) {
        if (currentLobby == null) return;
        if (!isLobbyCreator) return;

        try {
            String mapJson = map.Serialize();

            JsonObject payload = new JsonObject();
            payload.addProperty("map_data", mapJson);

            String jsonString = new Gson().toJson(payload);

            HttpRequest request = sessionManager.getProtectedRequestsService()
                    .ProtectedPost("/lobby/" + room + "/start",
                            HttpRequest.BodyPublishers.ofString(jsonString))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = sessionManager.getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gdx.app.log("Lobby", "Started room " + room + " with map");
            } else {
                Gdx.app.log("Lobby", "Failed to start room " + room + ": " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            Gdx.app.log("Lobby", "Failed to start room " + room + ": " + e.getMessage());
        }
    }

    @Override
    public void updatePosition(int progress) {
        connectionManager.emitProgressUpdate(
                sessionManager.getToken(),
                progress
        );
    }

    @Override
    public void closeConnection() {
        connectionManager.closeConnection();
        currentLobby = null;
        isLobbyCreator = false;
    }
}
