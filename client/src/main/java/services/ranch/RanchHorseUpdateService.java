// java
package services.ranch;

import animations.StrollingHorseActor;
import com.badlogic.gdx.utils.Array;
import data.horse.HorseData;
import data.race.EquippedHorseService;
import services.horse.HorseService;
import services.horse.HorseServiceImpl;
import services.managers.SessionManagerPort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RanchHorseUpdateService {
    private final HorseService horseService;
    private final EquippedHorseService equippedHorseService;

    public RanchHorseUpdateService(SessionManagerPort sessionManager, EquippedHorseService equippedHorseService) {
        this(new HorseServiceImpl(sessionManager), equippedHorseService);
    }

    public RanchHorseUpdateService(HorseService horseService, EquippedHorseService equippedHorseService) {
        this.horseService = horseService;
        this.equippedHorseService = equippedHorseService;
    }

    public void update(Array<StrollingHorseActor> actors) {
        List<HorseData> latest = horseService.getUserHorses();
        if (latest == null) return;
        applyInPlace(actors, latest);
        syncEquipped(latest);
    }

    private void applyInPlace(Array<StrollingHorseActor> actors, List<HorseData> latest) {
        Map<Integer, HorseData> latestById = new HashMap<>();
        for (HorseData h : latest) latestById.put(h.getId(), h);

        for (StrollingHorseActor actor : actors) {
            HorseData updated = latestById.get(actor.getHorseData().getId());
            if (updated != null) {
                actor.setHorseData(updated);
            }
        }
    }

    private void syncEquipped(List<HorseData> latest) {
        HorseData equipped = equippedHorseService.getEquippedHorse();
        if (equipped == null) {
            equippedHorseService.ensureEquippedHorse();
            return;
        }
        for (HorseData h : latest) {
            if (h.getId() == equipped.getId()) {
                equippedHorseService.equipHorse(h);
                return;
            }
        }
        equippedHorseService.ensureEquippedHorse();
    }
}