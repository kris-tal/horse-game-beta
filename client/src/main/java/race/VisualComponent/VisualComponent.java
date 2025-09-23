package race.VisualComponent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface VisualComponent {
    void update(float delta);
    void render(SpriteBatch batch, float x, float y, float size);
}
