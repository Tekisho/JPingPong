package io.github.tekisho.pingponggame.model;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

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
//    private double lastStateChange;
    private final BallModel ballModel;

    private PlayerModel winnerPlayer;
    private PlayerModel lastScoredPlayer;

    private boolean openSettingsRequest;
    // TODO: add abilitity to request game restart by pressing 'R' (or restartGameButton inside the settingsView)
    private boolean gameRestartRequest;

    // FIXME: Decide, when to init. game-loop & input handler
    private AnimationTimer gameLoop;
    private GameInputHandler gameInputHandler;

    public GameModel() {
        playerOneModel = new PlayerModel("Player 1");
        playerTwoModel = new PlayerModel("Player 2");
        ballModel = new BallModel();

        createGameLoop();
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

    public PlayerModel getPlayerOneModel() {
        return playerOneModel;
    }
    public PlayerModel getPlayerTwoModel() {
        return playerTwoModel;
    }
    public BallModel getBallModel() {
        return ballModel;
    }

    public boolean getOpenSettingsRequest() {
        return openSettingsRequest;
    }
    public void setOpenSettingsRequest(boolean openSettingsRequest) {
        this.openSettingsRequest = openSettingsRequest;
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
        public void removeActiveKey(KeyCode keyCode) {
            activeKeys.remove(keyCode);
        }
    }
    public GameInputHandler getGameInputHandler() {
        return gameInputHandler;
    }

    private void createGameLoop() {
        if (gameLoop == null) {
            gameInputHandler = new GameInputHandler();

            gameLoop = new AnimationTimer() {
                {
                    ballModel.randomizeXyVelocity();
                }

                @Override
                public void handle(long now) {
                    updateGame(now, gameInputHandler.getActiveKeys());
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

    private void updateGame (long now, Set<KeyCode> activeKeys) {
        updateMiscellaneous(activeKeys);

        updatePlayerOneRacket(activeKeys);
        updatePlayerTwoRacket();
        updateBall();
    }

    private void updateMiscellaneous(Set<KeyCode> activeKeys) {
        if (determineWinner()) {
            gameLoop.stop();
        }

        if (activeKeys.contains(KeyCode.ESCAPE)) {
            openSettingsRequest = true;
            gameInputHandler.removeActiveKey(KeyCode.ESCAPE);
        }
    }

    private void updatePlayerOneRacket(Set<KeyCode> activeKeys) {
        RacketModel racket = playerOneModel.getRacketModel();

        boolean isMovingUp = activeKeys.contains(KeyCode.UP) || activeKeys.contains(KeyCode.W);
        boolean isMovingDown = activeKeys.contains(KeyCode.DOWN) || activeKeys.contains(KeyCode.S);

        double velocity = racket.getVelocity();
        racket.setDy(0);

        if (!(isMovingUp && isMovingDown)) {
            if (isMovingUp) {
                racket.setDy(-velocity);
            }
            else if (isMovingDown) {
                racket.setDy(velocity);
            }
        }

        racket.move(gameSpaceHeight);
    }

    // FIXME: fix jitter in movement by letting bot move in one direction for certain period of time before pause & repeat
    private void updatePlayerTwoRacket() {
        RacketModel racket = playerTwoModel.getRacketModel();
        racket.move(gameSpaceHeight);

        double ballCurrentDx = ballModel.getDx();
        if (ballCurrentDx <= 0) {
            racket.setDy(0);
            return;
        }

        double ballCurrentY = ballModel.getCenterY();

        boolean isBallLower = ballCurrentY + ballModel.getRadius() > racket.getY() + racket.getHeight();
        boolean isBallHigher = ballCurrentY - ballModel.getRadius() < racket.getY();

        // FIXME: IDEA. If ball is not "caught", then move racket 1s or unti it reaches it center, then pause for 1 s.
        //  double currentSystemTime = System.currentTimeMillis();

        if (isBallLower || isBallHigher) {
            if (isBallLower) {
                racket.setDy(racket.getVelocity() / 1.75);
            } else {
                racket.setDy(-racket.getVelocity() / 1.75);
            }
        } else {
            racket.setDy(0);
        }
    }

    private void updateBall() {
        boolean isHitByPlayerOne = ballModel.getX() + ballModel.getDx() > gameSpaceWidth - ballModel.getWidth();
        boolean isHitByPlayerTwo = ballModel.getX() + ballModel.getDx() < 0;

        if (isHitByPlayerOne || isHitByPlayerTwo) {
            if (isHitByPlayerOne) {
                playerOneModel.addScore();
                lastScoredPlayer = playerOneModel;
            }
            else {
                playerTwoModel.addScore();
                lastScoredPlayer = playerTwoModel;
            }
            ballModel.resetPosition(gameSpaceWidth, gameSpaceHeight, GameObjectModel.DefaultPosition.CENTER);
            ballModel.randomizeXyVelocity();
        }

        ballModel.move(gameSpaceHeight, playerOneModel.getRacketModel(), playerTwoModel.getRacketModel());
    }

    public void resetGame() {
        resetWinner();
        resetScores();
        resetGameObjectsPositions();
        gameLoop.start();
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

//        winnerPlayer = tempOneScore > tempTwoScore ? playerOneModel : playerTwoModel;
        winnerPlayer = lastScoredPlayer;
        return true;
    }
    public void resetWinner() {
        if (winnerPlayer != null)
            winnerPlayer = null;
    }
    public PlayerModel getWinnerPlayer() {
        return winnerPlayer;
    }

    public void setLastScoredPlayer(PlayerModel lastScoredPlayer) {
        this.lastScoredPlayer = lastScoredPlayer;
    }

    public void resetGameObjectsPositions() {
        playerOneModel.getRacketModel().resetPosition(gameSpaceWidth, gameSpaceHeight, GameObjectModel.DefaultPosition.LEFT);
        playerTwoModel.getRacketModel().resetPosition(gameSpaceWidth, gameSpaceHeight, GameObjectModel.DefaultPosition.RIGHT);
        ballModel.resetPosition(gameSpaceWidth, gameSpaceHeight, GameObjectModel.DefaultPosition.CENTER);
    }

    public void resetScores() {
        playerOneModel.resetScore();
        playerTwoModel.resetScore();
    }

    public int getGameEndScore () {
        return gameEndScore;
    }
    public void setGameEndScore (int gameEndScore) {
        this.gameEndScore = gameEndScore;
    }
}
