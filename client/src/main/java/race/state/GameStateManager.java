package race.state;

import race.Player;
import data.race.GameMap;

public class GameStateManager {
    private boolean raceFinished = false;
    private boolean playerDead = false;
    private boolean raceCompleted = false;

    public void update(Player player, GameMap map) {
        if (raceFinished) return;

        if (!player.isAlive() && !playerDead) {
            playerDead = true;
            raceFinished = true;
        }

        if (player.getCol() >= map.getLength() && !raceCompleted) {
            raceCompleted = true;
            raceFinished = true;
        }
    }

    public boolean isRaceFinished() {
        return raceFinished;
    }

    public boolean isPlayerDead() {
        return playerDead;
    }

    public boolean isRaceCompleted() {
        return raceCompleted;
    }

    public void reset() {
        raceFinished = false;
        playerDead = false;
        raceCompleted = false;
    }
}
