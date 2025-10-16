package other;

import animations.StrollingHorseActor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.Batch;
import data.horse.HorseData;
import data.horse.HorseType;
import data.race.EquippedHorseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import services.horse.HorseService;
import services.ranch.RanchHorsesRefresher;
import views.ranch.RanchHorseClickListener;
import views.ranch.horses.HorseActorFactory;
import views.ranch.horses.HorseActors;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RanchHorsesIntegrationTest extends LibgdxHeadlessTestBase {

    private Stage stage;
    private Rectangle fence;
    private Map<Integer, com.badlogic.gdx.math.Vector2> posMap;
    private HorseActorFactory factory;
    private HorseActors horseActors;
    private AtomicReference<HorseData> lastClicked;

    private final AtomicReference<List<HorseData>> currentList = new AtomicReference<>();
    private HorseService horseService;
    private EquippedHorseService equippedSpy;

    @BeforeEach
    void setup() {
        stage = new Stage(new ScreenViewport(), mock(Batch.class));
        fence = new Rectangle(100, 100, 500, 300);
        posMap = new HashMap<>();
        lastClicked = new AtomicReference<>();

        factory = new HorseActorFactory(fence, lastClicked::set);
        horseActors = new HorseActors(stage, fence, posMap, factory);

        horseService = mock(HorseService.class);
        when(horseService.getUserHorses()).thenAnswer((Answer<List<HorseData>>) invocation -> currentList.get());

        equippedSpy = mock(EquippedHorseService.class);
    }

    private static HorseData horse(int id, int typeId) {
        HorseType type = mock(HorseType.class);
        when(type.getId()).thenReturn(typeId);

        HorseData h = mock(HorseData.class);
        when(h.getId()).thenReturn(id);
        when(h.getHorseType()).thenReturn(type);
        return h;
    }

    private static Map<Integer, StrollingHorseActor> byId(Array<Actor> actors) {
        Map<Integer, StrollingHorseActor> map = new HashMap<>();
        for (Actor a : actors) {
            if (a instanceof StrollingHorseActor) {
                StrollingHorseActor s = (StrollingHorseActor) a;
                map.put(s.getHorseData().getId(), s);
            }
        }
        return map;
    }

    @Test
    void initialLoad_addsActors_andSyncsEquipped() {
        currentList.set(List.of(horse(1, 10), horse(2, 10)));
        RanchHorsesRefresher refresher = new RanchHorsesRefresher(horseService, equippedSpy);

        refresher.initialLoad(horseActors);

        Array<Actor> children = stage.getActors();
        assertTrue(children.size >= 2, "Expected at least 2 actors on stage");
        verify(equippedSpy, atLeastOnce()).getEquippedHorse();
    }

    @Test
    void refreshAsync_addsRemovesReplaces_andClickReturnsCurrentData() throws Exception {
        currentList.set(List.of(horse(1, 10), horse(2, 10)));
        RanchHorsesRefresher refresher = new RanchHorsesRefresher(horseService, equippedSpy);
        refresher.initialLoad(horseActors);

        Map<Integer, StrollingHorseActor> before = byId(stage.getActors());
        StrollingHorseActor a1Before = before.get(1);
        assertNotNull(a1Before);

        HorseData h1v2 = horse(1, 99);
        HorseData h3 = horse(3, 10);
        currentList.set(List.of(h1v2, h3));

        CountDownLatch latch = new CountDownLatch(1);
        when(equippedSpy.getEquippedHorse()).thenReturn(h1v2);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(equippedSpy).equipHorse(any());

        refresher.refreshAsync(horseActors);

        assertTrue(latch.await(2, TimeUnit.SECONDS), "Refresh did not complete in time");

        Map<Integer, StrollingHorseActor> after = byId(stage.getActors());

        StrollingHorseActor a1After = after.get(1);
        assertNotNull(a1After);
        assertNotSame(a1Before, a1After, "Actor for id 1 should be replaced when type changes");
        assertEquals(a1Before.getX(), a1After.getX(), 0.001);
        assertEquals(a1Before.getY(), a1After.getY(), 0.001);

        assertTrue(after.containsKey(3));
        assertFalse(after.containsKey(2));

        RanchHorseClickListener listener = null;
        for (EventListener l : a1After.getListeners()) {
            if (l instanceof RanchHorseClickListener) {
                listener = (RanchHorseClickListener) l;
                break;
            }
        }
        assertNotNull(listener, "RanchHorseClickListener not attached");

        listener.clicked(null, 0, 0);
        assertNotNull(lastClicked.get());
        assertEquals(1, lastClicked.get().getId());
        assertEquals(99, lastClicked.get().getHorseType().getId());
    }

    @Test
    void updateZOrder_ordersByYDescending() {
        currentList.set(List.of(horse(1, 10), horse(2, 10)));
        RanchHorsesRefresher refresher = new RanchHorsesRefresher(horseService, equippedSpy);
        refresher.initialLoad(horseActors);

        Map<Integer, StrollingHorseActor> map = byId(stage.getActors());
        StrollingHorseActor a1 = map.get(1);
        StrollingHorseActor a2 = map.get(2);


        a1.setPosition(0, 200);
        a2.setPosition(0, 100);

        horseActors.updateZOrder();

        assertTrue(a1.getZIndex() < a2.getZIndex(), "Actor with higher Y should have lower z-index");
    }
}