package data.race;

import data.horse.HorseData;
import services.horse.HorseService;
import services.horse.HorseServiceImpl;
import services.managers.SessionManagerPort;

import java.util.List;
import java.util.Objects;

public class EquippedHorseService {
    private final HorseService horseService;
    private HorseData equippedHorse;

    public EquippedHorseService(SessionManagerPort sessionManager) {
        this(new HorseServiceImpl(Objects.requireNonNull(sessionManager, "sessionManager")));
    }

    public EquippedHorseService(HorseService horseService) {
        this.horseService = Objects.requireNonNull(horseService, "horseService");
    }

    public HorseData getEquippedHorse() {
        return equippedHorse;
    }

    public void equipHorse(HorseData horse) {
        this.equippedHorse = horse;
    }

    public boolean isEquipped(HorseData horseData) {
        return equippedHorse != null
                && horseData != null
                && equippedHorse.getId() == horseData.getId();
    }

    public HorseData ensureEquippedHorse() {
        List<HorseData> horses = horseService.getUserHorses();
        if (horses == null || horses.isEmpty()) {
            equippedHorse = null;
            throw new IllegalStateException("No horses available to equip.");
        }
        if (equippedHorse == null ||
                horses.stream().noneMatch(h -> h.getId() == equippedHorse.getId())) {
            equippedHorse = horses.get(0);
        }
        return equippedHorse;
    }
}