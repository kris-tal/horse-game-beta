package views.common.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ConfirmationPanel extends Panel {
    private final Runnable onConfirm;
    private final String title;
    private static ConfirmationPanel instance;

    private ConfirmationPanel(float width, float height, String title, Runnable onConfirm) {
        super(width, height);
        this.onConfirm = onConfirm;
        this.title = title;
        super.build();
    }

    public static ConfirmationPanel getInstance(float width, float height, String title, Runnable onConfirm) {
        if (instance == null)
            instance = new ConfirmationPanel(width, height, title, onConfirm);
        return instance;
    }

    @Override
    protected String getTitle() {
        return title;
    }

    @Override
    protected void addContent(float width, float height) {
        TextButton yesButton = UIFactory.createLargeTextButton("YES");
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onConfirm.run();
                hide();
            }
        });

        TextButton noButton = UIFactory.createLargeTextButton("NO");
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        content.add(yesButton).padLeft(defaultPadding).padRight(defaultPadding);
        content.add(noButton).padLeft(defaultPadding).padRight(defaultPadding);
    }
}
