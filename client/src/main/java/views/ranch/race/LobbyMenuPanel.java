package views.ranch.race;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import views.common.context.MenuContext;
import views.common.ui.Panel;
import views.common.ui.UIFactory;

public class LobbyMenuPanel extends Panel {
    public interface LobbyMenuListener {
        void onModeSelected(MenuContext.LobbyMode lobbyMode);
    }

    private LobbyMenuPanel.LobbyMenuListener listener;
    private boolean isCreatingLobby = false;

    public LobbyMenuPanel(float width, float height) {
        super(width, height);
        super.build();
    }

    @Override
    protected String getTitle() {
        return "LOBBY MENU";
    }

    @Override
    protected void addContent(float width, float height) {
        TextButton createLobbyButton = UIFactory.createLargeTextButton("create lobby");
        createLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isCreatingLobby) {
                    return;
                }
                isCreatingLobby = true;
                if (listener != null) listener.onModeSelected(MenuContext.LobbyMode.CREATE);
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        isCreatingLobby = false;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
        });
        content.add(createLobbyButton).padTop(smallPadding).padBottom(defaultPadding).row();

        TextButton joinLobbyButton = UIFactory.createLargeTextButton("join lobby");
        joinLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) listener.onModeSelected(MenuContext.LobbyMode.JOIN);

            }
        });
        content.add(joinLobbyButton).padBottom(defaultPadding).row();
    }

    public void setListener(LobbyMenuPanel.LobbyMenuListener listener) {
        this.listener = listener;
    }
}
