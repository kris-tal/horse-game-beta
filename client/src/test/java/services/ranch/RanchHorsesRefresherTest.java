package services.ranch;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import data.horse.HorseData;
import data.race.EquippedHorseService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.horse.HorseService;
import views.ranch.horses.HorseActors;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RanchHorsesRefresherTest {

    @BeforeAll
    static void mockGdxApp() {
        Application app = mock(Application.class);
        doAnswer(inv -> {
            Runnable r = inv.getArgument(0);
            r.run();
            return null;
        }).when(app).postRunnable(any());
        Gdx.app = app;
    }

    @AfterAll
    static void clearGdx() {
        Gdx.app = null;
    }

    private static HorseData horse(int id) {
        HorseData h = mock(HorseData.class);
        when(h.getId()).thenReturn(id);
        return h;
    }

    @Test
    void initialLoad_callsInit_andEquipsMatchingHorse() {

        HorseService horseService = mock(HorseService.class);
        EquippedHorseService equipped = mock(EquippedHorseService.class);
        HorseActors actors = mock(HorseActors.class);

        HorseData h1 = horse(1);
        HorseData h2 = horse(2);
        when(horseService.getUserHorses()).thenReturn(List.of(h1, h2));
        when(equipped.getEquippedHorse()).thenReturn(h2);

        RanchHorsesRefresher refresher = new RanchHorsesRefresher(horseService, equipped);


        refresher.initialLoad(actors);


        verify(actors).init(List.of(h1, h2));
        verify(equipped).getEquippedHorse();
        verify(equipped).equipHorse(h2);
        verifyNoMoreInteractions(actors, equipped);
    }

    @Test
    void refreshAsync_fetches_latest_andSyncs_onAppThread() throws Exception {

        HorseService horseService = mock(HorseService.class);
        EquippedHorseService equipped = mock(EquippedHorseService.class);
        HorseActors actors = mock(HorseActors.class);

        HorseData h3 = horse(3);
        when(horseService.getUserHorses()).thenReturn(List.of(h3));
        when(equipped.getEquippedHorse()).thenReturn(h3);

        CountDownLatch applied = new CountDownLatch(1);
        doAnswer(inv -> {
            applied.countDown();
            return null;
        }).when(equipped).equipHorse(h3);

        RanchHorsesRefresher refresher = new RanchHorsesRefresher(horseService, equipped);


        refresher.refreshAsync(actors);


        assertTrue(applied.await(2, TimeUnit.SECONDS), "Async refresh did not complete");
        verify(actors).syncWith(List.of(h3));
        verify(equipped).getEquippedHorse();
        verify(equipped).equipHorse(h3);
        verifyNoMoreInteractions(actors, equipped);
    }

    @Test
    void syncEquipped_whenEquippedIsNull_callsEnsure() {

        HorseService horseService = mock(HorseService.class);
        EquippedHorseService equipped = mock(EquippedHorseService.class);
        HorseActors actors = mock(HorseActors.class);

        HorseData h1 = horse(1);
        when(horseService.getUserHorses()).thenReturn(List.of(h1));
        when(equipped.getEquippedHorse()).thenReturn(null);

        RanchHorsesRefresher refresher = new RanchHorsesRefresher(horseService, equipped);


        refresher.initialLoad(actors);


        verify(actors).init(List.of(h1));
        verify(equipped).getEquippedHorse();
        verify(equipped).ensureEquippedHorse();
        verifyNoMoreInteractions(actors, equipped);
    }

    @Test
    void syncEquipped_whenEquippedMissing_callsEnsure() {

        HorseService horseService = mock(HorseService.class);
        EquippedHorseService equipped = mock(EquippedHorseService.class);
        HorseActors actors = mock(HorseActors.class);

        HorseData h1 = horse(1);
        HorseData h2 = horse(2);
        when(horseService.getUserHorses()).thenReturn(List.of(h1, h2));

        HorseData equippedHorse = horse(999); // not present in latest
        when(equipped.getEquippedHorse()).thenReturn(equippedHorse);

        RanchHorsesRefresher refresher = new RanchHorsesRefresher(horseService, equipped);


        refresher.initialLoad(actors);


        verify(actors).init(List.of(h1, h2));
        verify(equipped).getEquippedHorse();
        verify(equipped).ensureEquippedHorse();
        verify(equipped, never()).equipHorse(any());
        verifyNoMoreInteractions(actors, equipped);
    }
}