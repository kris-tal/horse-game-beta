package race.collision;

import data.race.GameMap;
import race.Player;
import race.objects.BoostObject;
import race.objects.CoinObject;
import race.objects.EmptyObject;

public class CollisionSystem {

    public void checkCollisions(Player player, GameMap map) {
        int col = (int) Math.round(Math.ceil(player.getCol()));

        if (col < map.getLength()) {
            int lane = player.getLane();
            var obj = map.getObject(lane, col);

            obj.onCollision(player);

            if (obj.isObstacle() && !player.isAlive()) {
                return;
            }

            if (obj instanceof CoinObject coin && coin.isTaken()) {
                map.setObject(lane, col, new EmptyObject());
            }
            if (obj instanceof BoostObject) {
                map.setObject(lane, col, new EmptyObject());
            }
        }
    }
}
