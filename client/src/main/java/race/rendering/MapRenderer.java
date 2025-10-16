package race.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import data.race.GameMap;
import race.camera.CameraSystem;

public class MapRenderer implements Renderer {

    private final GameMap map;
    private final CameraSystem camera;

    public MapRenderer(GameMap map, CameraSystem camera) {
        this.map = map;
        this.camera = camera;
    }

    @Override
    public void render(SpriteBatch batch) {
        int startCol = (int) camera.getScrollX() - 1;
        int endCol = startCol + camera.getVisibleCols() + 2;

        if (endCol > map.getLength()) endCol = map.getLength();

        for (int lane = 0; lane < map.getLanes(); lane++) {
            for (int col = startCol; col < endCol; col++) {
                if (col >= 0) {
                    var obj = map.getObject(lane, col);
                    if (obj != null) {
                        float x = camera.getScreenX(col);
                        float y = camera.getScreenY(lane);
                        obj.render(batch, x, y, camera.getCellSize());
                    }
                }
            }
        }
    }
}
