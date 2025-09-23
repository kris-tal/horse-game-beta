package data.horse;

import com.google.gson.Gson;

// Represent one horse
public class HorseData {
    private final int id;
    private final int name;
    private final int level;
    private final int training_points;

    public HorseData(String json) {
        HorseData temp = new Gson().fromJson(json, HorseData.class);
        this.id = temp.id;
        this.name = temp.name;
        this.level = temp.level;
        this.training_points = temp.training_points;
    }

    // getters & setters
    public int getId() { return id; }
    public int getName() { return name; }
    public int getLevel() { return level; }
    public int getTrainingPoints() { return training_points; }

    @Override
    public String toString() {
        return String.format("<Horse %s lvl=%d>", name, level);
    }
}

