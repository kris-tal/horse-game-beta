package race.objects;

import race.MapObject;
import race.Player;
import services.managers.ResourceManager;

public class CoinObject extends MapObject {
    boolean taken = false;

    public CoinObject() {
        super(ResourceManager.iconsAtlas.findRegion("coin_icon"), 0.5f);
    }

    @Override
    public void onCollision(Player player) {
        if (!taken) {
            taken = true;
            player.addCoins(1);
        }
    }

    public boolean isTaken() {
        return taken;
    }
}