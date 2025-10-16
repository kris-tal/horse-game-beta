package race.strategy.player;

import race.Player;

public class StandardStatusEffectManager implements StatusEffectManager {
    
    @Override
    public void applySlowDown(Player player, float seconds) {
        player.setSlowDownTimeLeft(seconds);
    }
    
    @Override
    public void applyVisionLimit(Player player, float seconds) {
        player.setVisionLimitTime(seconds);
    }
    
    @Override
    public void applyVisionLimitByCols(Player player, float cols) {
        player.setVisionLimitColsLeft(Math.max(player.getVisionLimitColsLeft(), cols));
    }
    
    @Override
    public void applyVisionLimitByMeters(Player player, float meters) {
        applyVisionLimitByCols(player, meters / 10f);
    }
    
    @Override
    public boolean isVisionLimited(Player player) {
        return player.getVisionLimitTime() > 0f || player.getVisionLimitColsLeft() > 0f;
    }
    
    @Override
    public void updateStatusEffects(Player player, float delta) {
        if (player.getSlowDownTimeLeft() > 0f) {
            player.setSlowDownTimeLeft(player.getSlowDownTimeLeft() - delta);
        }

        if (player.getVisionLimitTime() > 0f) {
            player.setVisionLimitTime(player.getVisionLimitTime() - delta);
        }
    }
}




