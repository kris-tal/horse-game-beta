package race.factory;

import race.config.BaseGameConfig;
import race.input.InputHandler;
import race.map.MapGenerator;

public interface GameWorldFactoryBuilder {

    GameWorldFactoryBuilder withMapGenerator(MapGenerator mapGenerator);

    GameWorldFactoryBuilder withInputHandler(InputHandler inputHandler);

    GameWorldFactoryBuilder withConfig(BaseGameConfig config);

    GameWorldFactoryBuilder withPlayerFactory(PlayerFactory playerFactory);

    GameWorldFactoryBuilder withHorseActorFactory(HorseActorFactory horseActorFactory);

    GameWorldFactoryBuilder withSystemsFactory(SystemsFactory systemsFactory);

    GameWorldFactory build();
}
