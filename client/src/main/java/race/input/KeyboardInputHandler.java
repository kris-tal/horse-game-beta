package race.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import race.Player;

public class KeyboardInputHandler implements InputHandler {
    
    @Override
    public void processInput(Player player, float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.moveUp();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            player.moveDown();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.startSprint();
        }
    }
}
