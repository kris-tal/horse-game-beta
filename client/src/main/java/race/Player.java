package race;

import data.horse.HorseType;

public class Player {
    private final HorseType horseType;
    private int lane;
    private float col = 0f;
    private int coins = 0;
    private float baseSpeed;
    private boolean alive = true;

    private boolean sprinting = false;
    private float sprintTimeLeft = 0f;
    private float sprintDuration = 2f;
    private float sprintCooldown = 10f;
    private float cooldownTimer = 0f;
    private float slowDownTimeLeft = 0f;  // czas spowolnienia
    private float slowDownFactor = 0.5f;
    // Widoczność
    private float visionLimitTime = 0f;
    private float visionLimitColsLeft = 0f;

    public Player(HorseType horseType, int startingLane, float speed) {
        this.horseType = horseType;
        lane = startingLane;
        this.baseSpeed = speed;
    }

    //Ruch
    public void moveUp() {
        if (lane < 2)
            lane++;
    }

    public void slowDown(float seconds) {
        slowDownTimeLeft = seconds;
    }

    public void moveDown() {
        if (lane > 0)
            lane--;
    }

    public void update(float delta) {
        // update sprint
        if (sprinting) {
            sprintTimeLeft -= delta;
            if (sprintTimeLeft <= 0f) {
                sprinting = false;
                cooldownTimer = sprintCooldown;
            }
        }

        // update cooldown sprintu
        if (cooldownTimer > 0f) {
            cooldownTimer -= delta;
        }

        // update widoczności
        if (visionLimitTime > 0f) {
            visionLimitTime -= delta;
        }

        // update spowolnienia
        float currentSpeed = baseSpeed;
        if (slowDownTimeLeft > 0f) {
            currentSpeed *= slowDownFactor;
            slowDownTimeLeft -= delta;
        }

        if (sprinting) currentSpeed *= 2.0f;  // sprint

        float prevCol = col;
        col += delta * currentSpeed;

        if (visionLimitColsLeft > 0f) {
            float advancedCols = Math.max(0f, col - prevCol);
            visionLimitColsLeft = Math.max(0f, visionLimitColsLeft - advancedCols);
        }
    }


    // Sprint
    public void startSprint() {
        if (!sprinting && cooldownTimer <= 0f) {
            sprinting = true;
            sprintTimeLeft = sprintDuration;
        }
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public boolean canSprint() {
        return !sprinting && cooldownTimer <= 0f;
    }

    public float getSprintCooldownProgress() {
        float denom = sprintCooldown <= 0f ? 1f : sprintCooldown;
        return Math.max(0f, cooldownTimer) / denom;
    }

    public boolean isInSprintCooldown() {
        return !sprinting && cooldownTimer > 0f;
    }

    public float getSprintTimeLeft() {
        return sprintTimeLeft;
    }

    public float getSprintDuration() {
        return sprintDuration;
    }

    public float getSprintCooldown() {
        return sprintCooldown;
    }

    public void resetSprint() {
        sprinting = false;
        cooldownTimer = sprintCooldown;
    }

    public void makeSprintAvailable() {
        sprinting = false;
        cooldownTimer = 0f;
    }

    // Spowolnienie przez przeszkody
    public void slowDown() {
        slowDown(1.5f);
    }

    // Widoczność w krzakach
    public void limitVision(float seconds) {
        visionLimitTime = seconds;
    }

    public void limitVisionByCols(float cols) {
        visionLimitColsLeft = Math.max(visionLimitColsLeft, cols);
    }

    public void limitVisionByMeters(float meters) {
        limitVisionByCols(meters / 10f);
    }

    public boolean isVisionLimited() {
        return visionLimitTime > 0f || visionLimitColsLeft > 0f;
    }

    public int getLane() {
        return lane;
    }

    public float getCol() {
        return col;
    }

    public boolean isAlive() {
        return alive;
    }

    public float getSpeed() {
        return baseSpeed;
    }

    public int getCoins() {
        return coins;
    }

    public void kill() {
        alive = false;
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public HorseType getHorseType() {
        return horseType;
    }
}
