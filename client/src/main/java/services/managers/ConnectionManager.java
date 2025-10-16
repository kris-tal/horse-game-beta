package services.managers;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import data.lobby.PlayerInfoClient;
import data.lobby.PlayerListResponse;
import data.race.PlayerProgressResponse;
import io.socket.client.IO;
import io.socket.client.Socket;
import services.listeners.GameListener;
import services.listeners.LobbyListener;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import static services.auth.AuthServiceImpl.BASE_URL;

public class ConnectionManager implements ConnectionManagerPort {
    private Socket sessionSocket;
    private final Gson gson;
    private LobbyListener lobbyListener;
    private GameListener gameListener;
    private String currentRoom;
    private boolean connected;

    public ConnectionManager() {
        gson = new Gson();
        connected = false;
    }

    @Override
    public void setLobbyListener(LobbyListener listener) {
        this.lobbyListener = listener;
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.gameListener = listener;
    }

    @Override
    public void clearListeners() {
        this.gameListener = null;
        this.lobbyListener = null;
    }

    @Override
    public boolean connectToRoom(String room, String token) {
        try {
            if (sessionSocket != null) {
                sessionSocket.close();
                sessionSocket = null;
            }

            IO.Options options = new IO.Options();
            options.auth = new HashMap<>();
            options.auth.put("token", token);
            options.auth.put("room", room);

            currentRoom = room;
            sessionSocket = IO.socket(new URI(BASE_URL), options);
            setupSocketEvents();
            sessionSocket.open();
            sessionSocket.connect();

            return true;
        } catch (Exception e) {
            currentRoom = null;
            return false;
        }
    }

    @Override
    public void emitProgressUpdate(String token, int progress) {
        if (sessionSocket != null && sessionSocket.connected()) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("room", currentRoom);
            data.put("progress", progress);
            sessionSocket.emit("progress_update", data);
        }
    }

    private void setupSocketEvents() {
        // Lobby-related events
        sessionSocket.on("player_joined", args -> {
            String data = args.length > 1 ? args[1].toString() : args[0].toString();
            handlePlayerListUpdate(data);
        });

        sessionSocket.on("player_left", args -> {
            String data = args.length > 1 ? args[1].toString() : args[0].toString();
            handlePlayerListUpdate(data);
        });

        sessionSocket.on("room_started", args -> {
            if (lobbyListener != null) {
                String data = args.length > 1 ? args[1].toString() : args[0].toString();
                lobbyListener.onLobbyStarted(data);
            }
        });

        sessionSocket.on("connect", args -> {
            try {
                connected = true;
                Gdx.app.postRunnable(() -> {
                    if (lobbyListener != null) {
                        lobbyListener.onLobbyJoined(true);
                        lobbyListener.onLobbyInitialized();
                    }
                });
            } catch (Exception e) {
                Gdx.app.postRunnable(() -> {
                    if (lobbyListener != null) {
                        lobbyListener.onLobbyJoined(false);
                    }
                });
            }
        });

        sessionSocket.on("disconnect", args -> {
            connected = false;
            if (lobbyListener != null) {
                lobbyListener.onLobbyClosed();
            }
        });

        sessionSocket.on(Socket.EVENT_CONNECT_ERROR, args -> {
            connected = false;
            Gdx.app.postRunnable(() -> {
                if (lobbyListener != null) {
                    lobbyListener.onLobbyJoined(false);
                }
            });
        });

        // Game-related events
        sessionSocket.on("progress_update", args -> {
            if (gameListener != null) {
                String data = args.length > 1 ? args[1].toString() : args[0].toString();
                handleProgressUpdate(data);
            }
        });

        sessionSocket.on("game_won", args -> {
            if (gameListener != null) {
                String winnerData = args.length > 1 ? args[1].toString() : args[0].toString();
                JsonObject jsonObj = new Gson().fromJson(winnerData, JsonObject.class);
                String winnerDataInner = jsonObj.get("winner").toString();
                jsonObj = new Gson().fromJson(winnerDataInner, JsonObject.class);
                String winnerUsername = jsonObj.get("username").toString();
                gameListener.onGameWon(winnerUsername);
            }
        });

        // room_closed can be both lobby and game event
        sessionSocket.on("room_closed", args -> {
            if (lobbyListener != null) {
                lobbyListener.onLobbyClosed();
            }
            if (gameListener != null) {
                gameListener.onGameClosed();
            }
        });
    }

    private void handlePlayerListUpdate(String jsonData) {
        try {
            Gdx.app.postRunnable(() -> {
                List<PlayerInfoClient> players = gson.fromJson(jsonData, PlayerListResponse.class).getPlayers();
                if (players != null && lobbyListener != null) {
                    HashMap<String, PlayerInfoClient> playerMap = new HashMap<>();
                    for (PlayerInfoClient p : players) {
                        playerMap.put(p.getUsername(), p);
                    }
                    lobbyListener.onPlayerListChanged(playerMap);
                }
            });
        } catch (Exception e) {
            System.out.println("ConnectionManager: Error parsing player list: "
                    + jsonData + ", error: " + e.getMessage());
        }
    }

    private void handleProgressUpdate(String jsonData) {
        try {
            Gdx.app.postRunnable(() -> {
                List<PlayerProgressResponse.PlayerProgress> progress =
                        gson.fromJson(jsonData, PlayerProgressResponse.class).getDistances();
                if (gameListener != null) {
                    gameListener.onProgressUpdate(progress);
                }
            });
        } catch (Exception e) {
            System.out.println("ConnectionManager: Error parsing progress update: " + jsonData + ", error: " + e.getMessage());
        }

    }

    @Override
    public void closeConnection() {
        if (sessionSocket != null) {
            sessionSocket.off();
            sessionSocket.close();
        }
        sessionSocket = null;
        currentRoom = null;
        connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public String getCurrentRoom() {
        return currentRoom;
    }

    @Override
    public Socket getSocket() {
        return sessionSocket;
    }
}