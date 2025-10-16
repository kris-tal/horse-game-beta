package race;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class MapObject {
    private final TextureRegion texture;
    private final float scale;

    public MapObject(TextureRegion texture) {
        this(texture, 1.0f);
    }

    public MapObject(TextureRegion texture, float scale) {
        this.texture = texture;
        this.scale = scale;
    }

    public abstract void onCollision(Player player);

    public void render(SpriteBatch batch, float x, float y, float size) {
        if (texture != null) {
            float scaledSize = size * scale;
            float offset = (size - scaledSize) / 2f;
            batch.draw(texture, x + offset, y + offset, scaledSize, scaledSize);
        }
    }

    public boolean isObstacle() {
        return false;
    }

    public boolean isFatal() {
        return false;
    }

}
