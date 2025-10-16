package data.training;

import com.google.gson.Gson;

// Represent one horse
public class TrainingResponse {

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public int getMoney() {
        return money;
    }

    public int getTraining_points() {
        return training_points;
    }

    public int getLevel() {
        return level;
    }

    public int getLongest_run_distance() {
        return longest_run_distance;
    }

    private final boolean success;
    private final String msg;
    private final int money;
    private final int training_points;
    private final int level;
    private final int longest_run_distance;
    public TrainingResponse(String json) {
        TrainingResponse temp = new Gson().fromJson(json, TrainingResponse.class);
        this.success = temp.success;
        this.msg = temp.msg;
        this.money = temp.money;
        this.training_points = temp.training_points;
        this.level = temp.level;
        this.longest_run_distance = temp.longest_run_distance;
    }

    @Override
    public String toString() {
        if(!success) return msg;
        return String.format("<Training success, money: %d lvl: %d training_points: %d longest_run: %dm>", 
            money, level, training_points, longest_run_distance);
    }

}

