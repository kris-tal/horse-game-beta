package race.strategy.sprint;

public class StandardSprintStrategy extends AbstractSprintStrategy {
    @Override
    public float calculateSprintCooldown(float baseCooldown) {
        return baseCooldown;
    }
}
