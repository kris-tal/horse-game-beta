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
import services.auth.AuthServiceImpl;
import views.common.UIFactory;
import views.common.UIStyles;
import services.managers.ResourceManager;
import views.login.LoginScreen;
import views.ranch.RanchScreen;

public class RegisterScreen implements Screen, RegisterView {
    private final HorseGame game;
    private final RegisterPresenter presenter;
    private Stage stage;

    public RegisterScreen(HorseGame game) {
        this.game = game;
        this.presenter = new RegisterPresenter(this, new AuthServiceImpl());
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

        //loading assets
        Texture bgTexture = ResourceManager.authScreenBgTexture;
        Texture logoTexture = ResourceManager.logoTexture;

        //staging bg
        Image background = new Image(bgTexture);
        background.setFillParent(true);
        stage.addActor(background);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        //staging logo
        Image logo = new Image(logoTexture);
        logo.setScaling(Scaling.fit);
        float logoWidth = screenWidth / 2f;
        float logoHeight = logoTexture.getHeight() * (logoWidth / logoTexture.getWidth());

        //username / password field
        TextField usernameField = UIFactory.createTextField("username");

        TextField passwordField = UIFactory.createTextField("password");

        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextField repeatPasswordField = UIFactory.createTextField("repeat password");
        repeatPasswordField.setPasswordMode(true);
        repeatPasswordField.setPasswordCharacter('*');

        //register button
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

        // Jesteśmy w polu username + klikamy Enter = skaczemy do pola password
        usernameField.setTextFieldListener((textField, c) -> {
            if (c == '\r' || c == '\n') {
                textField.getStage().setKeyboardFocus(passwordField);
            }
        });

        // Jesteśmy w polu password + klikamy Enter = skaczemy do pola repeat_password
        passwordField.setTextFieldListener((textField, c) -> {
            if (c == '\r' || c == '\n') {
                textField.getStage().setKeyboardFocus(repeatPasswordField);
            }
        });

        // Jesteśmy w polu repeat_password + klikamy Enter = próba rejestracji
        repeatPasswordField.setTextFieldListener((textField, c) -> {
            if (c == '\r' || c == '\n') {
                presenter.onRegisterClicked(usernameField.getText(), passwordField.getText(), repeatPasswordField.getText());
            }
        });

        float fieldWidth = Gdx.graphics.getWidth() / 3f;
        float fieldHeight = Gdx.graphics.getHeight() / 20f;

        //layout
        root.top();
        root.padTop(bigPadding);
        root.add(logo).width(logoWidth).height(logoHeight).padBottom(bigPadding).row();
        root.add(usernameField).width(fieldWidth).height(fieldHeight).padBottom(smallPadding).row();
        root.add(passwordField).width(fieldWidth).height(fieldHeight).padBottom(smallPadding).row();
        root.add(repeatPasswordField).width(fieldWidth).height(fieldHeight).padBottom(smallPadding).row();
        root.add(registerButton).width(fieldWidth / 4).height(fieldHeight / 2).padBottom(smallPadding).row();
        root.add(loginLabel).width(fieldWidth).padTop(smallPadding).row();
    }

    // LoginView methods
    @Override
    public void showRegisterFailure() {
        ResourceManager.snortSound.play();
    }

    @Override
    public void showRegisterSuccess() {
        ResourceManager.horseSound1.play();
    }

    @Override
    public void navigateToLogin() {
        game.setScreen(new LoginScreen(game));
    }

    @Override
    public void navigateToRanch() {
        game.setScreen(new RanchScreen(game));
    }

    @Override
    public void exitGame() {
        Gdx.app.exit();
    }

    // LibGDX things

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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }
}
