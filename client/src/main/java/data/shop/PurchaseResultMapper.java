package data.shop;

import com.google.gson.Gson;

public class PurchaseResultMapper {
    private static final Gson gson = new Gson();

    public static PurchaseResult fromJson(String json) {
        return gson.fromJson(json, PurchaseResult.class);
    }

    public static String toJson(PurchaseResult result) {
        return gson.toJson(result);
    }
}
