package io.github.tekisho.pingponggame.controller;

import io.github.tekisho.pingponggame.model.GameModel;
import io.github.tekisho.pingponggame.model.Observer;
import io.github.tekisho.pingponggame.view.SettingsView;
import io.github.tekisho.pingponggame.view.delegate.SettingsViewDelegate;

/**
 * Represents settings controller, that handles user input and allows interaction with game model through settings view.
 */
public class SettingsController implements SettingsViewDelegate, Observer {
    private final SettingsView settingsView;
    private final GameModel gameModel;

    public SettingsController(final SettingsView settingsView, final GameModel gameModel) {
        this.settingsView = settingsView;
        this.gameModel = gameModel;

        gameModel.attachObserver(this);
        settingsView.setDelegate(this);

        update();
    }

    @Override
    public void handleApplySettingsChanges() {
        gameModel.getPlayerOneModel().setName(settingsView.getPlayerOneName());
        gameModel.getPlayerTwoModel().setName(settingsView.getPlayerTwoName());

        gameModel.getPlayerOneModel().setScore(settingsView.getPlayerOneScore());
        gameModel.getPlayerTwoModel().setScore(settingsView.getPlayerTwoScore());
        if (settingsView.getPlayerOneScore() > settingsView.getPlayerTwoScore()) {
            gameModel.setLastScoredPlayer(gameModel.getPlayerOneModel());
        } else if (settingsView.getPlayerOneScore() < settingsView.getPlayerTwoScore()) {
            gameModel.setLastScoredPlayer(gameModel.getPlayerTwoModel());
        }

        gameModel.setGameEndScore(settingsView.getGameEndScore());

        gameModel.getPlayerOneModel().getRacketModel().updateSize(settingsView.getGameRacketWidth(), settingsView.getGameRacketHeight());
        gameModel.getPlayerTwoModel().getRacketModel().updateSize(settingsView.getGameRacketWidth(), settingsView.getGameRacketHeight());
        gameModel.getBallModel().updateSize(settingsView.getGameBallRadius());

        gameModel.getPlayerOneModel().getRacketModel().setVelocity(settingsView.getGameRacketVelocity());
        gameModel.getPlayerTwoModel().getRacketModel().setVelocity(settingsView.getGameRacketVelocity());
        gameModel.getBallModel().setBasicVelocity(settingsView.getGameBallVelocity());

        gameModel.notifyAllObservers();
    }

    @Override
    public void handleGamePauseAndContinuation() {
        settingsView.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != newValue) {
                newValue.windowProperty().addListener((observable1, oldValue1, newValue1) -> {
                    newValue1.setOnShowing((windowEvent -> gameModel.switchGameState(GameModel.GameState.PAUSED)));
                    newValue1.setOnCloseRequest((windowEvent -> {
                        gameModel.setOpenSettingsRequest(false);
                        gameModel.switchGameState(GameModel.GameState.RUNNING);
                    }));
                });
            }
        });
    }

    @Override
    public void update() {
        updatePlayersAndEndGameScore();
        updateRackets();
        updateBall();
    }

    private void updatePlayersAndEndGameScore() {
        settingsView.setPlayerOneName(gameModel.getPlayerOneModel().getName());
        settingsView.setPlayerTwoName(gameModel.getPlayerTwoModel().getName());

        settingsView.setPlayerOneScore(gameModel.getPlayerOneModel().getScore());
        settingsView.setPlayerTwoScore(gameModel.getPlayerTwoModel().getScore());

        settingsView.setGameEndScore(gameModel.getGameEndScore());
    }

    private void updateRackets() {
        settingsView.setGameRacketWidth(gameModel.getPlayerOneModel().getRacketModel().getWidth());
        settingsView.setGameRacketHeight(gameModel.getPlayerOneModel().getRacketModel().getHeight());
        settingsView.setGameRacketVelocity(gameModel.getPlayerOneModel().getRacketModel().getVelocity());
    }

    private void updateBall() {
        settingsView.setGameBallRadius(gameModel.getBallModel().getRadius());
        settingsView.setGameBallVelocity(gameModel.getBallModel().getBasicVelocity());
    }
}
