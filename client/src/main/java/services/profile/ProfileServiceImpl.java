package services.profile;

import com.google.gson.Gson;
import data.ProfileData;
import services.managers.SessionManagerPort;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ProfileServiceImpl implements ProfileService {
    private String username;
    private final Gson gson;
    private final SessionManagerPort sessionManager;

    public ProfileServiceImpl(SessionManagerPort sessionManager) {
        this.gson = new Gson();
        this.sessionManager = sessionManager;
    }

    @Override
    public ProfileData getUserData() {
        HttpRequest request = sessionManager.getProtectedRequestsService()
                .ProtectedGet("/profile")
                .build();
        try {
            HttpResponse<String> result = sessionManager.getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            if (result.statusCode() == 200) {
                ProfileData data =  gson.fromJson(result.body(), ProfileData.class);
                username =  data.getUsername();
                return data;
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }
        return null;
    }
    @Override
    public String getUsername() {
        if(username == null) return getUserData().getUsername();
        return username;
    }
}