package race.collision;

import data.race.GameMap;
import race.Player;

public class CollisionSystem {

    public void checkCollisions(Player player, GameMap map) {
        int col = Math.round(player.getCol());

        if (col < map.getLength()) {
            int lane = player.getLane();
            var obj = map.getObject(lane, col);

            obj.onCollision(player);

            if (obj instanceof race.objects.CoinObject coin && coin.isTaken()) {
                map.setObject(lane, col, new race.objects.EmptyObject());
            }
        }
    }
}
