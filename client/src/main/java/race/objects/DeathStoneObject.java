package race.objects;

import race.MapObject;
import race.Player;
import services.managers.ResourceManager;

public class DeathStoneObject extends MapObject {
    public DeathStoneObject() {
        super(ResourceManager.obstaclesAtlas.findRegion("death_stone"));
    }

    @Override
    public void onCollision(Player player) {
        player.kill();
    }

    @Override
    public boolean isObstacle() {
        return true;
    }

    @Override
    public boolean isFatal() {
        return true;
    }

}