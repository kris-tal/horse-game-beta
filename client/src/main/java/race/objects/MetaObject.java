package race.objects;

import race.MapObject;
import race.Player;
import services.managers.ResourceManager;

public class MetaObject extends MapObject {
    public MetaObject() {
        super(ResourceManager.obstaclesAtlas.findRegion("meta"));
    }

    @Override
    public void onCollision(Player player) {

    }

    @Override
    public boolean isObstacle() {
        return true;
    }

    @Override
    public boolean isFatal() {
        return false;
    }
}