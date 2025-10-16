package race.strategy.sprint;

public interface SprintStrategy {

    float calculateSprintCooldown(float baseCooldown);

    float getSprintCooldownProgress(float cooldownTimer, float sprintCooldown);

    boolean canSprint(boolean isSprinting, float cooldownTimer);

}
