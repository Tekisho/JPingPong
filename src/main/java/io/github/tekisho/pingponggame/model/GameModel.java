package io.github.tekisho.pingponggame.model;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
 * Represents model of game itself, contains other corresponding models (player, racket, ball), and relevant logic.
 */
public class GameModel implements Subject {
    // WSVGA resolution (128:75)
    public static final double DEFAULT_GAME_SPACE_WIDTH = 1024;
    public static final double DEFAULT_GAME_SPACE_HEIGHT = 600;

    private double gameSpaceWidth = DEFAULT_GAME_SPACE_WIDTH;
    private double gameSpaceHeight = DEFAULT_GAME_SPACE_HEIGHT;

    private static final int DEFAULT_GAME_END_SCORE = 10;
    private int gameEndScore = DEFAULT_GAME_END_SCORE;

    private static final double BOT_RACKET_VELOCITY_MULTIPLIER = 0.5;

    private final Set<Observer> observers = new HashSet<>();

    private AnimationTimer gameLoop;
    private GameInputHandler gameInputHandler;

    /**
     * States of the game.
     * @see #AWAITING_STARTUP
     * @see #START_COUNTDOWN
     * @see #RUNNING
     * @see #PAUSED
     * @see #PAUSED_HIDDEN
     * @see #OVER
     * @see #RESTARTING
     */
    public enum GameState {
        /**
         * Initial state, means that game has not been started yet.
         */
        AWAITING_STARTUP,

        /**
         * Means that the game is currently active, but not {@code RUNNING} (i.e., countdown still in progress).
         */
        START_COUNTDOWN,

        /**
         * Means that the game is currently active (i.e., running).
         */
        RUNNING,

        /**
         * Means that the game is paused, game loop is inactive.
         */
        PAUSED,

        /**
         * Same as {@code PAUSED}, but interactive UI is hidden.
         */
        PAUSED_HIDDEN,

        /**
         * Means that the game is finished, game loop is inactive.
         */
        OVER,

        /**
         * Means that the game is restarting, therefore it will start game loop and change state to {@code RUNNING} afterward.
         */
        RESTARTING
    }
    private GameState currentState;

    private final PlayerModel playerOneModel;
    private final PlayerModel playerTwoModel;
    private final BallModel ballModel;

    private PlayerModel winnerPlayer;
    private PlayerModel lastScoredPlayer; // TODO: Consider refactor, lastScoredPlayer should be reseted only during RESTARTING

    // TODO: Consider refactor. It would be more appropriate to consider removing "openSettingRequest" from the model, since its not related to the state of the game
    private boolean openSettingsRequest;

    public GameModel() {
        playerOneModel = new PlayerModel("Player 1");
        playerTwoModel = new PlayerModel("Player 2");
        ballModel = new BallModel();

        currentState = GameState.AWAITING_STARTUP;
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
        switchGameState(openSettingsRequest ? GameState.PAUSED : GameState.RUNNING);
        this.openSettingsRequest = openSettingsRequest;
    }

    /**
     * Represents game event handler, that listens to the keyboard input and allows to obtain currently active keys.
     */
    public class GameInputHandler implements EventHandler<KeyEvent> {
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

    /**
     * If not existed before, creates game loop along with game input handler.
     * @apiNote Game loop represented by anonymous class, that extends {@link AnimationTimer}.
     */
    private void createGameLoop() {
        if (gameLoop == null) {
            gameInputHandler = new GameInputHandler();

            gameLoop = new AnimationTimer() {
                {
                    resetGameObjectsPositions();
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

    // TODO: Implement logic for START_COUNTDOWN & PAUSED_HIDDEN states
    /**
     * Updates game depending on its current state.
     * @param now timestamp of the current frame given in nanoseconds
     * @param activeKeys set of current active keys
     * @implNote Simple Finite-State Machine.
     */
    private void updateGame (long now, Set<KeyCode> activeKeys) {
        switch (currentState) {
            case GameState.AWAITING_STARTUP -> {
                // do nothing
            }
            case GameState.START_COUNTDOWN -> {
                // do countdown
                switchGameState(GameState.RUNNING);
                updateGame(now, activeKeys);
            }
            case GameState.RUNNING -> {
                updateMiscellaneous(activeKeys);
                updatePlayerOneRacket(activeKeys);
                updatePlayerTwoRacket();
                updateBall();
            }
            case GameState.PAUSED, GameState.PAUSED_HIDDEN, GameState.OVER -> {
                gameLoop.stop();
            }
            case GameState.RESTARTING -> {
                resetGame();
                switchGameState(GameState.RUNNING);
            }
        }
    }

    /**
     * Switches state of the game to the new one.
     * @param state new state of the game
     * @implNote Automatically start game loop if state is {@code AWAITING_STARTUP}, or from {@code PAUSED} to {@code RUNNING}, or from {@code OVER} to {@code RESTARTING}.
     */
    public void switchGameState(GameState state) {
        if ((currentState == GameState.AWAITING_STARTUP) || (state == GameState.RUNNING && currentState == GameState.PAUSED) || (state == GameState.RESTARTING && currentState == GameState.OVER)) {
            gameLoop.start();
        }
        currentState = state;
    }
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Process updates for determining winner, and using shortcuts.
     * @param activeKeys set of current active keys
     */
    private void updateMiscellaneous(Set<KeyCode> activeKeys) {
        if (determineWinner()) {
            switchGameState(GameState.OVER);
        }

        if (activeKeys.contains(KeyCode.ESCAPE)) {
            gameInputHandler.removeActiveKey(KeyCode.ESCAPE);
            setOpenSettingsRequest(true);
        }

        if (activeKeys.contains(KeyCode.P)) {
            gameInputHandler.removeActiveKey(KeyCode.P);
            switchGameState(GameState.PAUSED);
        }

        if (activeKeys.contains(KeyCode.R)) {
            gameInputHandler.removeActiveKey(KeyCode.R);
            switchGameState(GameState.RESTARTING);
        }
    }

    /**
     * Updates racket of the first player allowing it to move depending on current active keys.
     * @param activeKeys set of current active keys
     */
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
    /**
     * Updates the racket of the second player (bot) based on primitive algorithm: if the ball going to the opposite direction - stop,
     * else if the ball is lower that the racket - move down, else (if the ball is higher) - move up.
     */
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

        // IDEA. If ball is not "caught", then move racket 1 second or until it reaches it center, then pause for 1 second
        // double currentSystemTime = System.currentTimeMillis();
        if (isBallLower || isBallHigher) {
            if (isBallLower) {
                racket.setDy(racket.getVelocity() * BOT_RACKET_VELOCITY_MULTIPLIER);
            } else {
                racket.setDy(-racket.getVelocity() * BOT_RACKET_VELOCITY_MULTIPLIER);
            }
        } else {
            racket.setDy(0);
        }
    }

    /**
     * Updates ball, considering hit logic.
     */
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
            resetRound();
        }

        ballModel.move(gameSpaceHeight, playerOneModel.getRacketModel(), playerTwoModel.getRacketModel());
    }

    /**
     * Fully resets game, and all it's corresponding properties (i.e., restarts game).
     */
    public void resetGame() {
        resetWinner();
        resetLastScoredPlayer();
        resetScores();
        resetGameObjectsPositions();
        resetRound();
    }

    /**
     * Begins new "round" of the game by resetting all corresponding properties.
     */
    public void resetRound() {
//        switchGameState(GameState.START_COUNTDOWN);
        ballModel.resetPosition(gameSpaceWidth, gameSpaceHeight, GameObjectModel.DefaultPosition.CENTER);
        ballModel.resetBounceCounter();
        ballModel.resetToBasicVelocity();
        ballModel.randomizeXyVelocity();
    }

    /**
     * Determines the winner by comparing player's scores.
     * @return {@code false} if none of the players reached or exceed game end score cap, otherwise {@code true}
     * @implNote In case scores are equal first player always becomes winner.
     */
    public boolean determineWinner() {
        int tempOneScore = playerOneModel.getScore();
        int tempTwoScore = playerTwoModel.getScore();

        if ((tempOneScore < gameEndScore) && (tempTwoScore < gameEndScore))
            return false;

        winnerPlayer = tempOneScore < tempTwoScore ? playerTwoModel : playerOneModel;
        return true;
    }

    /**
     * Resets winner (if exists), otherwise does nothing.
     */
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
    public void resetLastScoredPlayer() {
        lastScoredPlayer = null;
    }
    public PlayerModel getLastScoredPlayer() {
        return lastScoredPlayer;
    }

    /**
     * Resets position of each game object to the default one.
     */
    public void resetGameObjectsPositions() {
        playerOneModel.getRacketModel().resetPosition(gameSpaceWidth, gameSpaceHeight, GameObjectModel.DefaultPosition.LEFT);
        playerTwoModel.getRacketModel().resetPosition(gameSpaceWidth, gameSpaceHeight, GameObjectModel.DefaultPosition.RIGHT);
        ballModel.resetPosition(gameSpaceWidth, gameSpaceHeight, GameObjectModel.DefaultPosition.CENTER);
    }

    /**
     * Resets scores of all players to zero.
     */
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
