package io.github.tekisho.pingponggame.model;

import javafx.scene.paint.Color;

public abstract sealed class GameObjectModel permits RacketModel, BallModel {
    private double x = 0;
    private double y = 0;

    private double width;
    private double height;

    private double velocity;

    protected static final Color DEFAULT_FILL_COLOR = Color.rgb(255, 255, 255, 0.75);
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
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getCenterX() {
        return x + (width / 2);
    }
    public double getCenterY() {
        return y + (height / 2);
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

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
}
