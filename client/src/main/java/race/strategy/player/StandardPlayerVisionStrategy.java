package race.strategy.player;

import race.Player;

public class StandardPlayerVisionStrategy implements PlayerVisionStrategy {
    
    @Override
    public void updateVision(Player player, float delta) {
        if (player.getVisionLimitTime() > 0f) {
            player.setVisionLimitTime(player.getVisionLimitTime() - delta);
        }
    }

}
