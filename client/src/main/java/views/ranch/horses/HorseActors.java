package views.ranch.horses;

import animations.StrollingHorseActor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import data.horse.HorseData;

import java.util.*;

public class HorseActors {
    private final Stage stage;
    private final Rectangle fence;
    private final Map<Integer, Vector2> horseIdToPosition;
    private final HorseActorFactory factory;
    private final Array<StrollingHorseActor> actors = new Array<>();

    public HorseActors(Stage stage, Rectangle fence, Map<Integer, Vector2> horseIdToPosition, HorseActorFactory factory) {
        this.stage = stage;
        this.fence = fence;
        this.horseIdToPosition = horseIdToPosition;
        this.factory = factory;
    }

    public void init(List<HorseData> horses) {
        if (horses == null) return;
        for (HorseData h : horses) {
            StrollingHorseActor a = factory.create(h);
            Vector2 saved = horseIdToPosition.get(h.getId());
            if (saved != null) {
                a.setPosition(saved.x, saved.y);
            } else {
                a.setPosition(
                        MathUtils.random(fence.x, fence.x + fence.width),
                        MathUtils.random(fence.y, fence.y + fence.height)
                );
            }
            actors.add(a);
            stage.addActor(a);
        }
    }

    public void syncWith(List<HorseData> latest) {
        if (latest == null) return;

        Map<Integer, StrollingHorseActor> byId = new HashMap<>();
        for (StrollingHorseActor a : actors) {
            byId.put(a.getHorseData().getId(), a);
        }

        Set<Integer> latestIds = new HashSet<>();
        for (HorseData h : latest) {
            latestIds.add(h.getId());
            StrollingHorseActor existing = byId.get(h.getId());
            if (existing != null) {
                boolean sameType = existing.getHorseData().getHorseType().getId() == h.getHorseType().getId();
                if (sameType) {
                    existing.setHorseData(h);
                } else {
                    replace(existing, h);
                }
            } else {
                addNew(h);
            }
        }

        for (int i = actors.size - 1; i >= 0; i--) {
            StrollingHorseActor a = actors.get(i);
            if (!latestIds.contains(a.getHorseData().getId())) {
                a.remove();
                actors.removeIndex(i);
            }
        }
    }

    public void updateZOrder() {
        actors.sort((a, b) -> Float.compare(b.getY(), a.getY()));
        for (int i = 0; i < actors.size; i++) {
            actors.get(i).setZIndex(i + 1);
        }
    }

    private void addNew(HorseData h) {
        StrollingHorseActor a = factory.create(h);
        a.setPosition(
                MathUtils.random(fence.x, fence.x + fence.width),
                MathUtils.random(fence.y, fence.y + fence.height)
        );
        actors.add(a);
        stage.addActor(a);
    }

    private void replace(StrollingHorseActor oldActor, HorseData newData) {
        float x = oldActor.getX();
        float y = oldActor.getY();
        oldActor.remove();
        actors.removeValue(oldActor, true);

        StrollingHorseActor a = factory.create(newData);
        a.setPosition(x, y);
        actors.add(a);
        stage.addActor(a);
    }
}