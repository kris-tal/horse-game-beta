package data.horse;

import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public enum HorseType {
    BORING_HORSE(1, "Boring Horse", new Color(0.5f, 0.5f, 0.5f, 1f)),            // dull gray
    NORMAL_HORSE(2, "Normal Horse (Average)", new Color(0.7f, 0.6f, 0.5f, 1f)),  // natural brown
    TURING_MACHINE(3, "Turing Machine", new Color(0.2f, 0.4f, 0.8f, 1f)),        // deep blue, machine-like
    RAGE(4, "Rage", new Color(0.8f, 0.1f, 0.1f, 1f)),                            // angry red
    PINK_PRINCESS_BALLERINA(5, "Pink Princess Ballerina", new Color(1f, 0.6f, 0.8f, 1f)), // soft pink
    CTRL_VICTORY(6, "Ctrl+V(ictory)", new Color(0.3f, 0.9f, 0.4f, 1f)),          // bright green (victory)
    PROBABILITY_BOOK(7, "Probability & Computing 2nd Edition", new Color(0.2f, 0.2f, 0.2f, 1f)), // academic black/gray
    LANA_DEL_REY(8, "Lana del Rey", new Color(0.4f, 0.6f, 1f, 1f)),              // dreamy sky blue
    SILLY(9, ":P", new Color(1f, 1f, 0.3f, 1f)),                                 // silly bright yellow
    SAW(10, "Saw I (2004)", new Color(0.6f, 0f, 0f, 1f)),                        // horror blood red
    PIOTR(11, "Piotr", new Color(0.8f, 0.7f, 0.2f, 1f)),                         // strong gold
    HORSE(12, "Horse", new Color(0.9f, 0.9f, 0.9f, 1f));

    private final int id;
    private final String name;
    private final Color color;

    HorseType(int id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public String getAtlasKey() {
        return name().toLowerCase();
    }

    public String getAtlasKey(String action) {
        return name().toLowerCase() + "_" + action;
    }

    private static final Map<Integer, HorseType> BY_ID = new HashMap<>();

    static {
        for (HorseType horse : values()) {
            BY_ID.put(horse.id, horse);
        }
    }

    public static HorseType fromId(int id) {
        return BY_ID.get(id);
    }

}
