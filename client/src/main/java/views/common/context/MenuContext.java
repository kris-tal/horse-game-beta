package views.common.context;

public class MenuContext {
    public enum RaceMode {SINGLEPLAYER, MULTIPLAYER}

    public enum LobbyMode {CREATE, JOIN}

    private RaceMode raceMode;
    private LobbyMode lobbyMode;
    private String chosenHorse;

    public void setRaceMode(RaceMode raceMode) {
        this.raceMode = raceMode;
    }

    public void setLobbyMode(LobbyMode lobbyMode) {
        this.lobbyMode = lobbyMode;
    }

    public RaceMode getRaceMode() {
        return raceMode;
    }

    public LobbyMode getLobbyMode() {
        return lobbyMode;
    }

    public void setChosenHorse(String horse) {
        this.chosenHorse = horse;
    }

    public String getChosenHorse() {
        return chosenHorse;
    }
}
