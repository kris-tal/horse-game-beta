package data.horse;

import com.badlogic.gdx.graphics.Color;

public final class HorsePalette {
    private final Color body;
    private final Color mane;
    private final Color tail;

    public HorsePalette(Color body, Color mane, Color tail) {
        this.body = body;
        this.mane = mane;
        this.tail = tail;
    }

    public Color getBodyColor() {
        return body;
    }

    public Color getManeColor() {
        return mane;
    }

    public Color getTailColor() {
        return tail;
    }
}