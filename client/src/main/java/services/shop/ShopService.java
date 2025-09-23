package services.shop;

import data.shop.PurchaseResult;
import data.shop.ShopResponse;

public interface ShopService {
    ShopResponse getShopData();
    PurchaseResult postHorsePurchase(int horseId);
    PurchaseResult postUpgradePurchase(int horseId);
}
