package race.factory;

import animations.BaseHorseActor;
import animations.RaceHorseActor;
import race.Player;

public class RaceHorseActorFactory implements HorseActorFactory {
    @Override
    public BaseHorseActor create(Player player) {
        return new RaceHorseActor(player.getHorseType(), 3f);
    }
}
