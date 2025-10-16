// java
package views.ranch.shop;


import animations.IdleHorseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import data.horse.HorseType;
import data.horse.HorseTypeRegistry;
import data.shop.PurchaseResult;
import data.shop.ShopItem;
import data.shop.ShopResponse;
import services.managers.SessionManagerPort;
import services.shop.ShopServiceImpl;
import views.common.ui.ListPanel;
import views.common.ui.UIFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ShopPanel extends ListPanel<ShopItem> {
    private Consumer<PurchaseResult> onPurchaseComplete;

    private final ShopServiceImpl shopService;
    private TextButton buyButton;
    private TextButton upgradeButton;

    public ShopPanel(float width, float height, SessionManagerPort sessionManager) {
        super(width, height);
        this.shopService = new ShopServiceImpl(sessionManager);
        refreshShop();
    }

    private void createShopControls() {
        buyButton = UIFactory.createLargeTextButton("buy");
        upgradeButton = UIFactory.createLargeTextButton("upgrade");

        buyButton.setVisible(false);
        upgradeButton.setVisible(false);

        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedItem != null) onBuyButtonClicked(selectedItem);
            }
        });

        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedItem != null) onUpgradeButtonClicked(selectedItem);
            }
        });

        Stack buttonStack = new Stack();

        buttonStack.add(buyButton);
        buttonStack.add(upgradeButton);

        buyButton.setVisible(false);
        upgradeButton.setVisible(false);

        content.add(buttonStack).padTop(defaultPadding).center().row();
    }

    @Override
    protected String getTitle() {
        return "HORSE SHOP";
    }

    @Override
    protected void addContent(float width, float height) {
        super.addContent(width, height);
        createShopControls();
    }

    @Override
    protected void populateRow(Table row, ShopItem item) {
        row.pad(5);
        row.setTouchable(Touchable.enabled);

        if (item.isOwned()) row.setBackground(UIFactory.createDarkerBackgroundDrawable());
        else row.setBackground(UIFactory.createDefaultBackgroundDrawable());

        row.add(new IdleHorseActor(item.getHorseType(), 1)).expandX().left().padLeft(20);
        row.add(UIFactory.createLabel(item.getName())).expandX().left().pad(10);
        row.add(UIFactory.createLabel(String.valueOf(item.getPrice()))).right().pad(10);

        row.add(UIFactory.createCoinImage()).right().padRight(20);
    }

    @Override
    protected void onItemSelected(ShopItem item, Table row) {
        super.onItemSelected(item, row);
        buyButton.setVisible(!item.isOwned() && !item.isUpgrade());
        upgradeButton.setVisible(item.isOwned() || item.isUpgrade());
    }

    private void refreshShop() {
        new Thread(() -> {
            try {
                ShopResponse response = shopService.getShopData();
                List<ShopItem> allItems = new ArrayList<>();

                for (ShopResponse.HorseToBuy h : response.getHorses()) {
                    HorseType type = HorseTypeRegistry.getById(h.getId());
                    if (type != null) {
                        allItems.add(new ShopItem(type, h.getPrice(), false, false));
                    }
                }

                for (ShopResponse.Upgrade u : response.getUpgrades()) {
                    HorseType type = HorseTypeRegistry.getById(u.getId());
                    if (type != null) {
                        allItems.add(new ShopItem(type, u.getPrice(), true, true));
                    }
                }

                Gdx.app.postRunnable(() -> populateList(allItems));
            } catch (Exception ignored) {
            }
        }).start();
    }

    public void setOnPurchaseComplete(Consumer<PurchaseResult> callback) {
        this.onPurchaseComplete = callback;
    }

    private void sendBuyHorseRequest(int horseId) {
        new Thread(() -> {
            PurchaseResult result = shopService.postHorsePurchase(horseId);
            Gdx.app.postRunnable(() -> {
                if (result.isSuccess()) {
                    refreshShop();
                    if (onPurchaseComplete != null) onPurchaseComplete.accept(result);
                }
            });
        }).start();
    }

    private void sendUpgradeRequest(int horseId) {
        new Thread(() -> {
            PurchaseResult result = shopService.postUpgradePurchase(horseId);
            Gdx.app.postRunnable(() -> {
                if (result.isSuccess()) {
                    refreshShop();
                    if (onPurchaseComplete != null) onPurchaseComplete.accept(result);
                }
            });
        }).start();
    }

    public void onBuyButtonClicked(ShopItem item) {
        if (!item.isOwned() && !item.isUpgrade()) {
            sendBuyHorseRequest(item.getHorseType().getId());
        }
    }

    public void onUpgradeButtonClicked(ShopItem item) {
        if (item.isOwned() || item.isUpgrade()) {
            sendUpgradeRequest(item.getHorseType().getId());
        }
    }
}