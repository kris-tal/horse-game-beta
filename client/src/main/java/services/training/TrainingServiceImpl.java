package services.training;

import com.google.gson.Gson;
import data.training.TrainingData;
import data.training.TrainingResponse;
import services.managers.SessionManagerPort;
import services.validation.JsonResponseValidator;
import services.validation.ResponseValidator;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class TrainingServiceImpl implements TrainingService {
    private final Gson gson;
    private final ResponseValidator responseValidator;
    private final SessionManagerPort sessionManager;

    public TrainingServiceImpl(SessionManagerPort sessionManager) {
        this(sessionManager, new JsonResponseValidator());
    }

    public TrainingServiceImpl(SessionManagerPort sessionManager, ResponseValidator responseValidator) {
        this.gson = new Gson();
        this.responseValidator = responseValidator;
        this.sessionManager = sessionManager;
    }

    @Override
    public TrainingResponse sendTrainingData(TrainingData data) {
        try {
            String requestBody = gson.toJson(data);

            HttpRequest request = sessionManager.getProtectedRequestsService()
                    .ProtectedPost("/train", HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = sessionManager.getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                return new TrainingResponse("{\"success\": false, \"message\": \"HTTP error: " + response.statusCode() + "\"}");
            }

            String responseBody = response.body();
            if (!responseValidator.isValidJson(responseBody)) {
                String errorMessage = responseValidator.extractErrorMessage(responseBody);
                return new TrainingResponse("{\"success\": false, \"message\": \"" + errorMessage + "\"}");
            }

            try {
                return gson.fromJson(responseBody, TrainingResponse.class);
            } catch (Exception e) {
                String safeResponse = "{\"success\": false, \"message\": \"Failed to parse server response\", \"raw_response\": \"" +
                        responseBody.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\"}";
                return new TrainingResponse(safeResponse);
            }
        } catch (Exception e) {
            return new TrainingResponse("{\"success\": false, \"message\": \"Network error: " + e.getMessage() + "\"}");
        }
    }
}
