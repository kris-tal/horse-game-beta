package services.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ResourceManager {

    private static final AssetManager assetManager = new AssetManager();
    private static final InternalFileHandleResolver filePathResolver = new InternalFileHandleResolver();

    public static final String AUTH_SCREEN_BG_PATH = "images/background/auth_screen_background.png";
    public static final String RANCH_SCREEN_BG_PATH = "images/background/bg.png";
    public static final String RACE_SCREEN_BG_PATH = "images/background/race_screen_background.png";
    public static final String LOGO_PATH = "images/other/logo.png";
    public static final String HORSE_SOUND1_PATH = "sounds/horse_sound1.wav";
    public static final String SNORT_SOUND_PATH = "sounds/snort.wav";
    public static final String UI_ATLAS_PATH = "ui/ui.atlas";
    public static final String UI_FONT_PATH = "ui/fonts/uifont.fnt";
    public static final String ICONS_ATLAS_PATH = "ui/icons/icons.atlas";
    public static final String HORSES_ATLAS_PATH = "horses/horses.atlas";
    public static final String HORSE_ANIMATIONS_ATLAS_PATH = "horses/animations/animations.atlas";
    public static final String VISION_LIMITED_PATH = "images/race/bigbush.png";
    public static final String OBSTACLES_ATLAS_PATH = "images/race/obstacles.atlas";

    public static Texture authScreenBgTexture;
    public static Texture ranchScreenBgTexture;
    public static Texture raceScreenBgTexture;
    public static Texture logoTexture;
    public static TextureAtlas uiTextureAtlas;
    public static TextureAtlas uiAtlas;    //
    public static Skin uiSkin;
    public static Sound horseSound1;
    public static Sound snortSound;
    public static BitmapFont uiDefaultFont;
    public static BitmapFont uiMediumFont;
    public static BitmapFont uiLargeFont;
    public static TextureAtlas iconsAtlas;
    public static TextureAtlas horsesAtlas;
    public static TextureAtlas horseAnimationsAtlas;
    public static Texture visionLimitedTexture;
    public static TextureAtlas obstaclesAtlas;


    static {
        assetManager.load(AUTH_SCREEN_BG_PATH, Texture.class);
        assetManager.load(RANCH_SCREEN_BG_PATH, Texture.class);
        assetManager.load(RACE_SCREEN_BG_PATH, Texture.class);
        assetManager.load(LOGO_PATH, Texture.class);
        assetManager.load(HORSE_SOUND1_PATH, Sound.class);
        assetManager.load(SNORT_SOUND_PATH, Sound.class);
        assetManager.load(UI_ATLAS_PATH, TextureAtlas.class);
        assetManager.load(UI_FONT_PATH, BitmapFont.class);
        assetManager.load(ICONS_ATLAS_PATH, TextureAtlas.class);
        assetManager.load(HORSES_ATLAS_PATH, TextureAtlas.class);
        assetManager.load(HORSE_ANIMATIONS_ATLAS_PATH, TextureAtlas.class);
        assetManager.load(VISION_LIMITED_PATH, Texture.class);
        assetManager.load(OBSTACLES_ATLAS_PATH, TextureAtlas.class);

        assetManager.finishLoading();

        authScreenBgTexture = assetManager.get(AUTH_SCREEN_BG_PATH, Texture.class);
        ranchScreenBgTexture = assetManager.get(RANCH_SCREEN_BG_PATH, Texture.class);
        raceScreenBgTexture = assetManager.get(RACE_SCREEN_BG_PATH, Texture.class);
        logoTexture = assetManager.get(LOGO_PATH, Texture.class);
        horseSound1 = assetManager.get(HORSE_SOUND1_PATH, Sound.class);
        snortSound = assetManager.get(SNORT_SOUND_PATH, Sound.class);
        uiAtlas = assetManager.get(UI_ATLAS_PATH, TextureAtlas.class);
        uiDefaultFont = assetManager.get(UI_FONT_PATH, BitmapFont.class);
        uiMediumFont = new BitmapFont(Gdx.files.internal(UI_FONT_PATH), false);
        uiLargeFont = new BitmapFont(Gdx.files.internal(UI_FONT_PATH), false);
        iconsAtlas = assetManager.get(ICONS_ATLAS_PATH, TextureAtlas.class);
        horsesAtlas = assetManager.get(HORSES_ATLAS_PATH, TextureAtlas.class);
        horseAnimationsAtlas = assetManager.get(HORSE_ANIMATIONS_ATLAS_PATH, TextureAtlas.class);
        visionLimitedTexture = assetManager.get(VISION_LIMITED_PATH, Texture.class);
        obstaclesAtlas = assetManager.get(OBSTACLES_ATLAS_PATH, TextureAtlas.class);

    }

    //i'll need this later (maybe)
    public static void loadTexture(String path) {
        if (!assetManager.isLoaded(path)) {
            assetManager.setLoader(Texture.class, new TextureLoader(filePathResolver));
            assetManager.load(path, Texture.class);
            assetManager.finishLoadingAsset(path);
        }
    }

    public static Texture getTexture(String path) {
        if (assetManager.isLoaded(path)) {
            return assetManager.get(path, Texture.class);
        }
        return null;
    }

    public static void loadSkin(String path) {
        if (!assetManager.isLoaded(path)) {
            assetManager.load(path, Skin.class);
            assetManager.finishLoadingAsset(path);
        }
    }

    public static Skin getSkin(String path) {
        if (assetManager.isLoaded(path)) {
            return assetManager.get(path, Skin.class);
        }
        return null;
    }

    public void dispose() {
        assetManager.dispose();
        if (authScreenBgTexture != null) authScreenBgTexture.dispose();
        if (logoTexture != null) logoTexture.dispose();
        if (uiSkin != null) uiSkin.dispose();
    }
}