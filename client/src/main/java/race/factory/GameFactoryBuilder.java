package race.factory;

import race.config.BaseGameConfig;
import race.input.InputHandler;
import race.map.MapGenerator;

public class GameFactoryBuilder implements GameWorldFactoryBuilder {
    private MapGenerator mapGenerator;
    private InputHandler inputHandler;
    private BaseGameConfig config;
    private PlayerFactory playerFactory;
    private HorseActorFactory horseActorFactory;
    private SystemsFactory systemsFactory;

    @Override
    public GameWorldFactoryBuilder withMapGenerator(MapGenerator mapGenerator) {
        this.mapGenerator = mapGenerator;
        return this;
    }

    @Override
    public GameWorldFactoryBuilder withInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        return this;
    }

    @Override
    public GameWorldFactoryBuilder withConfig(BaseGameConfig config) {
        this.config = config;
        return this;
    }

    @Override
    public GameWorldFactoryBuilder withPlayerFactory(PlayerFactory playerFactory) {
        this.playerFactory = playerFactory;
        return this;
    }

    @Override
    public GameWorldFactoryBuilder withHorseActorFactory(HorseActorFactory horseActorFactory) {
        this.horseActorFactory = horseActorFactory;
        return this;
    }

    @Override
    public GameWorldFactoryBuilder withSystemsFactory(SystemsFactory systemsFactory) {
        this.systemsFactory = systemsFactory;
        return this;
    }

    @Override
    public GameWorldFactory build() {
        if (mapGenerator == null || inputHandler == null || config == null) {
            throw new IllegalStateException("All required components must be set before building");
        }
        return new GameFactory(
            mapGenerator,
            inputHandler,
            config,
            playerFactory != null ? playerFactory : new StandardPlayerFactory(),
            horseActorFactory != null ? horseActorFactory : new RaceHorseActorFactory(),
            systemsFactory != null ? systemsFactory : new GameSystemsFactory()
        );
    }
}
