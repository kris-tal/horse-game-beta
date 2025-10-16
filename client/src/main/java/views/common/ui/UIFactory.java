package views.common.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import services.managers.ResourceManager;

public class UIFactory {
    //buttons

    public static TextButton createTextButton(String text) {
        TextButton button = new TextButton(text, UIStyles.getDefaultButtonStyle());
        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });
        return button;
    }

    public static TextButton createLargeTextButton(String text) {
        TextButton button = createTextButton(text);
        button.setTransform(true);
        button.setOrigin(Align.center);
        button.setScale(1.5f);

        return button;
    }

    //labels
    public static Label createLabel(String text) {
        return new Label(text, UIStyles.getDefaultLabelStyle());
    }

    public static Label createMediumLabel(String text) {
        return new Label(text, UIStyles.getMediumLabelStyle());
    }

    public static Label createLargeLabel(String text) {
        return new Label(text, UIStyles.getLargerLabelStyle());
    }

    //text fields
    public static TextField createTextField(String message) {
        TextField.TextFieldStyle style = UIStyles.getTextFieldStyle();

        TextField textField = new TextField("", style);
        textField.setMessageText(message);
        textField.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Ibeam);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });
        return textField;
    }

    public static NinePatchDrawable createDefaultBackgroundDrawable() {
        return new NinePatchDrawable(UIStyles.getDefaultBackgroundPatch());
    }

    public static NinePatchDrawable createDarkerBackgroundDrawable() {
        return new NinePatchDrawable(UIStyles.getDarkerBackgroundPatch());
    }

    public static Image createCoinImage() {
        TextureRegion coinRegion = ResourceManager.iconsAtlas.findRegion("coin_icon");      //idk czy to tu powinno byc (skonsultuj)
        return new Image(new TextureRegionDrawable(coinRegion));
    }

    public static Image createPrestigeImage() {
        TextureRegion prestigeRegion = ResourceManager.iconsAtlas.findRegion(("prestige_icon"));
        return new Image(new TextureRegionDrawable(prestigeRegion));
    }

    public static ProgressBar createProgressBar(float min, float max, float stepSize, Color color) {
        return new ProgressBar(min, max, stepSize, false, UIStyles.getProgressBarStyle(color));
    }
}
