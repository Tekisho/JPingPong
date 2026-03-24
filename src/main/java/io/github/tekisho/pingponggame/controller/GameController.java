package io.github.tekisho.pingponggame.controller;

import io.github.tekisho.pingponggame.model.*;
import io.github.tekisho.pingponggame.view.GameView;
import io.github.tekisho.pingponggame.view.delegate.GameViewDelegate;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// TODO: Fix bug when min the game window - it accelerates (since not cons. delta time). 1) consider delta-time, 2) stop game when window is minimized
/**
 * Represents game controller, that handles user input and allows interaction with game model through game view.
 */
public class GameController implements GameViewDelegate, Observer {
    private final GameModel gameModel;
    private final GameView gameView;

    private Runnable openSettingsRequest;

    private final ScheduledExecutorService scheduler;

    public GameController(final GameView gameView, final GameModel gameModel) {
        this.gameModel = gameModel;
        this.gameView = gameView;

        gameModel.attachObserver(this);
        gameView.setDelegate(this);

        scheduler = Executors.newSingleThreadScheduledExecutor();

        update();
    }

    public void setOpenSettingsRequest(Runnable runnable) {
        openSettingsRequest = runnable;
    }

    @Override
    public void handleSetInitialSize() {
        gameView.setPrefSize(GameModel.DEFAULT_GAME_SPACE_WIDTH, GameModel.DEFAULT_GAME_SPACE_HEIGHT);
    }

    @Override
    public void handleViewResize(double w, double h) {
        // TODO: consider temp. stopping the game while resizing the window
        GameObjectModel playerOneRacketModel = gameModel.getPlayerOneModel().getRacketModel();
        GameObjectModel playerTwoRacketModel = gameModel.getPlayerTwoModel().getRacketModel();
        GameObjectModel ballModel = gameModel.getBallModel();

        // Old width and/or height is required to find relative position in available moving space (aka H/W - obj h/w)
        double oldWidth = gameModel.getGameSpaceWidth();
        double oldHeight = gameModel.getGameSpaceHeight();

        double racketOneRelY = playerOneRacketModel.getY() / (oldHeight - playerOneRacketModel.getHeight());
        double racketTwoRelY = playerTwoRacketModel.getY() / (oldHeight - playerTwoRacketModel.getHeight());

        double ballRelX = ballModel.getX() / (oldWidth - ballModel.getWidth());
        double ballRelY = ballModel.getY() / (oldHeight - ballModel.getHeight());

        gameModel.updateGameSpaceSize(w, h);

        double newWidth = gameModel.getGameSpaceWidth();
        double newHeight = gameModel.getGameSpaceHeight();

        playerOneRacketModel.updatePosition(
                playerOneRacketModel.getX(),
                racketOneRelY * (newHeight - playerOneRacketModel.getHeight())
        );
        playerTwoRacketModel.updatePosition(
                newWidth - (oldWidth - playerTwoRacketModel.getX()),
                racketTwoRelY * (newHeight - playerTwoRacketModel.getHeight())
        );
        ballModel.updatePosition(
                ballRelX * (newWidth - ballModel.getWidth()),
                ballRelY * (newHeight - ballModel.getHeight())
        );

        gameModel.notifyAllObservers();
    }

    @Override
    public void handleResetAndRestartGame() {
        gameView.setGameEndScreenVisibility(false);
        gameModel.switchGameState(GameModel.GameState.RESTARTING);
    }

    @Override
    public void handleResumeGame() {
        gameView.setGamePauseScreenVisibility(false);
        gameModel.switchGameState(GameModel.GameState.RUNNING);
    }

    @Override
    public void handleStartGame() {
        Scene gameScene = gameView.getScene();

        gameScene.setOnKeyPressed(gameModel.getGameInputHandler());
        gameScene.setOnKeyReleased(gameModel.getGameInputHandler());

        gameModel.switchGameState(GameModel.GameState.RUNNING);
    }

    @Override
    public void handleOpenSettingsRequest() {
        openSettingsRequest.run();
    }

    @Override
    public void update() {
        updateMiscellaneous();
        updatePlayersAndScore();
        updateRackets();
        updateBall();
    }

    private void updateMiscellaneous() {
        gameView.setGamePauseScreenVisibility((gameModel.getCurrentState() == GameModel.GameState.PAUSED));

        if (gameModel.getOpenSettingsRequest()) {
            handleOpenSettingsRequest();
        }
    }

    private void updatePlayersAndScore() {
        PlayerModel playerOneModel = gameModel.getPlayerOneModel();
        PlayerModel playerTwoModel = gameModel.getPlayerTwoModel();

        gameView.setPlayerOneName(playerOneModel.getName());
        gameView.setPlayerTwoName(playerTwoModel.getName());

        gameView.setScore(playerOneModel.getScore(), playerTwoModel.getScore());

        PlayerModel winnerPlayerModel = gameModel.getWinnerPlayer();
         if (winnerPlayerModel != null && gameModel.getCurrentState() == GameModel.GameState.OVER) {
             gameView.updateWinner(winnerPlayerModel.getName());
             gameView.setGameEndScreenVisibility(true);
         }

         // FIXME: modify to append resetting highlight if player scored again
         PlayerModel lastScoredPlayer = gameModel.getLastScoredPlayer();
         if (lastScoredPlayer != null && gameModel.getCurrentState() == GameModel.GameState.RUNNING) {
             gameModel.resetLastScoredPlayer();
             gameView.removeHighlightOnPlayerOneLabel();
             gameView.removeHighlightOnPlayerTwoLabel();

             if (lastScoredPlayer.equals(gameModel.getPlayerOneModel())) {
                 gameView.addHighlightOnPlayerOneLabel();
             } else {
                 gameView.addHighlightOnPlayerTwoLabel();
             }

             scheduler.schedule(() -> {
                 Platform.runLater(() -> {
                     gameView.removeHighlightOnPlayerOneLabel();
                     gameView.removeHighlightOnPlayerTwoLabel();
                 });
             }, 2, TimeUnit.SECONDS);
         }
    }

    private void updateRackets() {
        GameObjectModel playerOneRacketModel = gameModel.getPlayerOneModel().getRacketModel();
        GameObjectModel playerTwoRacketModel = gameModel.getPlayerTwoModel().getRacketModel();

        gameView.renderPlayerOneRacket(
                playerOneRacketModel.getX(),
                playerOneRacketModel.getY(),
                playerOneRacketModel.getWidth(),
                playerOneRacketModel.getHeight(),
                playerOneRacketModel.getColor()
        );

        gameView.renderPlayerTwoRacket(
                playerTwoRacketModel.getX(),
                playerTwoRacketModel.getY(),
                playerTwoRacketModel.getWidth(),
                playerTwoRacketModel.getHeight(),
                playerTwoRacketModel.getColor()
        );
    }

    private void updateBall() {
        GameObjectModel ballModel = gameModel.getBallModel();

        gameView.renderBall(
                ballModel.getX(),
                ballModel.getY(),
                ballModel.getWidth(),
                ballModel.getHeight(),
                ballModel.getColor()
        );
    }
}
