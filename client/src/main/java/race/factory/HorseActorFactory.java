package race.factory;

import animations.BaseHorseActor;
import race.Player;

public interface HorseActorFactory {
    BaseHorseActor create(Player player);
}


