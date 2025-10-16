package race.objects;

import race.MapObject;
import race.Player;
import services.managers.ResourceManager;

public class ElectricBarrierObject extends MapObject {

    private boolean destroyed = false;

    public ElectricBarrierObject() {
        super(ResourceManager.obstaclesAtlas.findRegion("barrier"));
    }

    @Override
    public void onCollision(Player player) {
        if (destroyed) return;

        if (player.isSprinting()) {
            destroyed = true;
            player.resetSprint();
            player.slowDown(1f);
        } else {
            player.kill();
        }
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
