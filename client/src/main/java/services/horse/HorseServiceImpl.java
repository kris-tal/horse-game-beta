package services.horse;

import com.google.gson.Gson;
import data.horse.HorseData;
import data.horse.HorseType;
import data.horse.HorsesResponse;
import services.managers.SessionManagerPort;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HorseServiceImpl implements HorseService {
    private final Gson gson;
    private final SessionManagerPort sessionManager;

    public HorseServiceImpl(SessionManagerPort sessionManager) {
        this.gson = new Gson();
        this.sessionManager = sessionManager;
    }

    @Override
    public List<HorseData> getUserHorses() {
        HttpRequest request = sessionManager.getProtectedRequestsService()
                .ProtectedGet("/horses")
                .build();
        try {
            HttpResponse<String> result = sessionManager.getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            if (result.statusCode() == 200) {
                HorsesResponse response = gson.fromJson(result.body(), HorsesResponse.class);
                return response.getHorses();
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }
        return null;
    }

    @Override
    public HorseData getHorseDataById(int horseId) {
        HttpRequest request = sessionManager.getProtectedRequestsService()
                .ProtectedGet(String.format("/horses/id/%d", horseId))
                .build();
        try {
            HttpResponse<String> result = sessionManager.getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            if (result.statusCode() == 200) {
                return gson.fromJson(result.body(), HorseData.class);
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }
        return null;
    }

    @Override
    public HorseData getHorseData(HorseType type) {
        HttpRequest request = sessionManager.getProtectedRequestsService()
                .ProtectedGet(String.format("/horses/name/%d", type.getId()))
                .build();
        try {
            HttpResponse<String> result = sessionManager.getProtectedRequestsService()
                    .GetHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            if (result.statusCode() == 200) {
                return gson.fromJson(result.body(), HorseData.class);
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }
        return null;
    }
}