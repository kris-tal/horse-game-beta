package race;

import data.horse.HorseType;
import race.state.SprintState;
import race.strategy.player.PlayerMovementStrategy;
import race.strategy.player.PlayerVisionStrategy;
import race.strategy.player.StatusEffectManager;
import race.strategy.sprint.SprintManager;
import race.strategy.sprint.SprintStrategy;

public class Player {
    private final HorseType horseType;
    private int lane;
    private float col = 0f;
    private int coins = 0;
    private final float baseSpeed;
    private boolean alive = true;
    private boolean immortal;
    private boolean raceCompleted = false;

    private final SprintState sprintState;
    private final SprintStrategy sprintStrategy;
    private final PlayerMovementStrategy movementStrategy;
    private final PlayerVisionStrategy visionStrategy;
    private final SprintManager sprintManager;
    private final StatusEffectManager statusEffectManager;

    // Status effects
    private float slowDownTimeLeft = 0f;
    private float slowDownFactor = 0.5f;
    private float visionLimitTime = 0f;
    private float visionLimitColsLeft = 0f;

    public Player(HorseType horseType, int startingLane, float speed,
                  SprintStrategy sprintStrategy, PlayerMovementStrategy movementStrategy,
                  PlayerVisionStrategy visionStrategy, SprintManager sprintManager,
                  StatusEffectManager statusEffectManager,
                  float sprintDuration, float sprintCooldown,
                  boolean immortal) {
        this.horseType = horseType;
        this.lane = startingLane;
        this.baseSpeed = speed;
        this.sprintStrategy = sprintStrategy;
        this.movementStrategy = movementStrategy;
        this.visionStrategy = visionStrategy;
        this.sprintManager = sprintManager;
        this.statusEffectManager = statusEffectManager;
        this.sprintState = new SprintState(sprintDuration, sprintCooldown);
        this.immortal = immortal;
    }

    public void update(float delta) {
        if (!alive) return;
        sprintManager.updateSprint(this, delta);
        movementStrategy.updateMovement(this, delta);
        visionStrategy.updateVision(this, delta);
        statusEffectManager.updateStatusEffects(this, delta);
    }

    // Sterowanie
    public void moveUp() {
        if (lane < 2) lane++;
    }

    public void moveDown() {
        if (lane > 0) lane--;
    }

    // Sprint API
    public void startSprint() {
        sprintManager.startSprint(this);
    }

    public boolean canSprint() {
        return sprintManager.canSprint(this);
    }

    public float getSprintCooldownProgress() {
        return sprintStrategy.getSprintCooldownProgress(sprintState.getCooldownTimer(), sprintState.getCooldown());
    }

    public boolean isSprinting() {
        return sprintState.isSprinting();
    }

    public boolean isInSprintCooldown() {
        return sprintState.isInCooldown();
    }

    public void resetSprint() {
        sprintManager.resetSprint(this);
    }

    public void makeSprintAvailable() {
        sprintManager.makeSprintAvailable(this);
    }

    public float getSprintTimeLeft() {
        return sprintState.getTimeLeft();
    }

    public float getSprintDuration() {
        return sprintState.getDuration();
    }

    public float getSprintCooldown() {
        return sprintState.getCooldown();
    }

    public float getCooldownTimer() {
        return sprintState.getCooldownTimer();
    }

    public SprintStrategy getSprintStrategy() {
        return sprintStrategy;
    }

    // Efekty
    public void slowDown(float seconds) {
        statusEffectManager.applySlowDown(this, seconds);
    }

    public void slowDown() {
        statusEffectManager.applySlowDown(this, 1.5f);
    }

    public void limitVisionByMeters(float meters) {
        statusEffectManager.applyVisionLimitByMeters(this, meters);
    }

    public boolean isVisionLimited() {
        return statusEffectManager.isVisionLimited(this);
    }

    public float getSlowDownTimeLeft() {
        return slowDownTimeLeft;
    }

    public float getSlowDownFactor() {
        return slowDownFactor;
    }

    public float getVisionLimitTime() {
        return visionLimitTime;
    }

    public float getVisionLimitColsLeft() {
        return visionLimitColsLeft;
    }

    // Stan podstawowy
    public void kill() {
        if (immortal) {
            slowDown();
        } else {
            alive = false;
        }
    }

    public void success() {
        raceCompleted = true;
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    // Gettery
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

    public HorseType getHorseType() {
        return horseType;
    }

    public SprintState getSprintState() {
        return sprintState;
    }

    public boolean isRaceCompleted() {
        return raceCompleted;
    }

    // Settery
    public void setCol(float col) {
        this.col = col;
    }

    public void setSlowDownTimeLeft(float slowDownTimeLeft) {
        this.slowDownTimeLeft = slowDownTimeLeft;
    }

    public void setVisionLimitTime(float visionLimitTime) {
        this.visionLimitTime = visionLimitTime;
    }

    public void setVisionLimitColsLeft(float visionLimitColsLeft) {
        this.visionLimitColsLeft = visionLimitColsLeft;
    }
}

