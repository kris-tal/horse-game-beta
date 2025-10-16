package race.factory;

import race.Player;
import race.config.BaseGameConfig;
import race.strategy.player.StandardPlayerMovementStrategy;
import race.strategy.player.StandardPlayerVisionStrategy;
import race.strategy.player.StandardStatusEffectManager;
import race.strategy.sprint.StandardSprintManager;
import race.strategy.sprint.StandardSprintStrategy;
import race.strategy.training.TrainingSprintStrategy;

public class StandardPlayerFactory implements PlayerFactory {
    @Override
    public Player create(BaseGameConfig config) {
        return new Player(
            config.getHorseType(),
            config.getLanes() / 2,
            config.getSpeed(),
            config.getSprintCooldownReduction() > 0f ?
                new TrainingSprintStrategy(config.getSprintCooldown()) :
                new StandardSprintStrategy(),
            new StandardPlayerMovementStrategy(),
            new StandardPlayerVisionStrategy(),
            new StandardSprintManager(),
            new StandardStatusEffectManager(),
            config.getSprintDuration(),
            config.getSprintCooldown(),
            config.isPlayerImmortal()
        );
    }
}
