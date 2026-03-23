package io.github.tekisho.pingponggame.view.delegate;

public interface SettingsViewDelegate extends ViewDelegate {
    /**
     * Applies all game model properties changes that has been made by the user.
     */
    void handleApplySettingsChanges();

    /**
     * Pauses the game while settings is open, resumes otherwise.
     */
    void handleGamePauseAndContinuation();
}
