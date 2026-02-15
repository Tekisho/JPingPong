package io.github.tekisho.pingponggame.view.delegate;

public interface GameViewDelegate {
    void handleSetInitialSize();

    void handleSceneResize(double w, double h);

    void handleResetGameObjectPositions();

    void  handleResetGame();

    void handleSettingsButtonClick();
}
