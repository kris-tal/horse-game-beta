package views.common.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public abstract class Panel extends Table {
    protected final float smallPadding = 20f;
    protected final float defaultPadding = 50f;
    protected final float bigPadding = 70f;

    protected Table content = new Table();
    protected Label titleLabel;
    private final float width;
    private final float height;

    public Panel(float width, float height) {
        super();
        this.width = width;
        this.height = height;
        setVisible(false);
    }

    protected void build() {
        content.clear();

        setBackground(new NinePatchDrawable(UIStyles.getDefaultBackgroundPatch()));
        setSize(width, height);
        pad(smallPadding);

        if (getTitle() != null) {
            titleLabel = UIFactory.createLargeLabel(getTitle());
            content.add(titleLabel).padBottom(defaultPadding).center().row();
        }

        addContent(width, height);

        Container<Table> contentContainer = new Container<>(content);

        add(contentContainer).expand().fill();

        TextButton closeButton = UIFactory.createLargeTextButton("X");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });

        Container<TextButton> closeContainer = new Container<>(closeButton);
        closeContainer.top().right();

        addActor(closeButton);
        closeButton.setPosition(getWidth() - closeButton.getWidth() - smallPadding, getHeight() - closeButton.getHeight() - smallPadding);
    }

    public void show(float screenWidth, float screenHeight) {
        setPosition(
                (screenWidth - getWidth()) / 2f,
                (screenHeight - getHeight()) / 2f
        );
        setVisible(true);
    }

    public void close() {
        hide();
    }

    public void hide() {
        setVisible(false);
    }

    protected abstract String getTitle();

    protected abstract void addContent(float width, float height);

    public boolean isOpen() {
        return isVisible();
    }
}
