package io.github.tekisho.pingponggame.view.delegate;

public interface GameViewDelegate extends ViewDelegate {
    /**
     * Sets initial size of the view.
     */
    void handleSetInitialSize();

    /**
     * Resizes all view components to their corresponding relative positions and updates game space size.
     * @param w new view width
     * @param h new view height
     */
    void handleViewResize(double w, double h);

    /**
     * Hiding corresponding UI, and restarts the game.
     */
    void handleResetAndRestartGame();

    /**
     * Hiding corresponding UI, and resumes the game.
     */
    void handleResumeGame();

    /**
     * Opens settings view.
     */
    void handleOpenSettingsRequest();

    /**
     * Opens save/load view.
     */
    void handleOpenSaveLoadRequest();

    /**
     * Configures game input handler and starts the game.
     */
    void handleStartGame();
}
