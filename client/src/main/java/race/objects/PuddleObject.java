package race.objects;

import race.MapObject;
import race.Player;
import services.managers.ResourceManager;

public class PuddleObject extends MapObject {

    public PuddleObject() {
        super(ResourceManager.obstaclesAtlas.findRegion("puddle"));
    }

    @Override
    public void onCollision(Player player) {
        player.slowDown();
        player.resetSprint();
    }

    @Override
    public boolean isObstacle() {
        return false;
    }
}
