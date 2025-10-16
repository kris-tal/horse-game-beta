package animations;

import data.horse.HorseType;

public class IdleHorseActor extends BaseHorseActor {

    public IdleHorseActor(HorseType horseType, float scale) {
        super(horseType, scale);
        setMoving(false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
