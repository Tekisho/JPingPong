package io.github.tekisho.pingponggame.model;

/**
 * Represents Racket, concrete {@link GameObjectModel}.
 */
public final class RacketModel extends GameObjectModel{
    private static final double DEFAULT_WIDTH = 20;
    private static final double DEFAULT_HEIGHT = 90;
    private static final double DEFAULT_VELOCITY = 3;

    public RacketModel() {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_VELOCITY);
    }

    public RacketModel(double width, double height, double velocity) {
        super(width, height, velocity);
    }

    /**
     * Moves racket, considering borders.
     * @param heightBoundary height of the object container
     * @see #move()
     */
    public void move(double heightBoundary) {
        if (getY() + getDy() < 0) {
            updatePosition(getX() + getDx(), 0);
        } else if (getY() + getDy() > heightBoundary - getHeight()) {
            updatePosition(getX() + getDx(), heightBoundary - getHeight());
        } else {
            move();
        }
    }
}