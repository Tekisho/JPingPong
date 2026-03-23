package io.github.tekisho.pingponggame.model;

import javafx.scene.paint.Color;

/**
 * Represents model of the game object (which itself restricted to racket or ball).
 */
public abstract sealed class GameObjectModel permits RacketModel, BallModel {
    private double x;
    private double y;

    private double width;
    private double height;

    private double basicVelocity;   // Allows keep initial (or last set) value
    private double velocity;

    // Velocity projections on OX & OY
    private double dx;
    private double dy;

    private static final Color DEFAULT_FILL_COLOR = Color.rgb(245, 245, 245, 0.97);
    private Color color;

    protected GameObjectModel(double width, double height, double velocity) {
        this(width, height, velocity, DEFAULT_FILL_COLOR);
    }
    protected GameObjectModel(double width, double height, double velocity, Color color) {
        this.width = width;
        this.height = height;
        this.basicVelocity = velocity;
        this.velocity = this.basicVelocity;
        this.color = color;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getCenterX() {
        return calculateCenterCoordinate(x, width);
    }
    public double getCenterY() {
        return calculateCenterCoordinate(y, height);
    }

    public double getDx() {
        return dx;
    }
    public void setDx(double dx) {
        this.dx = dx;
    }
    public double getDy() {
        return dy;
    }
    public void setDy(double dy) {
        this.dy = dy;
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    public double getBasicVelocity() {
        return basicVelocity;
    }
    public void setBasicVelocity(double basicVelocity) {
        this.basicVelocity = basicVelocity;
    }

    public double getVelocity() {
        return velocity;
    }
    public void setVelocity(double velocity) {
        this.velocity = velocity;

        // If object is moving, to preserve direction normalization of vector is required.
        double currentVelocity = Math.sqrt(dx*dx + dy*dy);
        if (currentVelocity > 0) {
            double normalizedDx = dx / currentVelocity;
            double normalizedDy = dy / currentVelocity;

            dx = normalizedDx * velocity;
            dy = normalizedDy * velocity;
        }
    }

    /**
     * Resets (current) velocity to the basic one (aka last set or initial).
     */
    public void resetToBasicVelocity() {
        setVelocity(basicVelocity);
    }

    /**
     * Calculates center value of the coordinate based on boundary.
     * @param coordinate x / y
     * @param boundary width, height, or similar
     * @return center coordinate
     */
    protected double calculateCenterCoordinate(double coordinate, double boundary) {
        return coordinate + (boundary / 2);
    }

    public void updatePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Updates coordinates, such that game object is centred based on point (xCenter, yCenter).
     * @param xCenter destination OX coordinate
     * @param yCenter destination OY coordinate
     * @see #updatePosition(double, double)
     */
    public void updatePositionWithCentering(double xCenter, double yCenter) {
        updatePosition(xCenter - (width / 2), yCenter - (height / 2));
    }

    // TODO: Modify to consider delta time.
    /**
     * Moves object from its initial coordinates (x,y) using corresponding to axes velocity values (dx, dy)
     */
    public void move() {
        updatePosition(getX() + getDx(), getY() + getDy());
    }

    /**
     * Set position of the object to one of the default ones considering padding.
     * @param widthBoundary object container width
     * @param heightBoundary object container height
     * @param position target position (LEFT / RIGHT / CENTER)
     */
    public void resetPosition(double widthBoundary, double heightBoundary, double padding, DefaultPosition position) {
        switch (position) {
            case DefaultPosition.LEFT -> updatePositionWithCentering(padding, heightBoundary/ 2);
            case DefaultPosition.RIGHT -> updatePositionWithCentering(widthBoundary - padding, heightBoundary / 2);
            case DefaultPosition.CENTER -> updatePositionWithCentering(widthBoundary / 2, heightBoundary / 2);
        }
    }

    /**
     * Same as {@link #resetPosition(double, double, double, DefaultPosition)}, but uses default padding.
     */
    public void resetPosition(double widthBoundary, double heightBoundary, DefaultPosition position) {
        resetPosition(widthBoundary, heightBoundary, DEFAULT_PADDING, position);
    }
    public static final double DEFAULT_PADDING = 50;
    public enum DefaultPosition {
        LEFT,
        RIGHT,
        CENTER
    }

    /**
     * Updates size of the object, re-centering it based on the original position (i.e., takes into account offset created by difference with the new
     * width & height).
     * @param width new width of the object
     * @param height new height of the object
     * @see #updatePositionWithCentering(double, double)
     */
    public void updateSize(double width, double height) {
        // Initial center coordinates (aka real object position) will differ after width & height change, so needs to be saved
        double initialCenterX = getCenterX();
        double initialCenterY = getCenterY();

        this.width = width;
        this.height = height;

        updatePositionWithCentering(initialCenterX, initialCenterY);
    }

    /**
     * Same as {@link #updateSize(double, double)}, but use radius instead of width & height.
     * @param radius radius of the object
     */
    public void updateSize(double radius) {
        updateSize(radius * 2, radius * 2);
    }

    /**
     * Calculates if collision exists with one of the collidingObjects.
     * @param collidingObjects collection of all objects to collide with
     * @return the first colliding occurrence, otherwise - {@code null}
     * @apiNote It is assumed that although there may be many objects that could cause a collision, object can only collide with one object at a time.
     * @implNote Axis-Aligned Bounding Box (AABB) algorithm used.
     */
    public GameObjectModel isCollidingWith(GameObjectModel... collidingObjects) {
        for (GameObjectModel object : collidingObjects) {
            if (getX() < (object.getX() + object.getWidth()) && getX() + getWidth() > object.getX() &&
                    getY() < (object.getY() + object.getHeight()) && getY() + getHeight() > object.getY()) {
                return object;
            }
        }
        return null;
    }

    /**
     * STUB: "resolves" overlapping (aka stuck) collision between collidingObject & collisionObject
     * @param collidingObject object to collide with
     * @param collisionObject initial object (which have a collision)
     */
    public void resolveCollision(GameObjectModel collidingObject, GameObjectModel collisionObject) {
        // TODO: Implement "resolving" of (stuck) collision by pushing object out
    }
}
