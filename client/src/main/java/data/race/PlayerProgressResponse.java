package data.race;

import java.util.List;

public class PlayerProgressResponse {
    public static class PlayerProgress {
        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {this.username = username;}
        String username;

        public int getProgress() {
            return progress;
        }
        public void setProgress(Integer progress) {this.progress = progress;}
        int progress;
    }

    public List<PlayerProgress> getDistances() {
        return distances;
    }
    public void setDistances(List<PlayerProgress> newDistances) {
        this.distances = newDistances;
    }

    List<PlayerProgress> distances;
}
