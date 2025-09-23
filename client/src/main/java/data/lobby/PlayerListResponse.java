package data.lobby;

import java.util.List;

public class PlayerListResponse {
    public List<PlayerInfoClient> getPlayers() {
        return players;
    }

    private List<PlayerInfoClient> players;

}