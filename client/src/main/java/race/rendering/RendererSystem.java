package race.rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public class RendererSystem implements Renderer {
    private final List<Renderer> renderers;

    public RendererSystem(List<Renderer> renderers) {
        this.renderers = renderers;
    }

    @Override
    public void render(SpriteBatch batch) {
        for (Renderer renderer : renderers) {
            renderer.render(batch);
        }
    }
}
