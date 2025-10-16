package race.factory;

import race.Player;
import race.config.BaseGameConfig;

public interface PlayerFactory {
    Player create(BaseGameConfig config);
}


