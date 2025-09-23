package views.ranch.race;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import views.common.MenuContext;
import views.common.Panel;
import views.common.UIFactory;

public class RaceMenuPanel extends Panel {
    public interface RaceMenuListener {
        void onModeSelected(MenuContext.RaceMode mode);
    }

    private RaceMenuListener listener;

    public RaceMenuPanel(float width, float height) {
        super(width, height);
    }

    @Override
    protected String getTitle() {
        return "RACE MENU";
    }

    @Override
    protected void addContent(float width, float height) {
        TextButton singleplayerButton = UIFactory.createLargeTextButton("singleplayer");
        singleplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) listener.onModeSelected(MenuContext.RaceMode.SINGLEPLAYER);
            }
        });
        content.add(singleplayerButton).padTop(smallPadding).padBottom(defaultPadding).row();

        TextButton multiplayerButton = UIFactory.createLargeTextButton("multiplayer");
        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) {
                    listener.onModeSelected(MenuContext.RaceMode.MULTIPLAYER);
                }
            }
        });

        content.add(multiplayerButton).padBottom(defaultPadding).row();

    }

    public void setListener(RaceMenuListener listener) {
        this.listener = listener;
    }

}
