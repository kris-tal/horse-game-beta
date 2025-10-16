package race.strategy.training;

import race.strategy.sprint.AbstractSprintStrategy;

public class TrainingSprintStrategy extends AbstractSprintStrategy {
    private final float sprintCooldown;

    public TrainingSprintStrategy(float sprintCooldown) {
        this.sprintCooldown = sprintCooldown;
    }

    @Override
    public float calculateSprintCooldown(float baseCooldown) {
        return sprintCooldown;
    }
}
