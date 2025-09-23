package data.lobby;

public class PlayerInfoClient {
    private String username;
    private int progress;
    private PlayerStatus role;  // <-- changed from boolean isCreator

    public PlayerInfoClient() {
    }

    public PlayerInfoClient(String username, PlayerStatus role) {
        this.username = username;
        this.progress = 0;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public PlayerStatus getRole() {
        return role;
    }

    public boolean isCreator(){
        return getRole()==PlayerStatus.Host;
    }

    public void setRole(PlayerStatus role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "PlayerInfoClient{" +
                "username='" + username + '\'' +
                ", progress=" + progress +
                ", role=" + role +
                '}';
    }
}