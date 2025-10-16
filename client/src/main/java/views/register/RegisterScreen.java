package views.register;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import core.HorseGame;
import data.race.EquippedHorseService;
import services.auth.AuthServiceImpl;
import services.managers.ConnectionManagerPort;
import services.managers.ResourceManager;
import services.managers.SessionManager;
import services.managers.SessionManagerPort;
import services.media.AudioService;
import views.common.ui.UIFactory;
import views.login.LoginScreen;
import views.ranch.RanchScreen;

public class RegisterScreen implements Screen, RegisterView {
    private final HorseGame game;
    private final RegisterPresenter presenter;
    private final SessionManagerPort sessionManager;
    private final ConnectionManagerPort connectionManager;
    private final AudioService audio;
    private Stage stage;

    public RegisterScreen(HorseGame game, ConnectionManagerPort connectionManager, AudioService audio) {
        this.game = game;
        this.sessionManager = new SessionManager();
        this.connectionManager = connectionManager;
        this.presenter = new RegisterPresenter(this, new AuthServiceImpl(), sessionManager);
        this.audio = audio;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float bigPadding = screenHeight / 10f;
        float smallPadding = screenHeight / 40f;

        Texture bgTexture = ResourceManager.authScreenBgTexture;
        Texture logoTexture = ResourceManager.logoTexture;

        Image background = new Image(bgTexture);
        background.setFillParent(true);
        stage.addActor(background);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Image logo = new Image(logoTexture);
        logo.setScaling(Scaling.fit);
        float logoWidth = screenWidth / 2f;
        float logoHeight = logoTexture.getHeight() * (logoWidth / logoTexture.getWidth());

        TextField usernameField = UIFactory.createTextField("username");
        TextField passwordField = UIFactory.createTextField("password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextField repeatPasswordField = UIFactory.createTextField("repeat password");
        repeatPasswordField.setPasswordMode(true);
        repeatPasswordField.setPasswordCharacter('*');

        TextButton registerButton = UIFactory.createTextButton("register!");
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                presenter.onRegisterClicked(
                        usernameField.getText(),
                        passwordField.getText(),
                        repeatPasswordField.getText()
                );
            }
        });

        Label loginLabel = UIFactory.createLabel("Actually I do have an account..");
        loginLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                presenter.onLoginLabelClicked();
            }
        });
        loginLabel.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
            }
        });

        usernameField.setTextFieldListener((textField, c) -> {
            if (c == '\r' || c == '\n') {
                textField.getStage().setKeyboardFocus(passwordField);
            }
        });
        passwordField.setTextFieldListener((textField, c) -> {
            if (c == '\r' || c == '\n') {
                textField.getStage().setKeyboardFocus(repeatPasswordField);
            }
        });
        repeatPasswordField.setTextFieldListener((textField, c) -> {
            if (c == '\r' || c == '\n') {
                presenter.onRegisterClicked(usernameField.getText(), passwordField.getText(), repeatPasswordField.getText());
            }
        });

        float fieldWidth = Gdx.graphics.getWidth() / 3f;
        float fieldHeight = Gdx.graphics.getHeight() / 20f;

        root.top();
        root.padTop(bigPadding);
        root.add(logo).width(logoWidth).height(logoHeight).padBottom(bigPadding).row();
        root.add(usernameField).width(fieldWidth).height(fieldHeight).padBottom(smallPadding).row();
        root.add(passwordField).width(fieldWidth).height(fieldHeight).padBottom(smallPadding).row();
        root.add(repeatPasswordField).width(fieldWidth).height(fieldHeight).padBottom(smallPadding).row();
        root.add(registerButton).width(fieldWidth / 4).height(fieldHeight / 2).padBottom(smallPadding).row();
        root.add(loginLabel).width(fieldWidth).padTop(smallPadding).row();
    }

    @Override
    public void showRegisterSuccess() {
        audio.playSuccessSound();
    }

    @Override
    public void showRegisterFailure() {
        audio.playFailureSound();
    }

    @Override
    public void navigateToLogin() {
        game.setScreen(new LoginScreen(game, connectionManager, audio));
    }

    @Override
    public void navigateToRanch() {
        game.setScreen(new RanchScreen(game, sessionManager, connectionManager, new EquippedHorseService(sessionManager)));
    }

    @Override
    public void exitGame() {
        Gdx.app.exit();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }
}
