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

    // TODO: Modify to avoid stuck of game object
    /**
     * Calculates if collision exists with one of the collidingObjects using Axis-Aligned Bounding Box (AABB) alghorithm
     * @param collidingObjects
     * @return GameObjectModel if object is colliding with one of the collidingObjects, otherwise - null
     * @apiNote It is assumed that although there may be many objects that could cause a collision, the ball can only collide with one object at a time
     */
    private GameObjectModel isCollidingWith(GameObjectModel... collidingObjects) {
        for (GameObjectModel object : collidingObjects) {
            if (getX() < (object.getX() + object.getWidth()) && getX() + getWidth() > object.getX() &&
                getY() < (object.getY() + object.getHeight()) && getY() + getHeight() > object.getY()) {
                return object;
            }
        }
        return null;
    }
    private void resolveCollision(GameObjectModel collidingObject) {
        // ...
    }

    // TODO: Modify to calculate proper bouncing angle
    private void processRacketBouncing(GameObjectModel collidingObject) {
        if (collidingObject != null) {
            setDx(-getDx());

            double max = 2;
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
