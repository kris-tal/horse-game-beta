package data.shop;

import com.google.gson.Gson;

public class ShopResponseMapper {
    private static final Gson gson = new Gson();

    public static ShopResponse fromJson(String json) {
        return gson.fromJson(json, ShopResponse.class);
    }

    public static String toJson(ShopResponse result) {
        return gson.toJson(result);
    }
}
