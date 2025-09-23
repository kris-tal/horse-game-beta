package services.shop;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import data.shop.PurchaseResult;
import data.shop.ShopResponse;
import services.managers.SessionManager;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ShopServiceImpl implements ShopService {

    private final Gson gson;

    public ShopServiceImpl() {
        this.gson = new Gson();
    }

    @Override
    public ShopResponse getShopData() {
        HttpRequest request = SessionManager.getInstance()
                .getProtectedRequestsService()
                .ProtectedGet("/shop")
                .build();

        try {
            HttpResponse<String> response = SessionManager.getInstance()
                    .getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), ShopResponse.class);
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }
        return null;
    }

    @Override
    public PurchaseResult postHorsePurchase(int horseId) {
        return sendPurchaseRequest(horseId);
    }

    @Override
    public PurchaseResult postUpgradePurchase(int horseId) {
        return sendPurchaseRequest(horseId);
    }

    private PurchaseResult sendPurchaseRequest(int horseId) {
        String requestBody = gson.toJson(Map.of("id", horseId));

        Gdx.app.log("purchase", requestBody);
        HttpRequest request = SessionManager.getInstance()
                .getProtectedRequestsService()
                .ProtectedPost("/shop",
                        HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = SessionManager.getInstance()
                    .getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 400
                    || response.statusCode() == 401 || response.statusCode() == 404) {
                return gson.fromJson(response.body(), PurchaseResult.class);
            }
        } catch (IOException | InterruptedException e) {
            return new PurchaseResult(0, false, "Request failed: " + e.getMessage());
        }
        return new PurchaseResult(0, false, "Unknown error");
    }
}
