package race.strategy.sprint;

import race.Player;

public class StandardSprintManager implements SprintManager {
    
    @Override
    public void startSprint(Player player) {
        if (player.getSprintStrategy().canSprint(player.getSprintState().isSprinting(), player.getSprintState().getCooldownTimer())) {
            player.getSprintState().setSprinting(true);
            player.getSprintState().setTimeLeft(player.getSprintState().getDuration());
        }
    }
    
    @Override
    public boolean canSprint(Player player) {
        return player.getSprintStrategy().canSprint(player.getSprintState().isSprinting(), player.getSprintState().getCooldownTimer());
    }
    
    @Override
    public void resetSprint(Player player) {
        player.getSprintState().setSprinting(false);
        player.getSprintState().setCooldownTimer(player.getSprintStrategy().calculateSprintCooldown(player.getSprintState().getCooldown()));
    }

    @Override
    public void makeSprintAvailable(Player player) {
        player.getSprintState().setSprinting(false);
        player.getSprintState().setCooldownTimer(0f);
    }
    
    @Override
    public void updateSprint(Player player, float delta) {
        if (player.getSprintState().isSprinting()) {
            player.getSprintState().setTimeLeft(player.getSprintState().getTimeLeft() - delta);
            if (player.getSprintState().getTimeLeft() <= 0f) {
                player.getSprintState().setSprinting(false);
                player.getSprintState().setCooldownTimer(player.getSprintStrategy().calculateSprintCooldown(player.getSprintState().getCooldown()));
            }
        }
        
        if (player.getSprintState().getCooldownTimer() > 0f) {
            player.getSprintState().setCooldownTimer(player.getSprintState().getCooldownTimer() - delta);
        }
    }
}




