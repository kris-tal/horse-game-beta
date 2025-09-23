package views.common;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import data.horse.HorseType;
import services.managers.ResourceManager;

public class HorseFactory {

    public static Image createHorseImage(HorseType horseType, float size) {
        TextureAtlas atlas = ResourceManager.horsesAtlas;
        TextureAtlas.AtlasRegion region = atlas.findRegion(horseType.getAtlasKey());

        if (region == null) {
            throw new IllegalArgumentException("No horse region found for name: " + horseType.name());
        }

        Image image = new Image(new TextureRegionDrawable(region));
        image.setScaling(Scaling.fit);
        image.setSize(size, size);
        return image;
    }

    public static Image createHorseImage(HorseType horseType) {
        return createHorseImage(horseType, 32f);
    }
}
