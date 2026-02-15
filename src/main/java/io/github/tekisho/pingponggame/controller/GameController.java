package io.github.tekisho.pingponggame.controller;

import io.github.tekisho.pingponggame.model.*;
import io.github.tekisho.pingponggame.view.GameView;
import io.github.tekisho.pingponggame.view.delegate.GameViewDelegate;

public class GameController implements GameViewDelegate, Observer {
    private final GameModel gameModel;
    private final GameView gameView;

    private Runnable openSettingsRequest;

    public GameController(final GameView gameView, final GameModel gameModel) {
        this.gameModel = gameModel;
        this.gameView = gameView;

        gameModel.attachObserver(this);
        gameView.setDelegate(this);

        update();
    }

    public void setOpenSettingsRequest(Runnable runnable) {
        openSettingsRequest = runnable;
    }

    @Override
    public void handleSetInitialSize() {
        gameView.setPrefSize(GameModel.DEFAULT_GAME_SPACE_WIDTH, GameModel.DEFAULT_GAME_SPACE_HEIGHT);
    }

    // TODO: Implement components repositioning / scaling, during gameSpace resize.
    @Override
    public void handleSceneResize(double w, double h) {
        // find offset / relative position

        gameModel.updateGameSpaceSize(w, h);
        // subtract offset / relative position from new gameSpace width

        gameModel.notifyAllObservers();
    }

    @Override
    public void handleResetGameObjectPositions() {
        gameModel.getPlayerOneModel().getRacketModel().updatePositionWithCentering(50, gameModel.getGameSpaceHeight() / 2);
        gameModel.getPlayerTwoModel().getRacketModel().updatePositionWithCentering(gameModel.getGameSpaceWidth() - 50, gameModel.getGameSpaceHeight() / 2);
        gameModel.getBallModel().updatePositionWithCentering(gameModel.getGameSpaceWidth() / 2, gameModel.getGameSpaceHeight() / 2);

        gameModel.notifyAllObservers();
    }

    @Override
    public void handleResetGame() {
        gameModel.resetWinner();
        gameModel.getPlayerOneModel().setScore(0);
        gameModel.getPlayerTwoModel().setScore(0);

        handleResetGameObjectPositions();

        gameView.toggleGameEndScreen();
    }

    @Override
    public void handleSettingsButtonClick() {
        openSettingsRequest.run();
    }

    @Override
    public void update() {
        updatePlayersAndScore();
        updateRackets();
        updateBall();
    }

    private void updatePlayersAndScore() {
        gameView.setPlayerOneName(gameModel.getPlayerOneModel().getName());
        gameView.setPlayerTwoName(gameModel.getPlayerTwoModel().getName());

        gameView.setScore(gameModel.getPlayerOneModel().getScore(), gameModel.getPlayerTwoModel().getScore());

         if (gameModel.determineWinner()) {
             gameView.updateWinner(gameModel.getWinnerPlayer().getName());
             gameView.toggleGameEndScreen();
         }
    }

    private void updateRackets() {
        gameView.renderPlayerOneRacket(
                gameModel.getPlayerOneModel().getRacketModel().getX(),
                gameModel.getPlayerOneModel().getRacketModel().getY(),
                gameModel.getPlayerOneModel().getRacketModel().getWidth(),
                gameModel.getPlayerOneModel().getRacketModel().getHeight(),
                gameModel.getPlayerOneModel().getRacketModel().getColor()
        );

        gameView.renderPlayerTwoRacket(
                gameModel.getPlayerTwoModel().getRacketModel().getX(),
                gameModel.getPlayerTwoModel().getRacketModel().getY(),
                gameModel.getPlayerTwoModel().getRacketModel().getWidth(),
                gameModel.getPlayerTwoModel().getRacketModel().getHeight(),
                gameModel.getPlayerTwoModel().getRacketModel().getColor()
        );
    }

    private void updateBall() {
        gameView.renderBall(
                gameModel.getBallModel().getX(),
                gameModel.getBallModel().getY(),
                gameModel.getBallModel().getWidth(),
                gameModel.getBallModel().getHeight(),
                gameModel.getBallModel().getColor()
        );
    }
}
