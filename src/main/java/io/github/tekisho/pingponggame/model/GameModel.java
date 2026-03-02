package io.github.tekisho.pingponggame.model;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameModel implements Subject {
    // WSVGA resolution (128:75)
    public static final double DEFAULT_GAME_SPACE_WIDTH = 1024;
    public static final double DEFAULT_GAME_SPACE_HEIGHT = 600;

    private double gameSpaceWidth = DEFAULT_GAME_SPACE_WIDTH;
    private double gameSpaceHeight = DEFAULT_GAME_SPACE_HEIGHT;

    public static final int DEFAULT_GAME_END_SCORE = 10;
    private int gameEndScore = DEFAULT_GAME_END_SCORE;

    private final Set<Observer> observers = new HashSet<>();

    private final PlayerModel playerOneModel;
    private final PlayerModel playerTwoModel;
    private final BallModel ballModel;

    private PlayerModel winnerPlayer;

    private AnimationTimer gameLoop;
    private GameInputHandler gameInputHandler;

    public GameModel() {
        playerOneModel = new PlayerModel("Player 1");
        playerTwoModel = new PlayerModel("Player 2");
        ballModel = new BallModel();
    }

    @Override
    public void attachObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detachObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    // Business Logic (aka game loop related)
    class GameInputHandler implements EventHandler<KeyEvent> {
        private final Set<KeyCode> activeKeys = new HashSet<>();

        @Override
        public void handle(KeyEvent keyEvent) {
            if (KeyEvent.KEY_PRESSED.equals(keyEvent.getEventType()) ) {
                activeKeys.add(keyEvent.getCode());
            } else if (KeyEvent.KEY_RELEASED.equals(keyEvent.getEventType())) {
                activeKeys.remove(keyEvent.getCode());
            }
        }

        public Set<KeyCode> getActiveKeys() {
            return Collections.unmodifiableSet(activeKeys);
        }
    }
    public void createGameLoop() {
        if (gameLoop == null) {
            gameInputHandler = new GameInputHandler();

            gameLoop = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    updateGame(now, gameInputHandler.getActiveKeys());
//                if (gameState.isGameOver()) {
//                    this.stop();
//                }
                    notifyAllObservers();
                }
            };
        }
    }

    public void startGameLoop() {
        gameLoop.start();
    }
    public void stopGameLoop() {
        gameLoop.stop();
    }

    public GameInputHandler getGameInputHandler() {
        return gameInputHandler;
    }

    private void updateGame (long now, Set<KeyCode> activeKeys) {
        updatePlayerOneRacket(activeKeys);
    }
    private void updatePlayerOneRacket(Set<KeyCode> activeKeys) {
        final double currentX = playerOneModel.getRacketModel().getX();
        final double currentY = playerOneModel.getRacketModel().getY();

        if (!((activeKeys.contains(KeyCode.UP) || activeKeys.contains(KeyCode.W)) && (activeKeys.contains(KeyCode.DOWN) || activeKeys.contains(KeyCode.S)))) {
            if (activeKeys.contains(KeyCode.UP) || activeKeys.contains(KeyCode.W))
                playerOneModel.getRacketModel().move(currentX, currentY - playerOneModel.getRacketModel().getVelocity(), gameSpaceHeight);
            else if (activeKeys.contains(KeyCode.DOWN) || activeKeys.contains(KeyCode.S))
                playerOneModel.getRacketModel().move(currentX, currentY + playerOneModel.getRacketModel().getVelocity(), gameSpaceHeight);
        }
    }

    public void updateGameSpaceSize(double gameSpaceWidth, double gameSpaceHeight) {
        this.gameSpaceWidth = gameSpaceWidth;
        this.gameSpaceHeight = gameSpaceHeight;
    }
    public double getGameSpaceWidth() {
        return gameSpaceWidth;
    }
    public double getGameSpaceHeight() {
        return gameSpaceHeight;
    }

    /**
     * Determines the winner by comparing player's scores.
     * @return  false if none of the players reached / exceed game end score cap, otherwise true
     */
    public boolean determineWinner() {
        int tempOneScore = playerOneModel.getScore();
        int tempTwoScore = playerTwoModel.getScore();

        if ((tempOneScore < gameEndScore) && (tempTwoScore < gameEndScore))
            return false;

        winnerPlayer = tempOneScore > tempTwoScore ? playerOneModel : playerTwoModel;
        return true;
    }
    public void resetWinner() {
        if (winnerPlayer != null)
            winnerPlayer = null;
    }
    public PlayerModel getWinnerPlayer() {
        return winnerPlayer;
    }

    // Player & Game Score related
    public PlayerModel getPlayerOneModel() {
        return playerOneModel;
    }
    public PlayerModel getPlayerTwoModel() {
        return playerTwoModel;
    }

    public int getGameEndScore () {
        return gameEndScore;
    }
    public void changeGameEndScore (int gameEndScore) {
        this.gameEndScore = gameEndScore;
    }

    // Ball related
    public BallModel getBallModel() {
        return ballModel;
    }
}
