package services.horse;

import data.horse.HorseData;
import data.horse.HorseType;

import java.util.List;

public interface HorseService {
    List<HorseData> getUserHorses();

    HorseData getHorseDataById(int horseId);

    HorseData getHorseData(HorseType type);
}