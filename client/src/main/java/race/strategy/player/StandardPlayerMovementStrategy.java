package race.strategy.player;

import race.Player;

public class StandardPlayerMovementStrategy implements PlayerMovementStrategy {
    
    @Override
    public void updateMovement(Player player, float delta) {
        float currentSpeed = calculateCurrentSpeed(player);
        float prevCol = player.getCol();

        player.setCol(prevCol + delta * currentSpeed);

        if (player.getVisionLimitColsLeft() > 0f) {
            float advancedCols = Math.max(0f, player.getCol() - prevCol);
            player.setVisionLimitColsLeft(Math.max(0f, player.getVisionLimitColsLeft() - advancedCols));
        }
    }
    
    @Override
    public float calculateCurrentSpeed(Player player) {
        float currentSpeed = player.getSpeed();

        if (player.getSlowDownTimeLeft() > 0f) {
            currentSpeed *= player.getSlowDownFactor();
        }

        if (player.isSprinting()) {
            currentSpeed *= 2.0f;
        }
        
        return currentSpeed;
    }
}
