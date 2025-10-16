package race.objects;

import race.MapObject;
import race.Player;
import services.managers.ResourceManager;

public class BushObject extends MapObject {

    public BushObject() {
        super(ResourceManager.obstaclesAtlas.findRegion("bush"));
    }

    @Override
    public void onCollision(Player player) {
        player.limitVisionByMeters(10f);
    }

    @Override
    public boolean isObstacle() {
        return false;
    }
}
