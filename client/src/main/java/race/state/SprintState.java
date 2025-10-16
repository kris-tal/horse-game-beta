package race.state;

public class SprintState {
    private boolean sprinting = false;
    private float timeLeft = 0f;
    private float cooldownTimer = 0f;
    private final float duration;
    private final float cooldown;

    public SprintState(float duration, float cooldown) {
        this.duration = duration;
        this.cooldown = cooldown;
    }

    public boolean isSprinting() { return sprinting; }
    public void setSprinting(boolean sprinting) { this.sprinting = sprinting; }

    public float getTimeLeft() { return timeLeft; }
    public void setTimeLeft(float timeLeft) { this.timeLeft = timeLeft; }

    public float getCooldownTimer() { return cooldownTimer; }
    public void setCooldownTimer(float cooldownTimer) { this.cooldownTimer = cooldownTimer; }

    public float getDuration() { return duration; }
    public float getCooldown() { return cooldown; }

    public boolean isInCooldown() {
        return !sprinting && cooldownTimer > 0f;
    }
}
