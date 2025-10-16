package race.strategy.sprint;

import race.Player;

public interface SprintManager {
    void startSprint(Player player);
    boolean canSprint(Player player);
    void resetSprint(Player player);
    void makeSprintAvailable(Player player);
    void updateSprint(Player player, float delta);
}




