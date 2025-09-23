package animations;

import com.badlogic.gdx.math.MathUtils;
import data.horse.HorseType;

public class StrollingHorseActor extends BaseHorseActor {
    private final float minX, maxX, minY, maxY;
    private float targetX, targetY;
    private float speed;
    private float idleTimer = 0f;
    private float nextMoveDelay = 0f;

    public StrollingHorseActor(HorseType horseType, float minX, float maxX, float minY, float maxY) {
        super(horseType);
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        scheduleNextMove();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (moving) {
            float dx = targetX - getX();
            float dy = targetY - getY();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            if (dist < 2f) {
                setMoving(false);
                idleTimer = 0f;
                scheduleNextMove();
            } else {
                float nx = dx / dist;
                float ny = dy / dist;
                moveTo(getX() + nx * speed * delta, getY() + ny * speed * delta);
            }
        } else {
            idleTimer += delta;
            if (idleTimer > nextMoveDelay) {
                pickNewTarget();
                setMoving(true);
            }
        }
    }

    private void pickNewTarget() {
        boolean shortWalk = MathUtils.randomBoolean(0.3f);
        if (shortWalk) {
            float offsetX = MathUtils.random(-50f, 50f);
            float offsetY = MathUtils.random(-20f, 20f);
            targetX = MathUtils.clamp(getX() + offsetX, minX, maxX);
            targetY = MathUtils.clamp(getY() + offsetY, minY, maxY);
        } else {
            targetX = MathUtils.random(minX, maxX);
            targetY = MathUtils.random(minY, maxY);
        }

        speed = MathUtils.random(40f, 80f);
    }

    private void scheduleNextMove() {
        nextMoveDelay = MathUtils.random(0f, 5f);
    }
}
