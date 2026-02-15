package io.github.tekisho.pingponggame.model;

public final class BallModel extends GameObjectModel {
    public static final double DEFAULT_RADIUS = 20;
    private double radius;

    public static final double DEFAULT_VELOCITY = 10;

    public BallModel() {
        this(DEFAULT_RADIUS, DEFAULT_VELOCITY);
    }
    public BallModel(double radius, double velocity) {
        super(radius * 2, radius * 2, velocity);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void updateSize(double radius) {
        super.updateSize(radius);
        this.radius = radius;
    }
}
