package views.common.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import services.managers.ResourceManager;

public class UIStyles {
    private final static TextureAtlas atlas = ResourceManager.uiAtlas;
    private final static Color defaultFontColor = new Color(216 / 255f, 204 / 255f, 193 / 255f, 1f);
    private final static Color darkerFontColor = new Color(144 / 255f, 113 / 255f, 96 / 255f, 1f);
    private final static BitmapFont defaultFont = ResourceManager.uiDefaultFont;
    private final static BitmapFont mediumFont = ResourceManager.uiMediumFont;
    private final static BitmapFont largeFont = ResourceManager.uiLargeFont;


    static {
        defaultFont.getData().setScale(0.5f);
        mediumFont.getData().setScale(0.8f);
        mediumFont.getData().setLineHeight(mediumFont.getData().lineHeight * 2f);
        largeFont.getData().setScale(1.5f);
    }

    public static void resizeFonts(int width, int height) {
        float scale = 1;
        defaultFont.getData().setScale(0.5f * scale);
        mediumFont.getData().setScale(0.8f * scale);
        largeFont.getData().setScale(1.5f * scale);
    }

    public static TextButton.TextButtonStyle getDefaultButtonStyle() {
        NinePatch upPatch = atlas.createPatch("color5_up");
        NinePatch downPatch = atlas.createPatch("color5_down");

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = new NinePatchDrawable(upPatch);
        style.down = new NinePatchDrawable(downPatch);
        style.font = defaultFont;
        style.fontColor = defaultFontColor;

        return style;
    }

    public static TextField.TextFieldStyle getTextFieldStyle() {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();

        NinePatch background = atlas.createPatch("color4_down");
        NinePatchDrawable backgroundDrawable = new NinePatchDrawable(background);
        backgroundDrawable.setLeftWidth(backgroundDrawable.getLeftWidth() + 10);
        style.background = backgroundDrawable;

        style.font = defaultFont;
        style.fontColor = defaultFontColor;
        style.messageFontColor = darkerFontColor;

        return style;
    }

    public static Label.LabelStyle getDefaultLabelStyle() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.fontColor = defaultFontColor;
        style.font = defaultFont;

        return style;
    }

    public static Label.LabelStyle getMediumLabelStyle() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.fontColor = defaultFontColor;
        style.font = mediumFont;

        return style;
    }

    public static Label.LabelStyle getLargerLabelStyle() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.fontColor = defaultFontColor;
        style.font = largeFont;

        return style;
    }

    public static NinePatch getDefaultBackgroundPatch() {
        return atlas.createPatch("color4_down");
    }

    public static NinePatch getDarkerBackgroundPatch() {
        return atlas.createPatch("color5_down");
    }

    public static ProgressBar.ProgressBarStyle getProgressBarStyle(Color fillColor) {
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();

        style.background = UIFactory.createDefaultBackgroundDrawable();
        style.background.setMinHeight(30f);
        style.background.setMinWidth(500f);

        Pixmap pm = new Pixmap(10, 16, Pixmap.Format.RGBA8888);
        pm.setColor(fillColor);
        pm.fill();
        Texture whiteTex = new Texture(pm);
        pm.dispose();

        style.knob = new TextureRegionDrawable(whiteTex);
        style.knob.setMinWidth(0);

        TextureRegionDrawable fillDrawable = new TextureRegionDrawable(whiteTex);
        fillDrawable.setMinWidth(0);
        fillDrawable.setLeftWidth(0);
        fillDrawable.setRightWidth(0);
        fillDrawable.setTopHeight(0);
        fillDrawable.setBottomHeight(0);
        style.knobBefore = fillDrawable;

        return style;
    }
}
