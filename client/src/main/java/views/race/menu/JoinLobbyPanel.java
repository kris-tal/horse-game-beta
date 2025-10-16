package views.race.menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import services.listeners.JoinLobbyListener;
import services.managers.LobbyManager;
import views.common.ui.ListPanel;
import views.common.ui.UIFactory;

public class JoinLobbyPanel extends ListPanel implements LobbyManager.LobbyConnectionListener {
    private LobbyManager lobbyManager;
    private JoinLobbyListener listener;
    private TextField roomCodeField;
    private TextButton joinButton;
    private Label statusLabel;

    public JoinLobbyPanel(float width, float height, LobbyManager manager) {
        super(width, height);
        this.lobbyManager = manager;
        this.lobbyManager.setConnectionListener(this);
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

        lobbyManager.joinLobby(lobbyCode);
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

    public void onLobbyJoined(boolean success) {
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
        roomCodeField = UIFactory.createTextField("Enter Lobby Code");
        roomCodeField.setMessageText("ABC123");

        roomCodeField.setTextFieldFilter((textField, c) -> {
            if (Character.isLowerCase(c)) {
                String currentText = textField.getText();
                int cursorPosition = textField.getCursorPosition();
                String newText = currentText.substring(0, cursorPosition) +
                        Character.toUpperCase(c) +
                        currentText.substring(cursorPosition);

                textField.setText(newText);
                textField.setCursorPosition(cursorPosition + 1);

                return false;
            }
            return Character.isUpperCase(c) || Character.isDigit(c) || c == ' ';
        });

        content.add(roomCodeField).width(200f).padTop(smallPadding).padBottom(defaultPadding).row();

        joinButton = UIFactory.createLargeTextButton("JOIN LOBBY");
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                attemptJoinLobby();
            }
        });
        content.add(joinButton).padBottom(defaultPadding).row();

        statusLabel = UIFactory.createLabel("");
        statusLabel.setColor(1f, 0.3f, 0.3f, 1f);
        content.add(statusLabel).padTop(10f).row();

        Label instructionLabel = UIFactory.createLabel("Ask the lobby host for the code");
        instructionLabel.setColor(0.7f, 0.7f, 0.7f, 1f);
        content.add(instructionLabel).padTop(20f).row();
    }

    @Override
    protected String getTitle() {
        return "JOIN LOBBY";
    }

    @Override
    protected void populateRow(Table row, Object item) {
    }
}