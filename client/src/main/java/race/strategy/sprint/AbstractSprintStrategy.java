package race.strategy.sprint;

public abstract class AbstractSprintStrategy implements SprintStrategy {
    
    @Override
    public float getSprintCooldownProgress(float cooldownTimer, float sprintCooldown) {
        float denom = sprintCooldown <= 0f ? 1f : sprintCooldown;
        return Math.max(0f, cooldownTimer) / denom;
    }

    @Override
    public boolean canSprint(boolean isSprinting, float cooldownTimer) {
        return !isSprinting && cooldownTimer <= 0f;
    }
}













