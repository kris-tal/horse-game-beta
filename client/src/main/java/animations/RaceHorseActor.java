package animations;

import data.horse.HorseData;
import data.horse.HorseType;

public class RaceHorseActor extends BaseHorseActor {
    private float speed = 200f;

    public RaceHorseActor(HorseData horseData, float scale) {
        super(horseData.getHorseType(), horseData, scale);
        setMoving(true);
    }

    public RaceHorseActor(HorseType horseType, float scale) {
        super(horseType, scale);
        setMoving(true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (moving) {
            float newX = getX() + speed * delta;
            moveTo(newX, getY());
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }
}
