package services.training;

import com.google.gson.Gson;
import data.auth.AuthCode;
import data.auth.AuthResult;
import data.training.TrainingData;
import data.training.TrainingResponse;
import services.auth.AuthServiceImpl;
import services.managers.SessionManager;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static services.auth.AuthServiceImpl.BASE_URL;
import static services.auth.AuthServiceImpl.REQUEST_TIMEOUT;

public class TrainingServiceImpl implements TrainingService {


    private final Gson gson;
    public TrainingServiceImpl() {
        gson = new Gson();
    }

    @Override
    public TrainingResponse performTraining(TrainingData data) {
        try {
            String requestBody = gson.toJson(data);


            HttpRequest request = SessionManager.getInstance()
                    .getProtectedRequestsService().ProtectedPost("/train",
                            HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8)).build();
            HttpResponse<String> response = SessionManager.getInstance().getProtectedRequestsService().
                    GetHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(response.body(), TrainingResponse.class);

        } catch (Exception e) {
            return null;
        }
    }
}
