package race.objects;

import race.MapObject;
import race.Player;
import services.managers.ResourceManager;

public class BoostObject extends MapObject {

    public BoostObject() {
        super(ResourceManager.obstaclesAtlas.findRegion("energy_drink"));
    }

    @Override
    public void onCollision(Player player) {
        if (player.isSprinting()) {
            player.makeSprintAvailable();
            player.startSprint();
        } else {
            player.makeSprintAvailable();
        }
    }

    @Override
    public boolean isObstacle() {
        return false;
    }
}
