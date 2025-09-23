package services.horse;

import com.google.gson.Gson;
import data.horse.HorseData;
import data.horse.HorseType;
import data.horse.HorsesResponse;
import services.managers.SessionManager;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HorseServiceImpl implements HorseService {
    private final Gson gson;
    public HorseServiceImpl() {
        gson = new Gson();
    }
    @Override
    public List<HorseData> getUserHorses() {
        HttpRequest request = SessionManager.getInstance()
                .getProtectedRequestsService().ProtectedGet("/horses").build();
        try {
            HttpResponse<String> result =
                    SessionManager.getInstance().getProtectedRequestsService()
                            .GetHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if(result.statusCode() == 200) {
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
        HttpRequest request = SessionManager.getInstance()
                .getProtectedRequestsService().ProtectedGet(String.format("/horses/id/%d", horseId)).build();
        try {
            HttpResponse<String> result =
                    SessionManager.getInstance().getProtectedRequestsService()
                            .GetHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if(result.statusCode() == 200) {
                HorseData response = gson.fromJson(result.body(), HorseData.class);
                return response;
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }
        return null;
    }

    @Override
    public HorseData getHorseData(HorseType type) {
        HttpRequest request = SessionManager.getInstance()
                .getProtectedRequestsService().ProtectedGet(String.format("/horses/name/%d", type.getId())).build();
        try {
            HttpResponse<String> result =
                    SessionManager.getInstance().getProtectedRequestsService()
                            .GetHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if(result.statusCode() == 200) {
                HorseData response = gson.fromJson(result.body(), HorseData.class);
                return response;
            }
        } catch (IOException | InterruptedException e) {
            return null;
        }
        return null;
    }
}
