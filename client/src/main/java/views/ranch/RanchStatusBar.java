// java
package views.ranch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import core.HorseGame;
import data.ProfileData;
import services.managers.SessionManagerPort;
import services.profile.ProfileService;
import services.profile.ProfileServiceImpl;
import views.common.ui.UIFactory;
import views.common.ui.UIStyles;

public class RanchStatusBar extends Table {

    private PlayerStatusItem playerStatusItem;

    public enum Action {RANKING, SHOP, RACE}

    public interface StatusBarListener {
        void onStatusBar(Action action);
    }

    private StatusBarListener listener;

    private final HorseGame game;
    private final SessionManagerPort sessionManager;

    public static class PlayerStatusItem extends Table {

        private final ProfileService service;
        private final Label usernameLabel;
        private final Label moneyLabel;

        public PlayerStatusItem(SessionManagerPort sessionManager) {
            super();
            this.service = new ProfileServiceImpl(sessionManager);
            setBackground(new NinePatchDrawable(UIStyles.getDefaultBackgroundPatch()));

            usernameLabel = UIFactory.createLabel("");
            moneyLabel = UIFactory.createLabel("0");

            pad(5).padLeft(10).padRight(10);

            add(usernameLabel).padRight(20).left();
            add(UIFactory.createCoinImage()).size(32, 32).padRight(5);
            add(moneyLabel).padRight(20);

            setTransform(true);
            setOrigin(Align.center);
            setScale(1.5f);
            loadData();
        }

        public void loadData() {
            new Thread(() -> {
                try {
                    ProfileData response = service.getUserData();
                    Gdx.app.postRunnable(() -> {
                        updateMoney(response.getMoney());
                        setUsername(response.getUsername());
                    });
                } catch (Exception ignored) {
                }
            }).start();
        }

        public void setUsername(String username) {
            usernameLabel.setText(username);
        }

        public void updateMoney(int newBalance) {
            moneyLabel.setText(String.valueOf(newBalance));
        }
    }

    private final Table leftStack;
    private final Table rightStack;

    public RanchStatusBar(HorseGame game, SessionManagerPort sessionManager) {
        super();
        this.game = game;
        this.sessionManager = sessionManager;

        this.leftStack = new Table();
        this.rightStack = new Table();

        setup();
    }

    private void setup() {
        setFillParent(true);
        top();
        padTop(40);
        padLeft(20);
        padRight(60);

        leftStack.left();
        rightStack.right();

        add(leftStack).expandX().fillX().left();
        add(rightStack).expandX().fillX().right();

        addItems();
    }

    private void addItems() {
        playerStatusItem = new PlayerStatusItem(sessionManager);
        addToLeft(playerStatusItem);

        TextButton shopButton = UIFactory.createLargeTextButton("shop");
        shopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) {
                    listener.onStatusBar(Action.SHOP);
                }
            }
        });

        TextButton raceButton = UIFactory.createLargeTextButton("race!");
        raceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listener != null) {
                    listener.onStatusBar(Action.RACE);
                }
            }
        });

        addToRight(shopButton);
        addToRight(raceButton);
    }

    private void addToLeft(Actor actor) {
        leftStack.add(actor).padRight(40).padLeft(10).padTop(10);
    }

    private void addToRight(Actor actor) {
        rightStack.add(actor).padLeft(60).padTop(-20);
    }

    public void setListener(StatusBarListener listener) {
        this.listener = listener;
    }

    public void updateMoney(int newBalance) {
        playerStatusItem.updateMoney(newBalance);
    }
}