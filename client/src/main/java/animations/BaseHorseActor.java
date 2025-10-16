package animations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import data.horse.HorseData;
import data.horse.HorsePalette;
import data.horse.HorseType;

public abstract class BaseHorseActor extends Actor {
    protected HorseData horseData;
    protected final HorseType horseType;
    protected final HorseAnimationSet animationSet;
    protected final HorsePalette palette;

    protected float stateTime = 0f;
    protected boolean moving = false;
    protected boolean facingRight = true;
    protected final float scale;

    float HORSE_WIDTH = 32f;
    float HORSE_HEIGHT = 32f;

    public BaseHorseActor(HorseType horseType, float scale) {
        this(horseType, null, scale);
    }

    public BaseHorseActor(HorseType horseType, HorseData horseData, float scale) {
        this.horseType = horseType;
        this.horseData = horseData;
        this.animationSet = HorseAnimationCache.get(horseType);
        this.palette = horseType.getPalette();
        this.scale = scale;

        setSize(HORSE_WIDTH * scale, HORSE_HEIGHT * scale);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        AnimationType animType = moving ? AnimationType.RUN : AnimationType.IDLE;

        TextureRegion body = animationSet.getFrame(animType, HorseAnimationSet.Part.BODY, stateTime);
        TextureRegion mane = animationSet.getFrame(animType, HorseAnimationSet.Part.MANE, stateTime);
        TextureRegion tail = animationSet.getFrame(animType, HorseAnimationSet.Part.TAIL, stateTime);

        float width = getWidth();
        float height = getHeight();
        float drawX = getX();
        float drawY = getY();

        if (!facingRight) {
            drawX = getX() + width;
            width = -width;
        }

        // draw body
        if (body != null) {
            Color c = palette.getBodyColor();
            batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
            batch.draw(body, drawX, drawY, width, height);
        }

        // draw mane
        if (mane != null) {
            Color c = palette.getManeColor();
            batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
            batch.draw(mane, drawX, drawY, width, height);
        }

        // draw tail
        if (tail != null) {
            Color c = palette.getTailColor();
            batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
            batch.draw(tail, drawX, drawY, width, height);
        }

        batch.setColor(1f, 1f, 1f, 1f);
    }

    protected void moveTo(float x, float y) {
        facingRight = x >= getX();
        setPosition(x, y);
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isFacingRight() {
        return facingRight;
    }
}
