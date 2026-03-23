package io.github.tekisho.pingponggame.model;

// TODO: Consider refactor of bouncing methods; they may refer to any game object, not only ball
/**
 * Represents Ball, concrete {@link GameObjectModel}. Include physics logic (i.e., bouncing & speed increase after it).
 */
public final class BallModel extends GameObjectModel {
    private static final double DEFAULT_RADIUS = 20;
    private double radius;

    private static final double DEFAULT_VELOCITY = 2;

    private static final int INCREASING_AFTER_BOUNCE_CAP = 2;
    private static final double INCREASING_AFTER_BOUNCE_FACTOR = 1.5;
    private int bounceCounter;

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
        return calculateCenterCoordinate(getX());
    }
    @Override
    public double getCenterY() {
        return calculateCenterCoordinate(getY());
    }

    /**
     * Same as {@link #calculateCenterCoordinate(double, double)}, but uses radius as boundary.
     */
    private double calculateCenterCoordinate(double coordinate) {
        return calculateCenterCoordinate(coordinate, 2 * radius);
    }

    @Override
    public void updateSize(double radius) {
        super.updateSize(radius);
        this.radius = radius;
    }

    /**
     * Moves ball, considering bouncing from borders & colliding objects.
     * @param heightBoundary height of the object container
     * @param collidingObjects objects to collide with
     * @see #move()
     */
    public void move(double heightBoundary, GameObjectModel... collidingObjects) {
        processBouncingFromTopDownBorders(heightBoundary);
        processBouncingFromRacket(isCollidingWith(collidingObjects));
        move();
    }

    /**
     * Inverts OY velocity if object collides with one of the borders.
     * @param heightBoundary height of the object container
     */
    private void processBouncingFromTopDownBorders(double heightBoundary) {
        final double newY = getY() + getDy();
        if (newY < 0 || newY > heightBoundary - getHeight()) {
            setDy(-getDy());
        }
    }

    // TODO: Modify to properly calculate bouncing angle and velocity
    /**
     * Calculates OX & OY velocity if object collides with colliding object, otherwise do nothing.
     * @param collidingObject object to collide with
     */
    private void processBouncingFromRacket(GameObjectModel collidingObject) {
        if (collidingObject == null) {
            return;
        }

        // Allows to bounce after each (INCREASING_AFTER_BOUNCE_CAP - 1) hit
        if (bounceCounter == INCREASING_AFTER_BOUNCE_CAP - 1) {
            setVelocity(getVelocity() * INCREASING_AFTER_BOUNCE_FACTOR);
            bounceCounter = 0;
        } else {
            bounceCounter++;
        }

        setDx(-getDx());

        double max = getVelocity();
        double min = -max;
        double randomValue = min + Math.random() * (max - min);
        setDy(randomValue);
    }

    /**
     * Resets bounce counter to zero.
     */
    public void resetBounceCounter() {
        bounceCounter = 0;
    }

    // TODO: Modify to properly calculate random velocity. Add option to generate ANY angle or ONLY DIAGONAL
    /**
     * Randomize velocity in way that object will move diagonally with speed capped by its current velocity.
     */
    public void randomizeXyVelocity() {
        setDx(Math.random() <= 0.5 ? getVelocity() : -getVelocity());
        setDy(Math.random() <= 0.5 ? getVelocity() : -getVelocity());

//        double randomAngle = Math.random() * 2 * Math.PI;
//        setDx(Math.cos(randomAngle) * getVelocity());
//        setDy(Math.sin(randomAngle) * getVelocity());
    }
}
