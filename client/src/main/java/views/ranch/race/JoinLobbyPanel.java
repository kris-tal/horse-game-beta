package views.ranch.race;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import services.managers.LobbyManager;
import views.common.ListPanel;
import views.common.UIFactory;

public class JoinLobbyPanel extends ListPanel implements LobbyManager.LobbyConnectionListener {

    private JoinLobbyListener listener;
    private TextField roomCodeField;
    private TextButton joinButton;
    private Label statusLabel;

    public interface JoinLobbyListener {
        void onLobbyJoined();
    }

    public JoinLobbyPanel(float width, float height) {
        super(width, height);
        LobbyManager.getInstance().setConnectionListener(this);
    }


    private void attemptJoinLobby() {
        String lobbyCode = roomCodeField.getText().trim().toUpperCase();

        if (lobbyCode.isEmpty()) {
            showError("Please enter a lobby code");
            return;
        }

        if (lobbyCode.length() != 6) {
            showError("Lobby codes consist of 6 letters and digits");
            return;
        }

        joinButton.setDisabled(true);
        joinButton.setText("JOINING...");
        statusLabel.setText("Connecting to lobby...");
        statusLabel.setColor(1f, 1f, 0.3f, 1f); // Yellow for loading

        LobbyManager.getInstance().joinLobby(lobbyCode);
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setColor(1f, 0.3f, 0.3f, 1f); // Red color
    }

    public void setListener(JoinLobbyListener listener) {
        this.listener = listener;
    }

    public void clearFields() {
        if (roomCodeField != null) {
            roomCodeField.setText("");
        }
        if (statusLabel != null) {
            statusLabel.setText("");
        }
    }

    public void onLobbyJoined(boolean success){
        if (success) {
            statusLabel.setText("Successfully joined!");
            statusLabel.setColor(0.3f, 1f, 0.3f, 1f);

            if (listener != null) {
                listener.onLobbyJoined();
            }
        } else {
            showError("Failed to join lobby.");
        }

        joinButton.setDisabled(false);
        joinButton.setText("JOIN LOBBY");
    }
    @Override
    protected void addContent(float width, float height) {
        /*
            TODO:
            * Make it auto uppercase since the only allowed codes are combined uppercase numbers and letters
         */
        roomCodeField = UIFactory.createTextField("Enter Lobby Code");
        roomCodeField.setMessageText("ABC123");
        content.add(roomCodeField).width(200f).padTop(smallPadding).padBottom(defaultPadding).row();

        joinButton = UIFactory.createLargeTextButton("JOIN LOBBY");
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                attemptJoinLobby();
            }
        });
        content.add(joinButton).padBottom(defaultPadding).row();

        // Status label for feedback
        statusLabel = UIFactory.createLabel("");
        statusLabel.setColor(1f, 0.3f, 0.3f, 1f); // Red color for errors
        content.add(statusLabel).padTop(10f).row();

        // Instructions
        Label instructionLabel = UIFactory.createLabel("Ask the lobby host for the code");
        instructionLabel.setColor(0.7f, 0.7f, 0.7f, 1f); // Gray color
        content.add(instructionLabel).padTop(20f).row();
    }

    @Override
    protected String getTitle() {
        return "JOIN LOBBY";
    }
    @Override
    protected void populateRow(Table row, Object item) {}
}