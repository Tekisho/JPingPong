package io.github.tekisho.pingponggame.model;

public final class RacketModel extends GameObjectModel{
    public static final double DEFAULT_WIDTH = 20;
    public static final double DEFAULT_HEIGHT = 90;
    public static final double DEFAULT_VELOCITY = 5;

    public RacketModel() {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_VELOCITY);
    }

    public RacketModel(double width, double height, double velocity) {
        super(width, height, velocity);
    }
}