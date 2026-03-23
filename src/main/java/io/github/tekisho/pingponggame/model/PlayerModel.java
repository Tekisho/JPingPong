package io.github.tekisho.pingponggame.model;

/**
 * Represents player model.
 * @implNote Player must always have the racket.
 */
public class PlayerModel {
    private String name;
    private int score;

    private final RacketModel racketModel;

    public PlayerModel(String name) {
        this(name, new RacketModel());
    }
    public PlayerModel(String name, RacketModel racketModel) {
        this.name = name;
        this.racketModel = racketModel;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = (name);
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Adds one score to the current score of the player.
     */
    public void addScore() {
        score++;
    }

    /**
     * Resets score of the player to zero.
     */
    public void resetScore() {
        score = 0;
    }

    public RacketModel getRacketModel() {
        return racketModel;
    }
}
