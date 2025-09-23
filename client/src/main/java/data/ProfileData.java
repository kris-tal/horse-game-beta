package data;

import com.google.gson.Gson;

public class ProfileData {
    private final String username;
    private final String email;
    private final int money;
    private final int prestige;
    private final String created_at;

    public ProfileData(String json) {
        ProfileData temp = new Gson().fromJson(json, ProfileData.class);
        this.username = temp.username;
        this.email = temp.email;
        this.money = temp.money;
        this.prestige = temp.prestige;
        this.created_at = temp.created_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getMoney() {
        return money;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public int getPrestige() {
        return prestige;
    }

    @Override
    public String toString() {
        return String.format("<User %s, with email %s, created at %s, prestige %d, money %d>", username, email, created_at.toString(), prestige, money
        );
    }
}

