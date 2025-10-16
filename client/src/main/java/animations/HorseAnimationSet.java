package animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;

public final class HorseAnimationSet {
    public enum Part {BODY, MANE, TAIL}

    private final EnumMap<AnimationType, EnumMap<Part, Animation<TextureRegion>>> animations;

    public HorseAnimationSet(EnumMap<AnimationType, EnumMap<Part, Animation<TextureRegion>>> animations) {
        this.animations = animations;
    }

    public Animation<TextureRegion> getAnimation(AnimationType type, Part part) {
        EnumMap<Part, Animation<TextureRegion>> byType = animations.get(type);
        if (byType == null) throw new NullPointerException("Object was null");
        return byType.get(part);
    }

    public TextureRegion getFrame(AnimationType type, Part part, float stateTime) {
        Animation<TextureRegion> anim = getAnimation(type, part);
        if (anim == null) throw new NullPointerException("Object was null");
        return anim.getKeyFrame(stateTime);
    }

    public boolean hasPart(AnimationType type, Part part) {
        return getAnimation(type, part) != null;
    }
}
