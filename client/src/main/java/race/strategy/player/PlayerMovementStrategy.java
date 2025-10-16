package race.strategy.player;

import race.Player;

public interface PlayerMovementStrategy {

    void updateMovement(Player player, float delta);

    float calculateCurrentSpeed(Player player);
}




