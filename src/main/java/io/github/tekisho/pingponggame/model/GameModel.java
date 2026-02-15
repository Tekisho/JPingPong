package io.github.tekisho.pingponggame.model;

import java.util.HashSet;
import java.util.Set;

// TODO: add method for starting game-loop.
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
