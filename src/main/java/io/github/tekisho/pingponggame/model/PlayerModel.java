package io.github.tekisho.pingponggame.model;


public class PlayerModel {
    private String name;
    private int score = 0;

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

    public RacketModel getRacketModel() {
        return racketModel;
    }
}
