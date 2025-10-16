package race.strategy.player;

import race.Player;

public interface StatusEffectManager {
    void applySlowDown(Player player, float seconds);
    void applyVisionLimit(Player player, float seconds);
    void applyVisionLimitByCols(Player player, float cols);
    void applyVisionLimitByMeters(Player player, float meters);
    boolean isVisionLimited(Player player);
    void updateStatusEffects(Player player, float delta);
}




