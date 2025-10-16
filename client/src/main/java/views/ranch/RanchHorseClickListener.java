package views.ranch;

import animations.StrollingHorseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import data.horse.HorseData;

import java.util.function.Consumer;

public class RanchHorseClickListener extends ClickListener {
    private final StrollingHorseActor actor;
    private final Consumer<HorseData> onHorseClicked;

    public RanchHorseClickListener(StrollingHorseActor actor, Consumer<HorseData> onHorseClicked) {
        this.actor = actor;
        this.onHorseClicked = onHorseClicked;
    }

    @Override
    public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        if (onHorseClicked != null) onHorseClicked.accept(actor.getHorseData());
    }

    @Override
    public void enter(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, Actor fromActor) {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
    }

    @Override
    public void exit(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, Actor toActor) {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }
}