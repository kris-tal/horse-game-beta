package animations;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum AnimationType {
    IDLE(0.1f, Animation.PlayMode.LOOP),
    RUN(0.15f, Animation.PlayMode.LOOP);

    private final float frameDuration;
    private final Animation.PlayMode playMode;

    AnimationType(float frameDuration, Animation.PlayMode playMode) {
        this.frameDuration = frameDuration;
        this.playMode = playMode;
    }

    public float getFrameDuration() {
        return frameDuration;
    }

    public Animation.PlayMode getPlayMode() {
        return playMode;
    }

    public String getName() {
        return name().toLowerCase();
    }
}
