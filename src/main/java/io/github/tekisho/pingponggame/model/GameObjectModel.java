package io.github.tekisho.pingponggame.model;

import javafx.scene.paint.Color;

public abstract sealed class GameObjectModel permits RacketModel, BallModel {
    private double x;
    private double y;

    private double width;
    private double height;

    private double velocity;

    private double dx;
    private double dy;

    protected static final Color DEFAULT_FILL_COLOR = Color.rgb(245, 245, 245, 0.95);
    private Color color;

    protected GameObjectModel(double width, double height, double velocity) {
        this(width, height, velocity, DEFAULT_FILL_COLOR);
    }
    protected GameObjectModel(double width, double height, double velocity, Color color) {
        this.width = width;
        this.height = height;
        this.velocity = velocity;
        this.color = color;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getCenterX() {
        return getCenterCoordinate(x, width);
    }
    public double getCenterY() {
        return getCenterCoordinate(y, height);
    }
    private double getCenterCoordinate(double coordinate, double boundary) {
        return coordinate + (boundary / 2);
    }

    public void updatePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Updates coordinates, such that game object is centred based on xCenter & yCenter arguments.
     * @param xCenter
     * @param yCenter
     */
    public void updatePositionWithCentering(double xCenter, double yCenter) {
        updatePosition(xCenter - (width / 2), yCenter - (height / 2));
    }

    // TODO: Modify to properly consider velocity & delta time
    /**
     * Updates position (without centering) according to the game rules
     */
    public void move() {
        updatePosition(getX() + getDx(), getY() + getDy());
    }

    public enum DefaultPosition {
        LEFT,
        RIGHT,
        CENTER
    }
    public void resetPosition(double widthBoundary, double heightBoundary, DefaultPosition position) {
        switch (position) {
            case DefaultPosition.LEFT -> updatePositionWithCentering(50, heightBoundary/ 2);
            case DefaultPosition.RIGHT -> updatePositionWithCentering(widthBoundary - 50, heightBoundary / 2);
            case DefaultPosition.CENTER -> updatePositionWithCentering(widthBoundary / 2, heightBoundary / 2);
        }
    }

    /**
     * Updates size of the game object, keeping its original center coordinates (i.e., takes into account offset created by difference with the new
     * width & height).
     * @param width
     * @param height
     * @see #updatePositionWithCentering(double, double)
     */
    public void updateSize(double width, double height) {
        // Initial center coordinates (aka real object position) will differ after width & height change, so needs to be saved.
        double initialCenterX = getCenterX();
        double initialCenterY = getCenterY();

        this.width = width;
        this.height = height;

        updatePositionWithCentering(initialCenterX, initialCenterY);
    }

    // TODO: Modify to avoid stuck of game object
    /**
     * Calculates if collision exists with one of the collidingObjects using Axis-Aligned Bounding Box (AABB) alghorithm
     * @param collidingObjects
     * @return GameObjectModel if object is colliding with one of the collidingObjects, otherwise - null
     * @apiNote It is assumed that although there may be many objects that could cause a collision, the ball can only collide with one object at a time
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
    public void resolveCollision(GameObjectModel collidingObject) {
        // ...
    }

    public void updateSize(double radius) {
        updateSize(radius * 2, radius * 2);
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    public double getVelocity() {
        return velocity;
    }
    public void setVelocity(double velocity) {
        this.velocity = velocity;
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
}
