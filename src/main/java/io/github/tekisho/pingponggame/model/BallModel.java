package io.github.tekisho.pingponggame.model;

public final class BallModel extends GameObjectModel {
    public static final double DEFAULT_RADIUS = 20;
    private double radius;

    public static final double DEFAULT_VELOCITY = 2;

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

    @Override
    public double getCenterX() {
        return getCenterCoordinate(getX());
    }
    @Override
    public double getCenterY() {
        return getCenterCoordinate(getY());
    }
    private double getCenterCoordinate(double coordinate) {
        return coordinate + radius;
    }

    @Override
    public void updateSize(double radius) {
        super.updateSize(radius);
        this.radius = radius;
    }

    public void move(double heightBoundary, GameObjectModel... collidingObjects) {
        if (getY() + getDy() < 0 || getY() + getDy() > heightBoundary - getHeight()) {
            setDy(-getDy());
        }
        processRacketBouncing(isCollidingWith(collidingObjects));
        move();
    }

    // TODO: Modify to calculate proper bouncing angle and properly use NEW velocity
    private void processRacketBouncing(GameObjectModel collidingObject) {
        if (collidingObject != null) {
            setDx(-getDx());

            double max = getVelocity();
            double min = -max;
            double randomValue = min + Math.random() * (max - min);
            setDy(randomValue);
        }
    }

    public void randomizeXyVelocity() {
        setDx(Math.random() <= 0.5 ? getVelocity() : -getVelocity());
        setDy(Math.random() <= 0.5 ? getVelocity() : -getVelocity());
    }
}
