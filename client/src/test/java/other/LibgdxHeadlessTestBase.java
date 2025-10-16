// java
package other;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.mockito.Mockito.mock;

public abstract class LibgdxHeadlessTestBase {
    private static HeadlessApplication app;

    @BeforeAll
    static void startHeadless() {
        if (app == null) {
            HeadlessApplicationConfiguration cfg = new HeadlessApplicationConfiguration();
            app = new HeadlessApplication(new ApplicationAdapter() {
            }, cfg);
            GL20 gl = mock(GL20.class);
            Gdx.gl = gl;
            Gdx.gl20 = gl;
        }
    }

    @AfterAll
    static void stopHeadless() {
        if (app != null) {
            app.exit();
            app = null;
        }
    }
}