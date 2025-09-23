package views.ranch.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import data.horse.HorseType;
import data.shop.PurchaseResult;
import data.shop.ShopItem;
import data.shop.ShopResponse;
import services.shop.ShopServiceImpl;
import views.common.HorseFactory;
import views.common.ListPanel;
import views.common.UIFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ShopPanel extends ListPanel<ShopItem> {
    private Consumer<PurchaseResult> onPurchaseComplete;

    private final ShopServiceImpl shopService;
    private TextButton buyButton;
    private TextButton upgradeButton;

    public ShopPanel(float width, float height) {
        super(width, height);
        this.shopService = new ShopServiceImpl();
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

        if(item.isOwned()) row.setBackground(UIFactory.createDarkerBackgroundDrawable());
        else row.setBackground(UIFactory.createDefaultBackgroundDrawable());

        row.add(HorseFactory.createHorseImage(item.getHorseType())).expandX().left().padLeft(20);
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
                for (ShopResponse.HorseToBuy h : response.horses)
                    allItems.add(new ShopItem(HorseType.fromId(h.id), h.price, false, false));

                for (ShopResponse.Upgrade u : response.upgrades)
                    allItems.add(new ShopItem(HorseType.fromId(u.id), u.price,true, true));

                Gdx.app.postRunnable(() -> populateList(allItems));
            } catch (Exception e) {
                Gdx.app.error("Shop", "Failed to fetch shop data", e);
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
                if (result.success) {
                    refreshShop();
                    if (onPurchaseComplete != null) onPurchaseComplete.accept(result);
                } else {
                    Gdx.app.error("Shop", "Horse purchase failed: " + result.message);
                }
            });
        }).start();
    }

    private void sendUpgradeRequest(int horseId) {
        new Thread(() -> {
            PurchaseResult result = shopService.postUpgradePurchase(horseId);
            Gdx.app.postRunnable(() -> {
                if (result.success) {
                    refreshShop();
                    if (onPurchaseComplete != null) onPurchaseComplete.accept(result);
                } else {
                    Gdx.app.error("Shop", "Upgrade purchase failed: " + result.message);
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
