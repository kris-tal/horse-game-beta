package data.lobby;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PlayerInfoClient {
    private String username;
    private int progress;
    private PlayerStatus role;
    private boolean connected;

    public PlayerInfoClient() {
    }

    public PlayerInfoClient(String username, PlayerStatus role) {
        this.username = username;
        this.progress = 0;
        this.role = role;
        this.connected = false;
    }

    public String getUsername() {
        return username;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public PlayerStatus getRole() {
        return role;
    }

    public boolean isCreator(){
        return getRole()==PlayerStatus.Host;
    }

    public void setRole(PlayerStatus role) {
        this.role = role;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    public void setUsername(String username) {this.username = username; }
    @Override
    public String toString() {
        return "PlayerInfoClient{" +
                "username='" + username + '\'' +
                ", progress=" + progress +
                ", role=" + role +
                ", connected=" + connected +
                '}';
    }

    public static class PlayerStatusDeserializer implements JsonDeserializer<PlayerStatus> {
        @Override
        public PlayerStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String roleString = json.getAsString();
            if ("Host".equals(roleString)) {
                return PlayerStatus.Host;
            } else if ("Player".equals(roleString)) {
                return PlayerStatus.Player;
            }
            throw new JsonParseException("Unknown PlayerStatus: " + roleString);
        }
    }
}