package services.media;

import services.managers.ResourceManager;

public class ResourceManagerAudioService implements AudioService {
    @Override
    public void playSuccessSound() {
        ResourceManager.horseSound1.play();
    }

    @Override
    public void playFailureSound() {
        ResourceManager.snortSound.play();
    }
}