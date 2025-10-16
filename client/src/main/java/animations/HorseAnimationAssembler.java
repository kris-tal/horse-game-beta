package animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import data.horse.HorseType;
import services.managers.ResourceManager;

import java.util.EnumMap;

public final class HorseAnimationAssembler {

    private HorseAnimationAssembler() {
    }

    public static HorseAnimationSet assemble(HorseType type) {
        EnumMap<AnimationType, EnumMap<HorseAnimationSet.Part, Animation<TextureRegion>>> animations =
                new EnumMap<>(AnimationType.class);

        for (AnimationType animType : AnimationType.values()) {
            EnumMap<HorseAnimationSet.Part, Animation<TextureRegion>> partsMap =
                    new EnumMap<>(HorseAnimationSet.Part.class);

            for (HorseAnimationSet.Part part : HorseAnimationSet.Part.values()) {
                String prefix = part.name().toLowerCase() + "_" + animType.getName();

                Array<TextureRegion> frames = findAnimationRegions(ResourceManager.horseAnimationsAtlas, prefix);

                if (frames.size == 0) {
                    throw new RuntimeException("Missing animation frames for horse '" + type.getAtlasKey() +
                            "', part '" + part + "', animation '" + animType.getName() + "'");
                }

                if (animType == AnimationType.RUN && frames.size != 2) {
                    Gdx.app.log("Assembler", "RUN animation frame count is " + frames.size, new RuntimeException());
                }

                partsMap.put(part, new Animation<>(
                        animType.getFrameDuration(),
                        frames,
                        animType.getPlayMode()
                ));
            }

            animations.put(animType, partsMap);
        }

        return new HorseAnimationSet(animations);
    }

    private static Array<TextureRegion> findAnimationRegions(TextureAtlas atlas, String prefix) {
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(prefix);
        Array<TextureRegion> frames = new Array<>();

        for (TextureAtlas.AtlasRegion region : regions) {
            frames.add(region);
        }

        if (frames.size == 0) {
            TextureAtlas.AtlasRegion single = atlas.findRegion(prefix);
            if (single != null) frames.add(single);
        }

        return frames;
    }
}
