package io.github.tekisho.pingponggame.controller;

import io.github.tekisho.pingponggame.model.*;
import io.github.tekisho.pingponggame.view.GameView;
import io.github.tekisho.pingponggame.view.delegate.GameViewDelegate;
import javafx.scene.Scene;

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

    @Override
    public void handleSceneResize(double w, double h) {
        // TODO: stop the game while resezing the window
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

        gameModel.getPlayerOneModel().getRacketModel().updatePosition(
                playerOneRacketModel.getX(),
                racketOneRelY * (gameModel.getGameSpaceHeight() - playerOneRacketModel.getHeight())
        );
        gameModel.getPlayerTwoModel().getRacketModel().updatePosition(
                gameModel.getGameSpaceWidth() - (oldWidth - playerTwoRacketModel.getX()),
                racketTwoRelY * (gameModel.getGameSpaceHeight() - playerTwoRacketModel.getHeight())
        );
        gameModel.getBallModel().updatePosition(
                ballRelX * (gameModel.getGameSpaceWidth() - gameModel.getBallModel().getWidth()),
                ballRelY * (gameModel.getGameSpaceHeight() - gameModel.getBallModel().getHeight())
        );

        gameModel.notifyAllObservers();
    }

    @Override
    public void handleResetGameObjectPositions() {
        gameModel.resetGameObjectsPositions();
        gameModel.notifyAllObservers();
    }

    @Override
    public void handleResetGame() {
        gameView.setGameEndScreenVisibility(false);
        gameModel.resetGame();
    }

    /**
     * Starting up the game
     */
    @Override
    public void handleStartGame() {
        Scene gameScene = gameView.getScene();

        gameScene.setOnKeyPressed(gameModel.getGameInputHandler());
        gameScene.setOnKeyReleased(gameModel.getGameInputHandler());

        gameModel.startGameLoop();
    }

    @Override
    public void handleSettingsButtonClick() {
        openSettingsRequest.run();
    }

    // TODO: Modify to higlight last scored player label for couple of seconds to nofity the end-player
    @Override
    public void update() {
        if (gameModel.getOpenSettingsRequest()) {
            handleSettingsButtonClick();
        }

        updatePlayersAndScore();
        updateRackets();
        updateBall();
    }

    private void updatePlayersAndScore() {
        PlayerModel playerOneModel = gameModel.getPlayerOneModel();
        PlayerModel playerTwoModel = gameModel.getPlayerTwoModel();

        gameView.setPlayerOneName(playerOneModel.getName());
        gameView.setPlayerTwoName(playerTwoModel.getName());

        gameView.setScore(playerOneModel.getScore(), playerTwoModel.getScore());

        PlayerModel winnerPlayerModel = gameModel.getWinnerPlayer();
         if (winnerPlayerModel != null) {
             gameView.updateWinner(winnerPlayerModel.getName());
             gameView.setGameEndScreenVisibility(true);
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
