package race.state;

import data.race.GameMap;
import race.Player;

public class GameStateManager {

    public enum GameState {
        RUNNING,
        COMPLETED,
        PLAYER_DEAD,
        FINISHED
    }

    private GameState currentState = GameState.RUNNING;

    public void update(Player player, GameMap map) {
        if (currentState == GameState.FINISHED
                || currentState == GameState.COMPLETED
                || currentState == GameState.PLAYER_DEAD) {
            return;
        }

        if (player.isRaceCompleted() || player.getCol() >= map.getLength()) {
            currentState = GameState.COMPLETED;
            return;
        }

        if (!player.isAlive()) {
            currentState = GameState.PLAYER_DEAD;
            return;
        }
    }

    public boolean isRaceFinished() {
        return currentState == GameState.COMPLETED
                || currentState == GameState.PLAYER_DEAD;
    }

    public boolean isPlayerDead() {
        return currentState == GameState.PLAYER_DEAD;
    }

    public boolean isRaceCompleted() {
        return currentState == GameState.COMPLETED;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void reset() {
        currentState = GameState.RUNNING;
    }

    public void completeRace() {
        currentState = GameState.COMPLETED;
    }
}
