
package services.ranch;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import animations.StrollingHorseActor;
import data.horse.HorseData;
import data.race.EquippedHorseService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.horse.HorseService;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RanchHorseUpdateServiceTest {

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

    private static StrollingHorseActor actorWith(HorseData data) {
        StrollingHorseActor a = mock(StrollingHorseActor.class);
        when(a.getHorseData()).thenReturn(data);

        doAnswer(inv -> {
            HorseData nd = inv.getArgument(0);
            when(a.getHorseData()).thenReturn(nd);
            return null;
        }).when(a).setHorseData(any());
        return a;
    }

    @Test
    void update_updatesOnlyMatchingActors_andEnsuresEquippedWhenNull() {
        HorseService horseService = mock(HorseService.class);
        EquippedHorseService equipped = mock(EquippedHorseService.class);

        HorseData initial1 = horse(1);
        HorseData initial2 = horse(2);
        StrollingHorseActor actor1 = actorWith(initial1);
        StrollingHorseActor actor2 = actorWith(initial2);
        Array<StrollingHorseActor> actors = new Array<>(new StrollingHorseActor[]{actor1, actor2});

        HorseData updated1 = horse(1);
        HorseData newHorse3 = horse(3);
        when(horseService.getUserHorses()).thenReturn(List.of(updated1, newHorse3));
        when(equipped.getEquippedHorse()).thenReturn(null);

        RanchHorseUpdateService service = new RanchHorseUpdateService(horseService, equipped);

        service.update(actors);

        verify(actor1).setHorseData(updated1);
        verify(actor2, never()).setHorseData(any());
        verify(equipped).getEquippedHorse();
        verify(equipped).ensureEquippedHorse();
        verify(equipped, never()).equipHorse(any());
    }


    @Test
    void update_and_updateAsync_doNothing_whenLatestNull() throws Exception {
        HorseService horseService = mock(HorseService.class);
        EquippedHorseService equipped = mock(EquippedHorseService.class);

        StrollingHorseActor actor1 = actorWith(horse(1));
        StrollingHorseActor actor2 = actorWith(horse(2));
        Array<StrollingHorseActor> actors = new Array<>(new StrollingHorseActor[]{actor1, actor2});

        when(horseService.getUserHorses()).thenReturn(null);

        RanchHorseUpdateService service = new RanchHorseUpdateService(horseService, equipped);

        service.update(actors);

        verifyNoInteractions(equipped);
        verify(actor1, never()).setHorseData(any());
        verify(actor2, never()).setHorseData(any());
    }
}