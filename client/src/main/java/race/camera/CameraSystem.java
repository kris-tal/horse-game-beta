package race.camera;

import com.badlogic.gdx.Gdx;
import data.race.GameMap;
import race.Player;

public class CameraSystem {
    private final int visibleCols;
    private final float cellSize;
    private final float leftOffset;
    private final float downOffset;
    
    private float scrollX = 0f;

    public CameraSystem(GameMap map, int visibleCols) {
        this.visibleCols = visibleCols;
        
        int maxCellHeight = Gdx.graphics.getWidth() / visibleCols;
        int maxCellWidth = Gdx.graphics.getHeight() / map.getLanes();

        cellSize = Math.max(32, Math.min(maxCellWidth, maxCellHeight));
        leftOffset = (Gdx.graphics.getWidth() - visibleCols * cellSize) / 2;
        downOffset = (Gdx.graphics.getHeight() - map.getLanes() * cellSize) / 2;
    }

    public void update(Player player, GameMap map) {
        scrollX = player.getCol();
        if (scrollX > map.getLength() - visibleCols) {
            scrollX = map.getLength() - visibleCols;
        }
    }

    public float getScreenX(float col) {
        return (col - scrollX) * cellSize + leftOffset;
    }

    public float getScreenY(int lane) {
        return lane * cellSize + downOffset;
    }

    public float getScrollX() { return scrollX; }
    public int getVisibleCols() { return visibleCols; }
    public float getCellSize() { return cellSize; }
    public float getLeftOffset() { return leftOffset; }
}
