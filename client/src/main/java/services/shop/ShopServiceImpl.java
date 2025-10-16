package services.shop;

import com.google.gson.Gson;
import data.shop.PurchaseResult;
import data.shop.PurchaseType;
import data.shop.ShopResponse;
import services.managers.SessionManagerPort;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ShopServiceImpl implements ShopService {

    private final Gson gson;
    private final SessionManagerPort sessionManager;

    public ShopServiceImpl(SessionManagerPort sessionManager) {
        this.gson = new Gson();
        this.sessionManager = sessionManager;
    }

    @Override
    public ShopResponse getShopData() {
        HttpRequest request = sessionManager.getProtectedRequestsService()
                .ProtectedGet("/shop")
                .build();

        try {
            HttpResponse<String> response = sessionManager.getProtectedRequestsService()
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
        return sendPurchaseRequest(horseId, PurchaseType.HORSE);
    }

    @Override
    public PurchaseResult postUpgradePurchase(int horseId) {
        return sendPurchaseRequest(horseId, PurchaseType.UPGRADE);
    }


    private PurchaseResult sendPurchaseRequest(int horseId, PurchaseType type) {
        String requestBody = gson.toJson(Map.of("id", horseId));

        HttpRequest request = sessionManager.getProtectedRequestsService()
                .ProtectedPost("/shop",
                        HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = sessionManager.getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            if (status == 200 || status == 400 || status == 401 || status == 404) {
                PurchaseResult result = gson.fromJson(response.body(), PurchaseResult.class);
                return new PurchaseResult(result.getMoney(), result.isSuccess(), result.getMessage(), type == PurchaseType.UPGRADE);
            }
        } catch (IOException | InterruptedException e) {
            return new PurchaseResult(0, false, "Request failed: " + e.getMessage(), type == PurchaseType.UPGRADE);
        }
        return new PurchaseResult(0, false, "Unknown error", type == PurchaseType.UPGRADE);
    }

}
