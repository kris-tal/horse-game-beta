package views.ranch.horses;

import animations.StrollingHorseActor;
import com.badlogic.gdx.math.Rectangle;
import data.horse.HorseData;
import views.ranch.RanchHorseClickListener;

import java.util.function.Consumer;

public class HorseActorFactory {
    private final Rectangle fence;
    private final Consumer<HorseData> onHorseClicked;

    public HorseActorFactory(Rectangle fence, Consumer<HorseData> onHorseClicked) {
        this.fence = fence;
        this.onHorseClicked = onHorseClicked;
    }

    public StrollingHorseActor create(HorseData horse) {
        StrollingHorseActor actor = new StrollingHorseActor(
                horse, 3f,
                fence.x, fence.x + fence.width,
                fence.y, fence.y + fence.height
        );
        actor.addListener(new RanchHorseClickListener(actor, onHorseClicked));
        return actor;
    }
}