package services.validation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class JsonResponseValidator implements ResponseValidator {
    private final Gson gson;

    public JsonResponseValidator() {
        this.gson = new Gson();
    }

    @Override
    public boolean isValidJson(String responseBody) {
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return false;
        }
        if (responseBody.trim().startsWith("<") || responseBody.contains("<!DOCTYPE") || responseBody.contains("<html")) {
            return false;
        }

        try {
            gson.fromJson(responseBody, Object.class);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    @Override
    public boolean isSuccessfulResponse(String responseBody) {
        if (!isValidJson(responseBody)) {
            return false;
        }

        try {
            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
            if (jsonObject.has("success")) {
                return jsonObject.get("success").getAsBoolean();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extractErrorMessage(String responseBody) {
        if (!isValidJson(responseBody)) {
            return "Invalid JSON response: " + responseBody;
        }

        try {
            JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
            if (jsonObject.has("msg")) {
                return jsonObject.get("msg").getAsString();
            }
            if (jsonObject.has("message")) {
                return jsonObject.get("message").getAsString();
            }
            return "Unknown error in response";
        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }
}
