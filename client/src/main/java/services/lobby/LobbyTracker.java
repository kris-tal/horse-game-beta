package services.lobby;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import data.lobby.PlayerInfoClient;
import data.lobby.PlayerListResponse;
import services.managers.LobbyManager;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class LobbyTracker {
    private final Gson gson = new Gson();

    public void updateFromServer(String jsonData) {
        try {
            Gdx.app.log("LobbyTracker", "Received data: " + jsonData);
            List<PlayerInfoClient> players = gson.fromJson(jsonData, PlayerListResponse.class).getPlayers();

            if (players != null) {
                HashMap<String, PlayerInfoClient> map = new HashMap<>();
                for (PlayerInfoClient p : players) {
                    map.put(p.getUsername(), p);
                }
                LobbyManager.getInstance().updatePlayers(map);
            }
        } catch (Exception e) {
            Gdx.app.error("LobbyTracker", "Error parsing player data: " + e.getMessage());
        }
    }
}