package services.ranch;

import com.badlogic.gdx.Gdx;
import data.horse.HorseData;
import data.race.EquippedHorseService;
import services.horse.HorseService;
import services.horse.HorseServiceImpl;
import services.managers.SessionManagerPort;
import views.ranch.horses.HorseActors;

import java.util.List;

public class RanchHorsesRefresher {
    private final HorseService horseService;
    private final EquippedHorseService equippedHorseService;

    public RanchHorsesRefresher(SessionManagerPort sessionManager, EquippedHorseService equippedHorseService) {
        this(new HorseServiceImpl(sessionManager), equippedHorseService);
    }

    public RanchHorsesRefresher(HorseService horseService, EquippedHorseService equippedHorseService) {
        this.horseService = horseService;
        this.equippedHorseService = equippedHorseService;
    }

    public void initialLoad(HorseActors actors) {
        List<HorseData> horses = horseService.getUserHorses();
        actors.init(horses);
        syncEquipped(horses);
    }

    public void refreshAsync(HorseActors actors) {
        new Thread(() -> {
            List<HorseData> latest = horseService.getUserHorses();
            if (latest == null) return;
            Gdx.app.postRunnable(() -> {
                actors.syncWith(latest);
                syncEquipped(latest);
            });
        }, "RanchHorsesRefresh").start();
    }

    private void syncEquipped(List<HorseData> latest) {
        if (latest == null || latest.isEmpty()) return;
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