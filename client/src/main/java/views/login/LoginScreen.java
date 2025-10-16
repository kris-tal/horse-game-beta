// java
package views.login;

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
import views.ranch.RanchScreen;
import views.register.RegisterScreen;

public class LoginScreen implements Screen, LoginView {
    private final HorseGame game;
    private final LoginPresenter presenter;
    private final ConnectionManagerPort connectionManager;
    private final SessionManagerPort sessionManager;
    private final AudioService audio;
    private Stage stage;

    public LoginScreen(HorseGame game, ConnectionManagerPort connectionManager, AudioService audio) {
        this.game = game;
        this.sessionManager = new SessionManager();
        this.connectionManager = connectionManager;
        this.presenter = new LoginPresenter(this, new AuthServiceImpl(), sessionManager);
        this.audio = audio;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float bigPadding = screenHeight / 10f;
        float mediumPadding = screenHeight / 20f;
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

        TextButton loginButton = UIFactory.createTextButton("log in");
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                presenter.onLoginClicked(usernameField.getText(), passwordField.getText());
            }
        });

        Label registerLabel = UIFactory.createLabel("I don't have an account..");
        registerLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                presenter.onRegisterClicked();
            }
        });
        registerLabel.addListener(new InputListener() {
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
                presenter.onLoginClicked(usernameField.getText(), passwordField.getText());
            }
        });

        float fieldWidth = Gdx.graphics.getWidth() / 3f;
        float fieldHeight = Gdx.graphics.getHeight() / 20f;

        root.top();
        root.padTop(bigPadding);
        root.add(logo).width(logoWidth).height(logoHeight).padBottom(bigPadding).row();
        root.add(usernameField).width(fieldWidth).height(fieldHeight).padBottom(mediumPadding).row();
        root.add(passwordField).width(fieldWidth).height(fieldHeight).padBottom(mediumPadding).row();
        root.add(loginButton).width(fieldWidth / 4).height(fieldHeight / 2).padBottom(smallPadding).row();
        root.add(registerLabel).width(fieldWidth).padTop(smallPadding).row();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            presenter.onExitPressed();
        }
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void showLoginSuccess() {
        audio.playSuccessSound();
    }

    @Override
    public void showLoginFailure() {
        audio.playFailureSound();
    }

    @Override
    public void navigateToRegister() {
        game.setScreen(new RegisterScreen(game, connectionManager, audio));
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
