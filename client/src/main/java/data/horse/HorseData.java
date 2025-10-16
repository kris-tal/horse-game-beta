package data.horse;

import com.google.gson.Gson;

// this represents a horse owned by the player

public class HorseData {
    private final int id;
    private final int name;
    private final int level;
    private final int training_points;
    private final int longest_run_distance;

    public HorseData() {
        this.id = 0;
        this.name = 0;
        this.level = 0;
        this.training_points = 0;
        this.longest_run_distance = 0;
    }

    public HorseData(String json) {
        HorseData temp = new Gson().fromJson(json, HorseData.class);
        this.id = temp.id;
        this.name = temp.name;
        this.level = temp.level;
        this.training_points = temp.training_points;
        this.longest_run_distance = temp.longest_run_distance;
    }

    public int getId() {
        return id;
    }

    public int getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getTrainingPoints() {
        return training_points;
    }

    public int getLongestRunDistance() {
        return longest_run_distance;
    }

    public HorseType getHorseType() {
        return HorseTypeRegistry.getById(name);
    }

    @Override
    public String toString() {
        return String.format("<Horse %s lvl=%d>", name, level);
    }
}