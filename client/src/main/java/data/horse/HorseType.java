package data.horse;

import com.badlogic.gdx.graphics.Color;

public final class HorseType {
    private final int id;
    private final String key;
    private final String name;
    private final HorsePalette palette;
    private final Color color;

    public HorseType(int id, String key, String name, HorsePalette palette, String color) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.palette = palette;
        this.color = Color.valueOf(color);
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public HorsePalette getPalette() {
        return palette;
    }

    public Color getColor() {
        return color;
    }

    public String getAtlasKey() {
        return key;
    }

    @Override
    public String toString() {
        return "HorseType{id=" + id + ", key='" + key + "', name='" + name + "'}";
    }
}